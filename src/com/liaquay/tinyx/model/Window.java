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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liaquay.tinyx.model.ButtonGrab.Trigger;
import com.liaquay.tinyx.model.Image.ImageType;
import com.liaquay.tinyx.model.eventfactories.EventFactories;

public class Window extends Drawable {

	private final Window _parent;
	private final List<Window> _children = new ArrayList<Window>();

	public interface Listener extends Drawable.Listener {
		public void childCreated(Window child);
		public void setCursor(Cursor cursor);
		public void setBackgroundPixmap(final Pixmap pixmap);
		public int getPixel(int x, int y);
		public void clearArea(int x, int y, int width, int height);
		public void drawBorder();
		public void setBorderPixmap(Pixmap pixmap);
	}

	/**
	 * Empty implementation of the listener so that we don't have null checks 
	 * throughout the code. 
	 */
	private static final class NullListener extends Drawable.NullListener implements Listener {

		@Override
		public void copyArea(Drawable d, GraphicsContext graphicsContext, int srcX, int srcY, int width, int height, int dstX, int dstY) {}
		@Override
		public void putImage(GraphicsContext graphicsContext, ImageType imageType, byte[] data, int width, int height, int destinationX, int destinationY, int leftPad, int depth) {}
		@Override
		public void childCreated(Window child) {}
		@Override
		public void setCursor(Cursor cursor) {}
		@Override
		public int getPixel(int x, int y) {return 0;}
		@Override
		public void clearArea(int x, int y, int width, int height) {}
		@Override
		public void setBackgroundPixmap(Pixmap pixmap) {}
		@Override
		public void drawBorder() {}
		@Override
		public void setBorderPixmap(Pixmap pixmap) {}
	}

	private static final Listener NULL_LISTENER = new NullListener();

	private Listener _listener = NULL_LISTENER;

	public void setListener(final Listener listener) {
		_listener = listener;
	}
	
	public Listener getWindowListener() {
		return _listener;
	}

	public enum MappedState {
		IsUnmapped,
		IsUnviewable,
		IsViewable;

		public static MappedState getFromIndex(final int index) {
			final MappedState[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}	
	}

	public enum BackingStoreHint {
		BackingStoreNever,
		BackingStoreWhenMapped,
		BackingStoreAlways;

		public static BackingStoreHint getFromIndex(final int index) {
			final BackingStoreHint[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}
	}

	private BackingStoreHint _backingStoreHint = BackingStoreHint.BackingStoreNever; // TODO Check default value

	public enum WinGravity {
		/*
		 * The win_gravity attribute controls the repositioning of subwindows
		 * when a parent window is resized. The attribute is set on the
		 * children. Normally, each child has a fixed position measured from the
		 * origin of the parent window. Window gravity can be used to tell the
		 * server to unmap the child or to move the child an amount depending on
		 * the change in size of the parent. The constants used to set
		 * win_gravity are similar to those for bit gravity, but their effect is
		 * quite different.
		 * 
		 * NorthGravity specifies that the child window should be moved
		 * horizontally by an amount one-half as great as the amount the window
		 * was resized in the horizontal direction. The child is not moved
		 * vertically. That means that if the window was originally centered
		 * along the top edge of the window, it will also be centered along the
		 * top edge of the window after resizing. If it was not originally
		 * centered, its relative distance from the center may be accentuated or
		 * reduced depending on whether the parent is resized larger or smaller.
		 * 
		 * Window gravity is only useful for children placed against or very
		 * near the outside edges of the parent or directly in its center.
		 * Furthermore, the child must be centered along one of the outside
		 * edges or in a corner. Figure 4-2 shows the nine child positions where
		 * window gravity can be useful and the setting to be used for each
		 * position.
		 */
		Unmap {
			@Override
			public int getOriginX(int x, int width) { return x; }
			@Override
			public int getOriginY(int y, int height) { return y; }
		},
		NorthWest {
			@Override
			public int getOriginX(int x, int width) { return x; }
			@Override
			public int getOriginY(int y, int height) { return y; }
		},
		North {
			@Override
			public int getOriginX(int x, int width) { return x + (width>>1); }
			@Override
			public int getOriginY(int y, int height) { return y; }
		},
		NorthEast {
			@Override
			public int getOriginX(int x, int width) { return x + width; }
			@Override
			public int getOriginY(int y, int height) { return y; }
		},
		West {
			@Override
			public int getOriginX(int x, int width) { return x; }
			@Override
			public int getOriginY(int y, int height) { return y + (height>>1); }
		},
		Center {
			@Override
			public int getOriginX(int x, int width) { return x + (width>>1); }
			@Override
			public int getOriginY(int y, int height) { return y + (height>>1); }
		},
		East {
			@Override
			public int getOriginX(int x, int width) { return x + width; }
			@Override
			public int getOriginY(int y, int height) { return y + (height>>1); }
		},
		SouthWest {
			@Override
			public int getOriginX(int x, int width) { return x; }
			@Override
			public int getOriginY(int y, int height) { return y + height; }
		},
		South {
			@Override
			public int getOriginX(int x, int width) { return x + (width>>1); }
			@Override
			public int getOriginY(int y, int height) { return y + height; }
		},
		SouthEast {
			@Override
			public int getOriginX(int x, int width) { return x + width; }
			@Override
			public int getOriginY(int y, int height) { return y + height; }
		},
		Static { // TODO Not sure what to do here!
			@Override
			public int getOriginX(int x, int width) { return x; }
			@Override
			public int getOriginY(int y, int height) { return y; }
		};

