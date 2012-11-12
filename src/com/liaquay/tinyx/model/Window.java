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
import java.util.List;

import com.liaquay.tinyx.model.eventfactories.EventFactories;

public class Window implements Drawable {
	
	private final Window _parent;
	private final List<Window> _children = new ArrayList<Window>();
	
	public interface Listener {
		public void childCreated(final Window parent, final Window child);
		public void mapped(final boolean mapped);
	}
	
	/**
	 * Empty implementation of the listener so that we don't have null checks 
	 * throughout the code. 
	 */
	private static final class NullListener implements Listener {
		@Override
		public void childCreated(final Window parent, final Window child) {}
		@Override
		public void mapped(boolean mapped) {}
	}
	
	private static final Listener NULL_LISTENER = new NullListener();
	
	private Listener _listener = NULL_LISTENER;
	
	public void setListener(final Listener listener) {
		_listener = listener;
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
	
	private BackingStoreHint _backingStoreHint = BackingStoreHint.BackingStoreAlways; // TODO Check default value
	
	public enum Gravity {
		ForgetGravity,
		NorthWestGravity,
		NorthGravity,
		NorthEastGravity,
		WestGravity,
		CenterGravity,
		EastGravity,
		SouthWestGravity,
		SouthGravity,
		SouthEastGravity,
		StaticGravity;
		
		public static Gravity getFromIndex(final int index) {
			final Gravity[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}
	}
	
	private Gravity _bitGravity = 	Gravity.NorthWestGravity;
	private Gravity _winGravity  = Gravity.	NorthWestGravity;
	
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

	private final int _depth;		/* depth of window */
	private int _x, _y;			/* location of window */
	private int _widthPixels, _heightPixels;	/* width and height of window in pixels */
	private int _borderWidth;		/* border width of window */
	private WindowClass _windowClass;

	private int _backgroundPixel = 0; // TODO Default value
	private int _borderPixel = 0; // TODO Default value
	private Pixmap _borderPixmap = null;
	
	private Pointer _pointer = new Pointer();
	private boolean _mapped = false;
	// TODO values are rubbish
	private int _backingPlanes = 0;
	private int _backingPixel = 0;
	private boolean _saveUnder= false;
	private boolean _overrideRedirect = false;
	private ColorMap _colorMap = null;
	private int _doNotPropagateMask = 0;
	private Pixmap _backgroundPixmap = null;
	private boolean _parentRelativeBackgroundPixmap = false;
	private final EventFactories _eventFactories;

//  Window root;                /* root of screen containing window */
//    int backing_store;          /* NotUseful, WhenMapped, Always */
//    unsigned long backing_planes;/* planes to be preserved if possible */
//    unsigned long backing_pixel;/* value to be used when restoring planes */
//    Bool save_under;            /* boolean, should bits under be saved? */
//    Colormap colormap;          /* color map to be associated with window */
//    Bool map_installed;         /* boolean, is color map currently installed*/
//    int map_state;              /* IsUnmapped, IsUnviewable, IsViewable */
//    long all_event_masks;       /* set of events all people have interest in*/
//    long your_event_mask;       /* my event mask */
//    long do_not_propagate_mask; /* set of events that should not propagate */
//    Bool override_redirect;     /* boolean value for override-redirect */
//    Screen *screen;             /* back pointer to correct screen */

    
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
			final EventFactories eventFactories) {
		
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
		
		if(_parent != null) {
			_parent.addChild(this);
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
		_listener.childCreated(this, child);
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
	
	public Pointer getPointer() {
		return _pointer;
	}

	public boolean isMapped() {
		return _mapped;
	}
	
	public void map() {
		if(!_mapped ) {
			_mapped = true;
			
			if (!_overrideRedirect && getParent() != null && getParent().checkForEvent(Event.SubstructureRedirectMask)) {
				final Event mapRequestEvent = _eventFactories.getMapRequestFactory().create(false, getParent(), this);
				getParent().deliver(mapRequestEvent, Event.SubstructureRedirectMask);
			}

			if(checkForEvent(Event.StructureNotifyMask)) {
				final Event mapNotifyEvent = _eventFactories.getMapNotifyFactory().create(false, this, this, _overrideRedirect);
				deliver(mapNotifyEvent, Event.StructureNotifyMask);
			}
			
			if(checkForEvent(Event.SubstructureNotifyMask)) {
				final Event mapNotifyEvent = _eventFactories.getMapNotifyFactory().create(false, getParent(), this, _overrideRedirect);
				deliver(mapNotifyEvent, Event.SubstructureNotifyMask);
			}
			
			// TODO check for visibility changes 
			// TODO Send visibility events 
			
			_listener.mapped(true);
		}
	}
	
	private boolean checkForEvent(final int mask) {
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			if((assoc.getEventMask() & mask) != 0) {
				return true;
			}
		}	
		return false;
	}
	
	private void deliver(final Event event, final int mask){
		for(int i = 0; i < _clientWindowAssociations.size(); ++i) {
			final ClientWindowAssociation assoc = _clientWindowAssociations.get(i);
			if((assoc.getEventMask() & mask) != 0) {
				assoc.getClient().getPostBox().send(event);
			}
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
			// TODO issue some unmapped event
			_listener.mapped(false);
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
	
	public Gravity getBitGravity() {
		return _bitGravity;
	}
	
	public Gravity getWinGravity() { 
		return _winGravity;
	}
	
	public MappedState getMappedState() {
		if(_mapped) {
			// TODO Should this value change 
			return MappedState.IsViewable;
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

	@Override
	public int getBorderWidth() {
		return _borderWidth;
	}

	public void setBackgroundPixel(final int backGroundPixel) {
		_backgroundPixel = backGroundPixel;
	}

	public void setBorderPixel(final int borderPixel) {
		_borderPixel = borderPixel;
	}

	public void setBitGravity(final Gravity gravity) {
		_bitGravity = gravity;
	}

	public void setWinGravity(Gravity gravity) {
		_winGravity = gravity;
	}

	public void setColorMap(final ColorMap colorMap) {
		_colorMap = colorMap;
	}

	public void setBackgroundPixmap(final Pixmap pixmap, boolean relative) {
		_backgroundPixmap = pixmap;
		_parentRelativeBackgroundPixmap = relative;
	}

	public Pixmap getBackgroundPixmap() {
		return _backgroundPixmap;
	}

	public Pixmap getBorderPixmap() {
		return _borderPixmap;
	}

	public void setBorderPixmap(final Pixmap borderPixmap) {
		_borderPixmap = borderPixmap;
	}

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

	public void clearArea(final boolean exposures, final int x, final int y, final int width, final int height) {
		// TODO Auto-generated method stub
		
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
}
