/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.model;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.io.ByteOrder;
import com.liaquay.tinyx.model.eventfactories.EventFactories;
import com.liaquay.tinyx.model.eventfactories.KeyFactory;
import com.liaquay.tinyx.model.eventfactories.MappingNotifyFactory;

/**
 * 
 */
public class Server extends Client {

	private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

	private static final byte[] VENDOR = "Liaquay".getBytes();

	// Formats pilfered from XFree86 and validated against tinyx output
	private static Format[] FORMATS = new Format[] {
		new Format (1, 1, 32),
		new Format (4, 8, 32),
		new Format (8, 8, 32),
		new Format (15, 16, 32),
		new Format (16, 16, 32),
		new Format (24, 32, 32),
		new Format (32, 32, 32)
	};

	private final AccessControls _accessControl = new AccessControls();
	private final Extensions _extensions = new Extensions();
	private final Clients _clients = new Clients();
	private final Keyboard _keyboard;
	private final List<Screen> _screens = new ArrayList<Screen>(2);
	private final Resources _resources = new Resources();
	private final Atoms _atoms = new Atoms();
	private final ByteOrder _imageByteOrder = ByteOrder.MSB;		// XFree was LSB
	private final ByteOrder _bitmapBitOrder = ByteOrder.MSB;		// XFree was LSB
	private final int _bitmapScanLineUnit = 8;  // Xfree is 32 
	private final int _bitmapScanLinePad = 8;   // Xfree is 32 

	private final boolean _handlesBigRequests = true;
	private final int _maximumRequestLength = 32000;

	private Focus _focus = null;

	// Used for generating server side resources such as the Root Window, ColorMap, etc.
	private final int _endServerResourceId;
	private int _serverResourceId;
	private final EventFactories _eventFactories;
	private final FontFactory _fontFactory;
	private final Pointer _pointer = new Pointer();
	private final ScreenSaver _screenSaver = new ScreenSaver();

	
	/**
	 * A lock used to protect the server from concurrent updates
	 */
	private final Lock _syncLock = new ReentrantLock();

	public interface Listener {
		public void windowCreated(final Window window); // TODO delete ?
		public void pixmapCreated(final Pixmap pixmap);
	}

	/**
	 * Empty implementation of the listener so that we don't have null checks 
	 * throughout the code. 
	 */
	private static final class NullListener implements Listener {
		@Override
		public void windowCreated(final Window window) {}

		@Override
		public void pixmapCreated(final Pixmap pixmap) {}
	}

	private static final Listener NULL_LISTENER = new NullListener();

	private Listener _listener = NULL_LISTENER;

	public void setListener(final Listener listener) {
		_listener = listener;
	}

	private Font _fixedFont = null;

	public Font getFixedFont() throws IOException { 
		if(_fixedFont == null) {
			final FontMatch fontMatch = getFontFactory().getFirstMatchingFont("fixed");
			if (fontMatch != null) {
				final FontDetail fontDetail = getFontFactory().open(fontMatch);
				_fixedFont = new Font(allocateResourceId(), fontDetail, getFontFactory());
				openFont(_fixedFont);
			}
		}
		return _fixedFont;
	}

	public void openFont(final Font font) {
		getResources().add(font);
	}

	public void pixmapCreated(final Pixmap pixmap) {
		getResources().add(pixmap);
		_listener.pixmapCreated(pixmap);
	}

	public void windowCreated(final Window window) {
		getResources().add(window);
		_listener.windowCreated(window);
	}

	public void closeFont(final Font font) {
		getResources().remove(font.getId());
		font.free();
	}

	/**
	 * Lock the server for read/update
	 */
	public void lock() {
		_syncLock.lock();
	}

	/**
	 * Unlock the server after read/update
	 */
	public void unlock() {
		_syncLock.unlock();
	}

	public int getTimestamp() {
		return (int)(System.currentTimeMillis() & 0xffffffff);
	}

	/**
	 * A Lock used to grab the server during client requests 
	 */
	private final Lock _requestLock = new ReentrantLock();

	/**
	 * Lock the server for a request.
	 * If the server has been grabbed requests will queue up on the request lock
	 */
	public void lockForRequest() {
		_requestLock.lock();
		_syncLock.lock();
	}

	/**
	 * Unlock the server after a request
	 */
	public void unlockForRequest() {
		_syncLock.unlock();
		_requestLock.unlock();
	}

	/**
	 * The client that currently holds a server grab
	 */
	private Client _grab = null;