		public static WinGravity getFromIndex(final int index) {
			final WinGravity[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}
		
		public abstract int getOriginX(final int x, final int width);
		public abstract int getOriginY(final int y, final int height);
	}
	
	public enum BitGravity {
		/*
		 * When an unobscured window is moved, its contents are moved with it,
		 * since none of the pixel values need to be changed. But when a window
		 * is enlarged or shrunk, the server has no idea where in the resulting
		 * window the old contents should be placed, so it normally throws them
		 * out. The bit_gravity attribute tells the server where to put the
		 * existing bits in the larger or smaller window. By instructing the
		 * server where to place the old contents, bit gravity allows some
		 * clients (not all can take advantage of it) to avoid redrawing parts
		 * of their windows.
		 * 
		 * Bit gravity is never necessary in programs. It does not affect the
		 * appearance or functionality of the client. It is used to improve
		 * performance in certain cases. Some X servers may not implement bit
		 * gravity and may throw out the window contents on resizing regardless
		 * of the setting of this attribute. This response is the default for
		 * all servers. That is, the default bit gravity is ForgetGravity, which
		 * means that the contents of a window are always lost when the window
		 * is resized, even if they are maintained in backing store or because
		 * of a save_under (to be described in Sections 4.3.5 and 4.3.6).
		 * 
		 * The window is tiled with its background in the areas that are not
		 * preserved by the bit gravity, unless no background is defined, in
		 * which case the existing screen is not altered.
		 */
		Forget,
		NorthWest,
		North,
		NorthEast,
		West,
		Center,
		East,
		SouthWest,
		South,
		SouthEast,
		Static;

		public static BitGravity getFromIndex(final int index) {
			final BitGravity[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}
	}
	private BitGravity _bitGravity = BitGravity.Forget;
	private WinGravity _winGravity  = WinGravity.NorthWest;

	public enum WindowClass {
		CopyFromParent,
		InputOutput,
		InputOnly;

		public static WindowClass getFromIndex(final int index) {
			final WindowClass[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}
	}

	private final int _resourceId;
	private final Visual _visual;
	private final Properties _properties = new Properties();
	private final List<ClientWindowAssociation> _clientWindowAssociations = new ArrayList<ClientWindowAssociation>(2);

	private final int _depth;				/* depth of window */
	private int _x, _y;						/* relative location of window */
	private int _absX, _absY;				/* absolute location of window */

	private Cursor _cursor = null;			/* The cursor to use when the mouse is within this window */
	private int _clipX, _clipY;				/* clip location of window */
	private int _clipW, _clipH;				/* clip size of window */

	private int _widthPixels, _heightPixels;	/* width and height of window in pixels */
	private int _borderWidth;		/* border width of window */
	private WindowClass _windowClass;


	private boolean _mapped = false;
	private boolean _viewable = false;

	private int _backingPlanes = -1;			// Default: All ones
	private int _backingPixel = 0;				// Default: Zero
	private boolean _saveUnder = false;			// Default: false
	private boolean _overrideRedirect = false;	// Default: false
	private ColorMap _colorMap = null;			// Default: Copy-from-parent
	private int _doNotPropagateMask = 0; 		// Default: Empty set
	private final EventFactories _eventFactories;
	private final Map<ButtonGrab.Trigger, ButtonGrab> _buttonGrabs = new HashMap<ButtonGrab.Trigger, ButtonGrab>(4);
	