	/**
	 * Grab the server.
	 * This prevents further requests for any other client to be processed.
	 * 
	 * @param client The client grabbing the server
	 */
	public void grab(final Client client) {
		_requestLock.lock();
		_grab = client;
	}

	/**
	 * Un-grab the server.			@Override
			public void pixmapCreated(final Pixmap pixmap) {
				pixmap.setListener(new XawtPixmap(pixmap));
			}
	 * Allows other clients to process requests.
	 * 
	 * @param client The client grabbing the server
	 */
	public void ungrab(final Client client) {
		if(_grab == client) {
			_grab = null;
			_requestLock.unlock();
		}
	}

	public Server(final EventFactories eventFactories, final Keyboard keyboard, final FontFactory fontFactory) {		
		// Create the server as a client with ID of 0
		super(	0, 
				new PostBox() {
			@Override
			public void send(final Event event, final Window window) {
				// Do nothing. No messages should be sent to the server anyhow.
			}
		}, 
		null // There is not host for the server client.
				);

		_eventFactories = eventFactories;
		_fontFactory = fontFactory;
		_keyboard = keyboard; 

		_keyboard.setListener(new Keyboard.Listener() {
			@Override
			public void mappingNotify(final int firstKeyCode, final int count) {
				_clients.send(_eventFactories.getMappingNotifyFactory().create(MappingNotifyFactory.Request.Keyboard, firstKeyCode, count));
			}

			@Override
			public void modiferNotify() {
				_clients.send(_eventFactories.getMappingNotifyFactory().create(MappingNotifyFactory.Request.Modifier, 0, 0));
			}
		});

		_pointer.setListener(new Pointer.Listener() {
			@Override
			public void mappingNotify(final int count) {
				_clients.send(_eventFactories.getMappingNotifyFactory().create(MappingNotifyFactory.Request.Pointer, 0, count));
			}
		});

		_serverResourceId = (getClientId() << Resource.CLIENTOFFSET) | Resource.SERVER_BIT;
		_endServerResourceId = (_serverResourceId | Resource.RESOURCE_ID_MASK)+1;

		// TODO not sure we should do this!
		// TODO How accessible should the server client be?
		//_clients.add(this);
	}
	
	// TODO these need to be cleared down when windows are destroyed.
	private final Map<String, Selection> _selections = new TreeMap<String, Selection>();

	public Window getSelectionOwner(final String name) {
		final Selection selection = _selections.get(name);
		if(selection == null) return null;
		return selection.getWindow();
	}
	
	public void setSelectionOwner(final Window window, final String name, final int timestamp) {
		final Selection selection = _selections.get(name);
		if(selection != null) {
			if(timestamp >= selection.getTimestamp()) {
				if(window == null) {
					_selections.remove(name);
				}
				else {
					selection.update(window, timestamp);
				}
			}
			else {
				final Selection newSelection = new Selection(window, timestamp);
				_selections.put(name, newSelection);
			}
		}
	}
	
	public AccessControls getAccessControls() {
		return _accessControl;
	}

	public Pointer getPointer() {
		return _pointer;
	}

	public ScreenSaver getScreenSaver() {
		return _screenSaver;
	}

	public Extensions getExtensions() {
		return _extensions;
	}

	public EventFactories getEventFactories() {
		return _eventFactories;
	}

	public FontFactory getFontFactory() {
		return _fontFactory;
	}

	private int allocateResourceId(){
		final int id =_serverResourceId++;
		if (id !=_endServerResourceId){
			return id;
		}
		throw new RuntimeException("Error allocating fake ID");
	}	

	public interface ResourceFactory<T> {
		public T create(final int resourceId);
	}

	public ColorMap createColorMap(final ResourceFactory<ColorMap> factory) {
		final int resourceId = allocateResourceId();
		final ColorMap colorMap = factory.create(resourceId);
		_resources.add(colorMap);
		return colorMap;
	}

	public Visual createVisual(final ResourceFactory<Visual> factory) {
		final int resourceId = allocateResourceId();
		final Visual visual = factory.create(resourceId);
		_resources.add(visual);
		return visual;
	}
	
	public Pixmap createPixmap(final ResourceFactory<Pixmap> factory) {
		final int resourceId = allocateResourceId();
		final Pixmap pixmap = factory.create(resourceId);
		_resources.add(pixmap);
		return pixmap;
	}