	public enum StackMode {
		Above,
		Below,
		TopIf,
		BottomIf,
		Opposite;

		public static StackMode getFromIndex(final int index) {
			final StackMode[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}
	}

	public Window(
			final int resourceId, 
			final Window parent, 
			final Visual visual,
			final int depth,
			final int width,
			final int height,
			final int x,
			final int y,
			final int borderWidth,
			final WindowClass windowClass,
			final EventFactories eventFactories,
			final ColorMap colorMap) {

		_parent = parent;
		_resourceId = resourceId;
		_visual = visual;
		_depth = depth;
		_widthPixels = width;
		_heightPixels = height;
		_x = x;
		_y = y;
		_borderWidth = borderWidth;
		_windowClass = windowClass;
		_eventFactories = eventFactories;
		_colorMap = colorMap;
		if(_parent != null) {
			_parent.addChild(this);
		}

		updateLocation();
		if(_parent != null){
			_parent._listener.childCreated(this);
		}
	}

	public ButtonGrab getButtonGrab(final ButtonGrab.Trigger trigger) {
		return _buttonGrabs.get(trigger);
	}

	public void addButtonGrab(final ButtonGrab buttonGrab) {
		_buttonGrabs.put(buttonGrab.getTrigger(), buttonGrab);
	}

	/**
	 * Update the absolute position of the window and its clip rectangle.
	 */
	private void updateLocation() {
		if(_parent == null) {
			_absX = _x + _borderWidth;
			_absY = _y + _borderWidth;
			_clipX = _x;
			_clipY = _y;
			_clipW = _widthPixels + _borderWidth + _borderWidth;
			_clipH = _heightPixels + _borderWidth + _borderWidth;
		}
		else {
			// Calculate inner coordinates of the parent window
			final int pix0 = _parent._absX;
			final int piy0 = _parent._absY;
			final int pix1 = pix0 + _parent._widthPixels;
			final int piy1 = piy0 + _parent._heightPixels;

			// Calculate the parent clip coordinates
			final int clx0 = _parent._clipX;
			final int cly0 = _parent._clipY;
			final int clx1 = clx0 + _parent._clipW;
			final int cly1 = cly0 + _parent._clipH;

			// Now calculate the clipped parent inner coordinates
			final int cpx0 = pix0 > clx0 ? pix0 : clx0;
			final int cpy0 = piy0 > cly0 ? piy0 : cly0;
			final int cpx1 = pix1 < clx1 ? pix1 : clx1;
			final int cpy1 = piy1 < cly1 ? piy1 : cly1;

			// Calculate the outer coordinates of the child window
			final int cox0 = pix0 + _x;
			final int coy0 = piy0 + _y;
			final int cox1 = cox0 + _widthPixels + _borderWidth + _borderWidth;
			final int coy1 = coy0 + _heightPixels + _borderWidth + _borderWidth;

			_absX = cox0 + _borderWidth;
			_absY = coy0 + _borderWidth;

			// Clip the outer child to the inner clipped parent
			_clipX = cox0 > cpx0 ? cox0 : cpx0;
			_clipY = coy0 > cpy0 ? coy0 : cpy0;

			// Find the clipped right and lower points
			final int x3 = cox1 < cpx1 ? cox1 : cpx1;
			final int y3 = coy1 < cpy1 ? coy1 : cpy1;

			_clipW = _clipX < x3 ? x3 - _clipX : 0;
			_clipH = _clipY < y3 ? y3 - _clipY : 0;
		}

		for(int i = 0; i < _children.size() ; ++i) {
			_children.get(i).updateLocation();
		}
	}

	private void updateVisibility() {

		final boolean newVisibility = isMappedToRoot() && nonZeroClippedArea() && !isInputOnly();

		if(!_viewable && newVisibility) {
			_viewable = newVisibility;
			redraw();
		}

		_viewable = newVisibility;

		for(int i = 0; i < _children.size() ; ++i) {
			_children.get(i).updateVisibility();
		}
	}

	public boolean containsPixel(final int absX, final int absY) {
		return absX >= _clipX && absX < (_clipX + _clipW) &&
				absY >= _clipY && absY < (_clipY + _clipH);  
	}

	public Window childWindowAt(final int absX, final int absY) {
		for(int i = _children.size()-1; i >= 0 ; --i) {
			final Window child = _children.get(i);
			if(child.isMapped()) {
				final Window childContains = child.windowAt(absX, absY);
				if(childContains != null) {
					return childContains;
				}
			}
		}
		return null;
	}

	public Window windowAt(final int absX, final int absY) {
		if(containsPixel(absX, absY)) {
			final Window childWindow = childWindowAt(absX, absY);
			return childWindow == null ? this : childWindow;
		}
		else {
			return null;
		}
	}

	public Window getParent() {
		return _parent;
	}

	public void removeChild(final Window window) {
		_children.remove(window);
	}

	public Window getChild(final int i) {
		return _children.get(i);
	}

	private void addChild(final Window child) {
		_children.add(child);
	}

	public boolean hasAncestor(final Window ancestor) {
		if(this == ancestor) return true;
		if(_parent != null) return _parent.hasAncestor(ancestor);
		return false;
	}

	@Override
	public int getId() {
		return _resourceId;
	}

	public int getWidthPixels() {
		return _widthPixels;
	}	

	public int getHeightPixels() {
		return _heightPixels;
	}

	public void free() {
		unmap();
		
		for(int i = _clientWindowAssociations.size()-1; i>=0; --i) {
			_clientWindowAssociations.get(i).free();
		}

		if(getParent() != null) {
			getParent().removeChild(this);
		}
	}

	public Properties getProperties() {
		return _properties;
	}

	@Override
	public Screen getScreen() {
		return getParent().getScreen();
	}

	public Window getRootWindow() {
		return getScreen();
	}

	public int getChildCount() {
		return _children.size();
	}

	@Override
	public Visual getVisual() {
		return _visual;
	}

	public int getDepth() {
		return _depth;
	}

	public boolean isMapped() {
		return _mapped;
	}

	public void map() {
		if(!_mapped ) {
			_mapped = true;

			if (!_overrideRedirect && getParent() != null && wouldDeliver(Event.SubstructureRedirectMask)) {
				final Event mapRequestEvent = _eventFactories.getMapRequestFactory().create(false, getParent(), this);
				getParent().deliver(mapRequestEvent, Event.SubstructureRedirectMask);
			}

			if(wouldDeliver(Event.StructureNotifyMask)) {
				final Event mapNotifyEvent = _eventFactories.getMapNotifyFactory().create(false, this, this, _overrideRedirect);
				deliver(mapNotifyEvent, Event.StructureNotifyMask);
			}

			if(wouldDeliver(Event.SubstructureNotifyMask)) {
				final Event mapNotifyEvent = _eventFactories.getMapNotifyFactory().create(false, getParent(), this, _overrideRedirect);
				deliver(mapNotifyEvent, Event.SubstructureNotifyMask);
			}

			updateVisibility();
		}
	}
	
	public EventFactories getEventFactories() {
		return _eventFactories;
	}
	
	private void redraw() {
		if(isViewable()){
			_listener.drawBorder();
			redrawContents();
		}
	}
	
	private void redrawContents() {
		if(isViewable()){
			 _listener.clearArea(0, 0, _widthPixels, _heightPixels);
			 
			for(int i = 0; i < _children.size() ; ++i) {
				final Window c = _children.get(i);
				c.redraw();
			}
			sendExposeEvent();
		}
	}

	/**
	 * Determine if all the window up to the root window are mapped,
	 * in which case this window is mapped and displayable.
	 */
	private boolean isMappedToRoot() {
		return _mapped && (_parent == null || _parent.isMappedToRoot());
	}

	public boolean wouldDeliver(final int mask) {
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			if((assoc.getEventMask() & mask) != 0) {
				return true;
			}
		}
		if((_doNotPropagateMask & mask) == 0 && _parent != null) {
			return _parent.wouldDeliver(mask);
		}
		return false;
	}

	public ClientWindowAssociation getDeliverToAssociation(final int mask, final Client client) {
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			if((assoc.getClient() == client) && (assoc.getEventMask() & mask) != 0) {
				return assoc;
			}
		}
		if((_doNotPropagateMask & mask) == 0 && _parent != null) {
			_parent.getDeliverToAssociation(mask, client);
		}
		return null;
	}