	public Screen addScreen(final ResourceFactory<Screen> factory) {
		final int resourceId = allocateResourceId();
		final Screen screen = factory.create(resourceId);
		// TODO not very graceful
		if(_screens.size() == 0) {
			_focus = new Focus(screen, Focus.RevertTo.None);
			_pointer.set(screen, screen.getWidthPixels() >> 1, screen.getHeightPixels() >> 1);
		}

		_screens.add(screen);
		_resources.add(screen);

		return screen;
	}

	public Client allocateClient(final PostBox postBox, final Host host) {
		return _clients.allocate(postBox, host);
	}

	public void freeClient(final Client client) {
		final int clientId = client.getClientId();
		if(clientId == 0) {
			throw new RuntimeException("Don't try to free the server silly");
		}
		_clients.free(client);
		_resources.free(client);
		ungrab(client);
		if(_pointer.getPointerGrab() != null && _pointer.getPointerGrab().getClient() == client) {
			releasePointerGrab();
		}
		if(_keyboard.getKeyboardGrab() != null && _keyboard.getKeyboardGrab().getClient() == client) {
			releaseKeyboardGrab();
		}
	}

	public byte[] getVendor() {
		return VENDOR;
	}

	public static Format[] getFormats() {
		return FORMATS;
	}

	public List<Screen> getScreens() {
		return _screens;
	}

	public Keyboard getKeyboard() {
		return _keyboard;
	}

	public Resources getResources() {
		return _resources;
	}

	public Atoms getAtoms() {
		return _atoms;
	}

	public Focus getFocus() {
		return _focus;
	}

	public ByteOrder getImageByteOrder() {
		return _imageByteOrder;
	}

	public ByteOrder getBitmapBitOrder() {
		return _bitmapBitOrder;
	}
	public int getBitmapScanLineUnit() {
		return _bitmapScanLineUnit;
	}

	public int getBitmapScanLinePad() {
		return _bitmapScanLinePad;
	}

	public boolean getHandlesBigRequests() {
		return _handlesBigRequests;
	}

	public int getMaximumRequestLength() {
		return _maximumRequestLength;
	}


	// TODO I need to be called
	public void free() {
		for(final Iterator<Client> i = _clients.iterator(); i.hasNext(); ) {
			final Client client = i.next();
			freeClient(client);
		}

		// TODO free all resources
	}

	/**
	 * Get a combination of modifier keys and pointer buttons for events
	 * @return
	 */
	public int getKeyButtonMask() {
		return _keyboard.getModifierMask() | _pointer.getButtonMask();
	}

	//
	// TODO should the following be part of an input device adapter?_listener
	//
	

	private abstract class PointerInputEvent implements InputEvent {
		private final int _when;
		private final int _screenIndex;
		private final int _x;
		private final int _y;
		
		public PointerInputEvent(
				final int screenIndex, 
				final int x, 
				final int y, 
				final int when) {
			
			_screenIndex = screenIndex;
			_x = x;
			_y = y;
			_when = when;
		}
		
		@Override
		public long getWhen() {
			return _when;
		}
		
		private Window _child = null;
		
		@Override
		public void deliver() {
			_pointer.set(_screens.get(_screenIndex), _x, _y);

			final PointerGrab grab = _pointer.getPointerGrab();
			
			_child = _pointer.childWindowAt();

			deliver(grab, _child, _x, _y);
		}
		
		protected abstract void deliver(
				final PointerGrab grab,
				final Window child,
				final int x,
				final int y);
		
		protected final void send(final Event event, final int eventMask) {
			final PointerGrab grab = _pointer.getPointerGrab();
			if(grab == null) {
				_child.deliver(event, eventMask);
			}
			else {
				
				boolean delivered = false;
				if(grab.isOwnerEvents()) {
					delivered =  _child.deliver(event, eventMask, grab.getClient());
				}
				if(!delivered) {
					delivered = grab.getGrabWindow().deliver(event, eventMask, grab.getClient());
				}
				if(delivered && _ptrInputState.equals(InputQueueState.Single)) _ptrInputState = InputQueueState.Frozen;
				
				if(grab.getExitWhenAllReleased()) {
					if(_pointer.getButtonMask() == 0) {
						releasePointerGrab();
					}
				}
			}
		}
	}
	