	public ClientWindowAssociation getDeliverToAssociation(final int mask) {
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			if((assoc.getEventMask() & mask) != 0) {
				return assoc;
			}
		}
		if((_doNotPropagateMask & mask) == 0 && _parent != null) {
			return _parent.getDeliverToAssociation(mask);
		}
		return null;
	}

	public void deliver(final Event event, final int mask){
		deliver(event, mask, new BitSet(Resource.MAXCLIENTS));
	}

	private void deliver(final Event event, final int mask, final BitSet deliveredToClient){
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			final Client client = assoc.getClient();
			final int clientId = client.getClientId();
			if((assoc.getEventMask() & mask) != 0 && !deliveredToClient.get(clientId)) {
				client.getPostBox().send(event, assoc.getWindow());
				deliveredToClient.set(clientId);
			}
		}
		if((_doNotPropagateMask & mask) == 0 && _parent != null) {
			_parent.deliver(event, mask, deliveredToClient);
		}
	}

	/**
	 * Performs a MapWindow request on all unmapped children of the window,
	 * in top-to-bottom stacking order.
	 */
	public void mapSubwindows() {
		// TODO not sure the order is correct
		for(int i = 0; i < _children.size() ; ++i) {
			_children.get(i).map();
			_children.get(i).mapSubwindows();
		}
	}

	public void unmap() {
		if(_mapped) {
			_mapped = false;
			_viewable = false;
			
			if(_parent != null && _parent.isViewable()) {
				_parent.redrawContents();
			}
		}
	}

	/**
	 * Performs an UnmapWindow request on all mapped children of the window,
	 * in bottom-to-top stacking order
	 */
	public void unmapSubwindows() {
		// TODO not sure the order is correct
		for(int i = _children.size()-1; i >= 0 ; --i) {
			_children.get(i).unmap();
			_children.get(i).mapSubwindows();
		}
	}

	public boolean isInputOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	public BackingStoreHint getBackingStoreHint() {
		return _backingStoreHint;
	}

	public BitGravity getBitGravity() {
		return _bitGravity;
	}

	public WinGravity getWinGravity() { 
		return _winGravity;
	}

	public MappedState getMappedState() {
		if(_mapped) {
			MappedState state = isViewable() ? MappedState.IsViewable : MappedState.IsUnviewable;
			
			Window w = this;
			while (w.getId() != w.getRootWindow().getId()) {
				if (!w.isMapped()) {
					// A window is Unviewable if it is mapped but some ancestor is unmapped (GetWindowAttributes docs)
					state = MappedState.IsUnviewable;
				}
				
				w = w.getParent();
			}
			
			return state;
		}
		else {
			return MappedState.IsUnmapped;
		}
	}

	public int getBackingPlanes() {
		return _backingPlanes;
	}

	public int getBackingPixel() {
		return _backingPixel;
	}

	public boolean getSaveUnder() {
		return _saveUnder;
	}

	// TODO what is this used for? Might need to be efficient/cached.
	public int getAllEventMask() {
		int eventMask = 0;
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			eventMask |= assoc.getEventMask();
		}
		return eventMask;
	}

	public int getClientEventMask(final Client client) {
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			if(assoc.getClient() == client) return assoc.getEventMask();
		}
		return 0;
	}

	public int getDoNotPropagateMask() {
		return _doNotPropagateMask;
	}

	public boolean getOverrideRedirect() {
		return _overrideRedirect;
	}

	@Override
	public ColorMap getColorMap() {
		return _colorMap;
	}

	@Override
	public int getX() {
		return _x;
	}

	@Override
	public int getY() {
		return _y;
	}

	@Override
	public int getWidth() {
		return _widthPixels;
	}

	@Override
	public int getHeight() {
		return _heightPixels;
	}

	public Cursor getCursor() {
		return this._cursor;
	}

	public void setCursor(Cursor cursor) {
		this._cursor = cursor;


		//TODO: Move this into event handling code.. Only here to test the drawing methods
		_listener.setCursor(cursor);
	}

	public int getAbsX() {
		return _absX;
	}

	public int getAbsY() {
		return _absY;
	}

	public int getClipX() {
		return _clipX;
	}

	public int getClipY() {
		return _clipY;
	}

	public int getClipWidth() {
		return _clipW;
	}

	public int getClipHeight() {
		return _clipH;
	}