	public void pointerMoved(
			final int screenIndex, 
			final int x, 
			final int y, 
			final int when) {
		
		enqueuePtr(new PointerInputEvent(screenIndex, x, y, when) {
			
			@Override
			protected void deliver(
						final PointerGrab grab,
						final Window child,
						final int x,
						final int y){
				
				final Event event = _eventFactories.getMotionFactory().create(
						grab, 
						_pointer, 
						child,
						getKeyButtonMask(), 
						when);
				
				send(event, Event.MotionMask);
			}
		});
	}
	
	/**
	 * Called by implementation to deliver a pointer button press to the server
	 * 
	 * @param buttonNumber code representing physical button in the range 1-5
	 * @param when time in milliseconds
	 */
	public void buttonPressed(
			final int screenIndex, 
			final int x, 
			final int y, 
			final int buttonNumber, 
			final int when) {

		if(buttonNumber < 1 || buttonNumber > 5) {
			final String message = "Button number " + buttonNumber + " out of range";
			LOGGER.log(Level.SEVERE, message);
			throw new RuntimeException(message);
		}

		enqueuePtr(new PointerInputEvent(screenIndex, x, y, when) {
			
			@Override
			protected void deliver(
						PointerGrab grab,
						final Window child,
						final int x,
						final int y){
				
				// Update the pressed buttons
				_pointer.buttonPressed(buttonNumber-1);

				if(grab == null) {
					// There is no current grab on the pointer so consider activating a passive grab...
					final ButtonGrab buttonGrab = _pointer.findButtonGrab(buttonNumber, _keyboard.getModifierMask());

					if(buttonGrab != null) {
						// Remove the passive grab...
						buttonGrab.getGrabWindow().removeButtonGrab(buttonGrab);

						// Activate the button grab...
						final PointerGrab pointerGrab = buttonGrab.getPointerGrab(getTimestamp());

						// Set the grab on the server
						setPointerGrab(pointerGrab);

						grab = pointerGrab;
					}
				}

				// There are no grabs so create an active grab!
				if(grab == null) {
					// Start an active grab. See X Window System page 253.

					final ClientWindowAssociation clientWindowAssociation = child.getDeliverToAssociation(Event.ButtonPressMask);

					// Find a client interested in the event
					if(clientWindowAssociation != null) {
						final Client client = clientWindowAssociation.getClient();
						final Window eventWindow = clientWindowAssociation.getWindow();
						final int eventMask = clientWindowAssociation.getEventMask();

						final PointerGrab buttonDownPointerGrab = new PointerGrab(
								client,
								(eventMask & Event.OwnerGrabButtonMask) != 0,
								eventWindow,
								eventMask,
								false,
								false,
								null,
								null,
								getTimestamp());

						buttonDownPointerGrab.setExitWhenAllReleased();

						// Set the grab on the server
						_pointer.setPointerGrab(buttonDownPointerGrab);

						// TODO set the cursor
						dequeueAll();

						grab = buttonDownPointerGrab;
					}
				}

				if(grab != null) {

					// All buttons are delivered in the context of a grab
					final Event event = _eventFactories.getButtonPressFactory().create(
							buttonNumber, 
							grab, 
							_pointer, 
							child,
							getKeyButtonMask(), 
							when);
					
					send(event, Event.ButtonPressMask);
				}
			}
		});
	}

	/**
	 * Called by implementation to deliver a key release to the server
	 * 
	 * @param buttonNumber code representing physical button in the range 1-5
	 * @param when time in milliseconds
	 */
	public void buttonReleased(final int screenIndex, final int x, final int y, final int buttonNumber, final int when){

		if(buttonNumber < 1 || buttonNumber >5) {
			final String message = "Button number " + buttonNumber + " out of range";
			LOGGER.log(Level.SEVERE, message);
			throw new RuntimeException(message);
		}

		enqueuePtr(new PointerInputEvent(screenIndex, x, y, when) {
			
			@Override
			protected void deliver(
						PointerGrab grab,
						final Window child,
						final int x,
						final int y){
				
				_pointer.buttonReleased(buttonNumber-1);
				
				final Event event = _eventFactories.getButtonReleaseFactory().create(
						buttonNumber, 
						grab, 
						_pointer, 
						child,
						getKeyButtonMask(), 
						when);

				send(event, Event.ButtonPressMask);
			}
		});
	}

	public void setPointerGrab(final PointerGrab pointerGrab) {

		// Set the grab on the pointer
		_pointer.setPointerGrab(pointerGrab);

		// TODO set the cursor
		// HACK
		_ptrInputState =  pointerGrab.isPointerSynchronous() ? InputQueueState.Frozen : InputQueueState.Normal;
		dequeueAll();

		// Freeze/thaw input queues
		//setFreezeState(pointerGrab.isKeyboardSynchronous(), pointerGrab.isPointerSynchronous());
	}

	public void releasePointerGrab() {
		// Set the grab on the pointer
		_pointer.setPointerGrab(null);

		// Thaw input queues
		setFreezeState();

		// TODO  It also generates EnterNotify and LeaveNotify events.
	}

	private void enqueuePtr(final InputEvent e) {
		try {
			lock();
			_ptrEventQueue.add(e);
			dequeueAll();
		}
		finally {
			unlock();
		}
	}	

	private Window getKeyFocusWindow(final int eventMask) {

		final KeyboardGrab grab = _keyboard.getKeyboardGrab();

		Window focusWindow = null;

		if(grab == null || grab.isOwnerEvents()) {

			switch(_focus.getMode()) {
			case None:
				// Do not deliver an event for a focus of none.
				return null;
			case PointerRoot:
				// Deliver events relative to the pointer root.
				focusWindow = _pointer.getScreen().getRootWindow();
				break;
			default:
				// Deliver events relative to the focus window.
				focusWindow = _focus.getWindow();
				break;
			}
		}

		if(grab != null) {
			if(focusWindow != null) {
				// If we wouldn't have reported the event then deliver it from the grab window
				if(grab.isOwnerEvents() && focusWindow.getDeliverToAssociation(eventMask, grab.getClient()) == null) {
					focusWindow = grab.getGrabWindow();
				}
			}
			if(focusWindow == null) {
				focusWindow = grab.getGrabWindow();
			}
		}

		return focusWindow;
	}
	
	private void deliverKeyEvent(
			final int keycode, 
			final int when,
			final int eventMask,
			final KeyFactory eventFactory) {
		
		final Window focusWindow = getKeyFocusWindow(eventMask);
		final Window child = _pointer.childWindowAt();
		final Window w = focusWindow == null || child.hasAncestor(focusWindow) ? child : focusWindow;

		final Event event = eventFactory.create(
				focusWindow,
				child,
				_pointer, 
				keycode,
				getKeyButtonMask(),
				when);

		final KeyboardGrab grab = _keyboard.getKeyboardGrab();

		if(grab == null) {
			w.deliver(event, eventMask);
		}
		else {
			
			boolean delivered = false;
			if(grab.isOwnerEvents()) {
				delivered =  w.deliver(event, eventMask, grab.getClient());
			}
			if(!delivered) {
				delivered = grab.getGrabWindow().deliver(event, eventMask, grab.getClient());
			}
			if(delivered && _keyInputState.equals(InputQueueState.Single)) _keyInputState = InputQueueState.Frozen;
		}
	}
	
	/**
	 * Called by implementation to deliver a key press to the server
	 * 
	 * @param keycode code representing physical key in the range 0-255
	 * @param when time in milliseconds
	 */
	public void keyPressed(final int keycode, final int when) {

		if(keycode < 0 || keycode > 255) {
			final String message = "Keycode " + keycode + " out of range";
			LOGGER.log(Level.SEVERE, message);
			throw new RuntimeException(message);
		}

		enqueueKey(new InputEvent() {
			@Override
			public long getWhen() {
				return when;
			}

			@Override
			public void deliver() {
				_keyboard.keyPressed(keycode, when);
				deliverKeyEvent(keycode, when, Event.KeyPressMask, _eventFactories.getKeyPressFactory());
			}
		});
	}

	/**
	 * Called by implementation to deliver a key release to the server
	 * 
	 * @param keycode code representing physical key in the range 0-255
	 * @param when time in milliseconds
	 */
	public void keyReleased(final int keycode, final int when){

		if(keycode < 0 || keycode > 255) {
			final String message = "Keycode " + keycode + " out of range";
			LOGGER.log(Level.SEVERE, message);
			throw new RuntimeException(message);
		}

		enqueueKey(new InputEvent() {
			@Override
			public long getWhen() {
				return when;
			}

			@Override
			public void deliver() {
				_keyboard.keyReleased(keycode, when);
				deliverKeyEvent(keycode, when, Event.KeyReleaseMask, _eventFactories.getKeyReleaseFactory());
			}
		});
	}	

	public void setKeyboardGrab(final KeyboardGrab keyboardGrab) {

		// Set the grab on the keyboard
		_keyboard.setKeyboardGrab(keyboardGrab);

		// Freeze/thaw input queues
		setFreezeState(keyboardGrab.isKeyboardSynchronous(), keyboardGrab.isPointerSynchronous());
	}