	@Override
	public int getBorderWidth() {
		return _borderWidth;
	}

	public void setBitGravity(final BitGravity gravity) {
		_bitGravity = gravity;
	}

	public void setWinGravity(WinGravity gravity) {
		_winGravity = gravity;
	}

	public void setColorMap(final ColorMap colorMap) {
		_colorMap = colorMap;
	}

	//
	// Window background handling
	//
	public enum BackgroundMode {
		None,
		Pixel,
		Pixmap,
		Parent
	}
	
	private BackgroundMode _backgroundMode = BackgroundMode.None;
	private int _backgroundPixel = 0;

	public BackgroundMode getBackgroundMode() {
		return _backgroundMode;
	}
	
	public int getBackgroundPixel() {
		return _backgroundPixel;
	}

	public void setBackgroundPixel(final int backGroundPixel) {
		final boolean changed = _backgroundPixel != backGroundPixel;
		_backgroundPixel = backGroundPixel;
		_backgroundMode = BackgroundMode.Pixel;
		
		// Redraw the window
		if(changed) redrawContents();
	}
	
	public void setBackgroundPixmap(final Pixmap pixmap) {
		_backgroundMode = pixmap == null ? BackgroundMode.None : BackgroundMode.Pixmap;
		
		// Redraw the window
		_listener.setBackgroundPixmap(pixmap);
		redrawContents();
	}

	public void setBackgroundParent() {
		_backgroundMode = BackgroundMode.Parent;
		
		// Redraw the window
		redrawContents();
	}
	
	public Window getBackgroundWindow() {
		for(Window w = this; w != null; w = w._parent) {
			if(!w._backgroundMode.equals(BackgroundMode.Parent)) {
				return w;
			}
		}
		return null;
	}
	
	// 
	// Window Border handling
	//
	private BackgroundMode _borderMode = BackgroundMode.None;
	private int _borderPixel = 0;
	private Pixmap _borderPixmap = null;

	public BackgroundMode getBorderMode() {
		return _borderMode;
	}
	
	public Pixmap getBorderPixmap() {
		return _borderPixmap;
	}

	public int getBorderPixel() {
		return _borderPixel;
	}	

	public void setBorderPixel(final int borderPixel) {
		final boolean changed = _borderPixel != borderPixel;
		_borderPixel = borderPixel;
		_borderMode = BackgroundMode.Pixel;
		
		// Redraw the border
		if(changed) {
			redrawBorder();
		}
	}
	
	public void setBorderPixmap(final Pixmap pixmap) {
		_borderMode = pixmap == null ? BackgroundMode.None : BackgroundMode.Pixmap;
		
		// Redraw the window
		_listener.setBorderPixmap(pixmap);
		redrawBorder();
	}

	public void setBorderParent() {
		_borderMode = BackgroundMode.Parent;
		
		// Redraw the window
		redrawBorder();
	}
	
	public Window getBorderWindow() {
		for(Window w = this; w != null; w = w._parent) {
			if(!w._borderMode.equals(BackgroundMode.Parent)) {
				return w;
			}
		}
		return null;
	}

	private void redrawBorder() {
		if(!isViewable()) return;
		_listener.drawBorder();
		for(int i = 0; i < _children.size() ; ++i) {
			final Window c = _children.get(i);
			if(c._borderMode.equals(BackgroundMode.Parent)) {
				c.redrawBorder();
			}
		}
	}
	
	//
	// End Border Handling
	//
	
	public void setBackingPixel(final int backingPixel) {
		_backingPixel = backingPixel;
	}

	public void setBackingPlanes(final int backingPlanes) {
		_backingPlanes = backingPlanes;
	}

	public void setBackingStoreHint(final BackingStoreHint backingStoreHint) {
		_backingStoreHint = backingStoreHint;
	}

	public void setDoNotPropagateMask(final int doNotPropagateMask) {
		_doNotPropagateMask = doNotPropagateMask;
	}

	public void setOverrideDirect(final boolean overrideRedirect) {
		_overrideRedirect = overrideRedirect;
	}

	public void setSaveUnder(final boolean saveUnder) {
		_saveUnder = saveUnder;
	}

	private boolean overlaps(
			final int x,
			final int y,
			final int w,
			final int h) {
		
		final int x1l = _x;
		final int x1r = _x + _widthPixels;
		final int y1t = _y;
		final int y1b = _y + _heightPixels;
		final int x2l = x;
		final int x2r = x + w;
		final int y2t = y;
		final int y2b = y + h;
		
		return x1l < x2r && x1r > x2l &&  y1t < y2b && y1b > y2t;
	}
	