	public void releaseKeyboardGrab() {
		// Set the grab on the keyboard
		_keyboard.setKeyboardGrab(null);

		// Thaw input queues
		setFreezeState();

		// TODO  It also generates EnterNotify and LeaveNotify events.
	}

	private void enqueueKey(final InputEvent e) {
		try {
			lock();
			_keyEventQueue.add(e);
			dequeueAll();
		}
		finally {
			unlock();
		}
	}

	// TODO worry about these getting too large
	private Queue<InputEvent> _keyEventQueue = new ArrayDeque<InputEvent>(100);
	private Queue<InputEvent> _ptrEventQueue = new ArrayDeque<InputEvent>(100);
	
	enum InputQueueState {
		Normal, 
		Frozen,
		Single
	}
	
	private InputQueueState _keyInputState = InputQueueState.Normal;
	private InputQueueState _ptrInputState = InputQueueState.Normal;

	/**
	 * Dequeue events in the correct order
	 */
	private boolean dequeue() {
		final boolean haveKeyEvents = !_keyEventQueue.isEmpty() && !_keyInputState.equals(InputQueueState.Frozen);
		final boolean havePtrEvents = !_ptrEventQueue.isEmpty() && !_ptrInputState.equals(InputQueueState.Frozen);
		if(haveKeyEvents && !havePtrEvents) {
			final InputEvent keyEvent = _keyEventQueue.remove();
			keyEvent.deliver();
		}
		else if(!haveKeyEvents && havePtrEvents) {
			final InputEvent ptrEvent = _ptrEventQueue.remove();
			ptrEvent.deliver();
		}
		else if(haveKeyEvents && havePtrEvents) {
			final InputEvent keyEvent = _keyEventQueue.remove();
			final InputEvent ptrEvent = _ptrEventQueue.remove();
			if(keyEvent.getWhen() - ptrEvent.getWhen() > 0) {
				keyEvent.deliver();
				ptrEvent.deliver();
			}
			else {
				ptrEvent.deliver();
				keyEvent.deliver();
			}
		}
		else {
			return false;
		}
		return true;
	}

	private void dequeueAll() {
		while(dequeue());
	}

	private void setFreezeState() {
		final Grab grab;
		final Grab pointerGrab = _pointer.getPointerGrab();
		final Grab keyboardGrab = _keyboard.getKeyboardGrab();
		if(pointerGrab != null && keyboardGrab != null) {
			if(pointerGrab.getTimestamp() > keyboardGrab.getTimestamp()) {
				grab = pointerGrab;
			}
			else {
				grab = keyboardGrab;
			}
		}
		else if (pointerGrab != null) {
			grab = pointerGrab;
		}
		else if (keyboardGrab != null) {
			grab = keyboardGrab;
		}
		else {
			grab = null;
		}
		if(grab == null) {
			setFreezeState(false, false);
		}
		else {
			setFreezeState(grab.isKeyboardSynchronous(), grab.isPointerSynchronous());
		}
	}
	
	private void setFreezeState(final boolean keyboard, final boolean pointer) {
		_keyInputState = keyboard ? InputQueueState.Frozen : InputQueueState.Normal;
		_ptrInputState = pointer ? InputQueueState.Frozen : InputQueueState.Normal;
		dequeueAll();
	}
	
	public void allowKeyboardEvents(final Client client, final boolean sync, final int time) {
		final KeyboardGrab grab = getKeyboard().getKeyboardGrab();
		if(grab == null) return;
		if(grab.getClient() != client) return;
		if(time < grab.getTimestamp()) return;
		if(sync && _keyInputState.equals(InputQueueState.Frozen)) {
			_keyInputState = InputQueueState.Single;
		}
		else {
			_keyInputState = InputQueueState.Normal;
		}
		dequeueAll();
	}	
	
	public void allowPointerEvents(final Client client, final boolean sync, final int time) {
		final PointerGrab grab = getPointer().getPointerGrab();
		if(grab == null) return;
		if(grab.getClient() != client) return;
		if(time < grab.getTimestamp()) return;
		if(sync && _ptrInputState.equals(InputQueueState.Frozen)) {
			_ptrInputState = InputQueueState.Single;
		}
		else {
			_ptrInputState = InputQueueState.Normal;
		}
		dequeueAll();		
	}

}