	public void clearArea(
			final int x, 
			final int y, 
			final int width, 
			final int height, 
			final boolean exposures) {
		
		_listener.clearArea(x, y, width, height);
		
		if(exposures) sendExposeEvent();
		
		for(int i = 0; i < _children.size() ; ++i) {
			final Window c = _children.get(i);
			if(c.overlaps(x, y, width, height)) {
				c.redraw();
			}
		}
	}
	
	private void sendExposeEvent() {
		if(wouldDeliver(Event.ExposureMask)) {
			final Event exposeEvent = _eventFactories.getExposureFactory().create(this.getId(), getX(), getY(), getClipWidth(), getClipHeight(), 0);
			deliver(exposeEvent, Event.ExposureMask);
		}		
	}

	public void add(final ClientWindowAssociation assoc) {
		_clientWindowAssociations.add(assoc);
	}

	public void remove(final ClientWindowAssociation assoc) {
		_clientWindowAssociations.remove(assoc);
	} 

	public ClientWindowAssociation getClientWindowAssociations(final Client client) {
		// Linear search but this should be a short list (1 or 2 long).
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			if(assoc.getClient() == client) return assoc;
		}
		return null;
	}

	public final boolean isViewable() {
		return _viewable;
	}

	private final boolean nonZeroClippedArea() {
		return _clipH > 0 && _clipW > 0;
	}

	public ButtonGrab findFirstButtonGrab(final Trigger trigger) {
		ButtonGrab grab = null;
		if(_parent != null) {
			grab = _parent.findFirstButtonGrab(trigger);
		}
		if(grab == null) {
			grab = _buttonGrabs.get(trigger);

			// Check the confine-to window is viewable
			if(grab != null && grab.getConfineToWindow() != null) {
				if(!grab.getConfineToWindow().isViewable()) {
					grab = null;
				}
			}
		}
		return grab;
	}

	public void removeButtonGrab(final ButtonGrab buttonGrab) {
		_buttonGrabs.remove(buttonGrab.getTrigger());
	}	

	public int getChildWindowIndex(final Window child) {
		int childIndex = 0;
		for(; childIndex < _children.size() ; ++childIndex) {
			final Window c =  _children.get(childIndex);
			if(c == child) break;
		}
		if(childIndex < _children.size()) {
			return childIndex;
		}
		else {
			return -1;
		}
	}
	
	private boolean above(final Window siblingWindow) {
		final int childIndex = _parent.getChildWindowIndex(this);
		_parent._children.remove(this);
		final int siblingIndex = _parent.getChildWindowIndex(siblingWindow);
		_parent._children.add(siblingIndex+1, this);
		return childIndex !=  _parent.getChildWindowIndex(this);
	}
	
	private boolean below(final Window siblingWindow) {
		final int childIndex = _parent.getChildWindowIndex(this);
		_parent._children.remove(this);
		final int siblingIndex = _parent.getChildWindowIndex(siblingWindow);
		_parent._children.add(siblingIndex, this);
		return childIndex !=  _parent.getChildWindowIndex(this);
	}
	
	private boolean bottomIf(final Window siblingWindow) {
		if(siblingWindow.overlaps(this)) {
			final int siblingIndex = _parent.getChildWindowIndex(siblingWindow);
			final int childIndex = _parent.getChildWindowIndex(this);
			if(childIndex>siblingIndex) {
				_parent._children.remove(this);
				_parent._children.add(0, this);
				return true;
			}
		}
		return false;
	}
	
	private boolean opposite(final Window siblingWindow) {
		if(siblingWindow.overlaps(this)) {
			final int siblingIndex = _parent.getChildWindowIndex(siblingWindow);
			final int childIndex = _parent.getChildWindowIndex(this);
			if(childIndex<siblingIndex) {
				_parent._children.remove(this);
				_parent._children.add(this);
				return true;
			}
			else {
				_parent._children.remove(this);
				_parent._children.add(0, this);
				return true;
			}
		}
		return false;
	}
	
	private boolean topIf(final Window siblingWindow) {
		if(siblingWindow.overlaps(this)) {
			final int siblingIndex = _parent.getChildWindowIndex(siblingWindow);
			final int childIndex = _parent.getChildWindowIndex(this);
			if(childIndex<siblingIndex) {
				_parent._children.remove(this);
				_parent._children.add(this);
				return true;
			}
		}
		return false;
	}
	
	private boolean above() {
		final int childIndex = _parent.getChildWindowIndex(this);
		if(childIndex == _parent._children.size() -1) return false;
		_parent._children.remove(this);
		_parent._children.add(this);
		return true;
	}

	private boolean below() {
		final int childIndex = _parent.getChildWindowIndex(this);
		if(childIndex == 0) return false;
		_parent._children.remove(this);
		_parent._children.add(0, this);
		return true;
	}	
	private boolean bottomIf() {
		for(int i = 0; i < _parent._children.size() ; ++i) {
			final Window c = _parent._children.get(i);
			if(c == this) break;
			if(c.overlaps(this)) {
				_parent._children.remove(this);
				_parent._children.add(0, this);
				return true;
			}
		}
		return false;
	}
	
	private boolean opposite() {
		boolean underThis = false;
		for(int i = _parent._children.size()-1; i >= 0 ; --i) {
			final Window c = _parent._children.get(i);
			if(c == this) { 
				underThis = false;
			}
			else {
				if(c.overlaps(this)) {
					if(underThis) {
						// If the window occludes any sibling, then the window is placed at the bottom of the stack
						_parent._children.remove(this);
						_parent._children.add(0, this);
						return true;
					}
					else {
						// If any sibling occludes window, then the window is placed at the top of the stack. 
						_parent._children.remove(this);
						_parent._children.add(this);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean topIf() {
		final int childIndex = _parent.getChildWindowIndex(this);
		for(int i = childIndex+1; i < _parent._children.size() ; ++i) {
			final Window c = _parent._children.get(i);
			if(c.overlaps(this)) {
				_parent._children.remove(this);
				_parent._children.add(this);
				return true;
			}
		}
		return false;
	}
	
	public void configure(
			final int x, 
			final int y, 
			final int width, 
			final int height, 
			final int borderWidth, 
			final StackMode stackMode,
			final Window siblingWindow) {
		
		boolean changed = 
				_x != x ||
				_y != y ||
				_widthPixels != width ||
				_heightPixels != height ||
				_borderWidth != borderWidth;
		
		_x = x;
		_y = y;
		_widthPixels = width;
		_heightPixels = height;
		_borderWidth = borderWidth;

		if(stackMode != null && _parent != null) {

			if(siblingWindow != null && _parent.getChildWindowIndex(siblingWindow) > 0) {

				switch(stackMode) {
				case Above: {
					changed |= above(siblingWindow);
					break;
				}
				case Below: {
					changed |= below(siblingWindow);
					break;
				}
				case BottomIf:{
					changed |= bottomIf(siblingWindow);
					break;
				}
				case Opposite:{
					changed |= opposite(siblingWindow);
					break;
				}
				case TopIf:{
					changed |= topIf(siblingWindow);
					break;
				}
				}
			}
			else {
				switch(stackMode) {
				case Above: {
					changed |= above();
					break;
				}
				case Below: {
					changed |= below();
					break;
				}
				case BottomIf:{
					changed |= bottomIf();
					break;
				}
				case Opposite:{
					changed |= opposite();
					break;
				}
				case TopIf:{
					changed |= topIf();
					break;
				}
				}
			}
		}

		if(changed) {
			updateLocation();

			// TODO Events etc.
			_parent.redraw(); // TODO somewhat over the top!
		}
	}

	private boolean overlaps(final Window window) {

		final int doubleBorder = _borderWidth + _borderWidth;
		final int windowDoubleBorder = window._borderWidth + window._borderWidth;
		final int x1l = _absX;
		final int x1r = _absX + _widthPixels + doubleBorder;
		final int y1t = _absY;
		final int y1b = _absY + _heightPixels + doubleBorder;
		final int x2l = window._absX;
		final int x2r = window._absX + window._widthPixels + windowDoubleBorder;
		final int y2t = window._absY;
		final int y2b = window._absY + window._heightPixels + windowDoubleBorder;

		return x1l < x2r && x1r > x2l &&  y1t < y2b && y1b > y2t;		
	}

	@Override
	public Drawable.Listener getDrawableListener() {
		return _listener;
	}

}
