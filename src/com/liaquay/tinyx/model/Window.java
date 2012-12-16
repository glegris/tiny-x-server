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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liaquay.tinyx.model.ButtonGrab.Trigger;
import com.liaquay.tinyx.model.eventfactories.EventFactories;

public class Window implements Drawable {

	private final Window _parent;
	private final List<Window> _children = new ArrayList<Window>();

	public interface Listener {
		public void childCreated(final Window child);
		public void mapped(final Window window, final boolean mapped);
		public void visible(final Window window, final boolean visible);
		public void renderDrawable(final Drawable drawable, final GraphicsContext graphicsContext,
			int srcX, int srcY, int width, int height, int dstX, int dstY);
		public void setCursor(Cursor cursor);
	}

	/**
	 * Empty implementation of the listener so that we don't have null checks 
	 * throughout the code. 
	 */
	private static final class NullListener implements Listener {
		@Override
		public void childCreated(final Window child) {}
		@Override
		public void mapped(final Window window, final boolean mapped) {}
		@Override
		public void visible(Window window, boolean visible) {}
		@Override
		public void renderDrawable(Drawable drawable,
				GraphicsContext graphicsContext, int srcX, int srcY, int width,
				int height, int dstX, int dstY) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setCursor(Cursor cursor) {
			// TODO Auto-generated method stub
			
		}
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
	private int _x, _y;			/* relative location of window */
	private int _absX, _absY;      /* absolute location of window */

	private Cursor _cursor;			/* The cursor to use when the mouse is within this window */
	private int _clipX, _clipY;    /* clip location of window */
	private int _clipW, _clipH;    /* clip size of window */

	private int _widthPixels, _heightPixels;	/* width and height of window in pixels */
	private int _borderWidth;		/* border width of window */
	private WindowClass _windowClass;

	private int _backgroundPixel = 0; // TODO Default value
	private int _borderPixel = 0; // TODO Default value
	private Pixmap _borderPixmap = null;

	private boolean _mapped = false;
	private boolean _viewable = false;
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
	private final Map<ButtonGrab.Trigger, ButtonGrab> _buttonGrabs = new HashMap<ButtonGrab.Trigger, ButtonGrab>(4);
	

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

		updateLocation();
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

	// TODO Work out difference between visible and exposed.
	private void updateVisibility() {
		
		final boolean newVisibility = isMappedToRoot() && nonZeroClippedArea() && !isInputOnly();
		
		if(!_viewable && newVisibility) {
			// Expose
			_listener.visible(this, true);

			// TODO issue visibility event
//			final Event mapNotifyEvent = _eventFactories.getMapNotifyFactory().create()
		}
		else if(_viewable && !newVisibility){
			// Hide
			_listener.visible(this, false);
			
			// TODO issue visibility event
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
		_listener.childCreated(child);
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

			if(isMappedToRoot()) {
				_listener.mapped(this,true);
			}
			
			updateVisibility();
		}
	}

	/**
	 * Determine if all the window up to the root window are mapped,
	 * in which case this window is mapped and displayable.
	 */
	private boolean isMappedToRoot() {
		return _mapped && (_parent == null || _parent.isMappedToRoot());
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
			_listener.mapped(this, false);
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
			return isViewable() ? MappedState.IsViewable : MappedState.IsUnviewable;
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

	public final boolean isViewable() {
		return _viewable;
	}
	
	private final boolean nonZeroClippedArea() {
		return _clipH > 0 && _clipW > 0;
	}
	
	public void copyArea(Window window, GraphicsContext graphicsContext,
			int srcX, int srcY, int width, int height, int dstX, int dstY) {

		_listener.renderDrawable(window, graphicsContext, srcX, srcY, width, height, dstX, dstY);

//		Graphics g=dst.getGraphics();
//		if(g==null) return;
//
//		if(window==dst){ 
//			copyArea(srcx, srcy, width, height, destx-srcx, desty-srcy); 
//			dst.draw(destx, desty, width, height);
//			return;
//		}
//
//		Image img=window.getImage(gc, srcx, srcy, width, height);
//		if(srcx==0 && srcy==0 && width==window.width && height==window.height){
//			dst.ddxwindow.drawImage(gc.clip_mask, img, destx, desty, width, height);
//		}
//		else{
//			java.awt.Shape tmp=g.getClip();
//			g.clipRect(destx, desty, width, height);
//			dst.ddxwindow.drawImage(gc.clip_mask, img, destx-srcx, desty-srcy, 
//					window.width, window.height);
//			if(tmp==null){ g.setClip(0, 0, dst.width, dst.height);}
//			else{g.setClip(tmp);}
//		}
//		dst.draw(destx, desty, width, height);
//		if(img!=window.getImage()){
//			img.flush();
//		}
	}

	public void copyArea(Pixmap pixmap, GraphicsContext graphicsContext,
			int srcX, int srcY, int width, int height, int dstX, int dstY) {

		_listener.renderDrawable(pixmap, graphicsContext, srcX, srcY, width, height, dstX, dstY);
	}
	
	public ButtonGrab findFirstButtonGrab(final Trigger trigger) {
		ButtonGrab grab = null;
		if(_parent != null) {
			grab = _parent.findFirstButtonGrab(trigger);
		}
		if(grab == null) {
			grab = _buttonGrabs.get(trigger);
			
			// Check the confine-to window is viewable
			if(grab.getConfineToWindow() != null) {
				if(!grab.getConfineToWindow().isViewable()) {
					grab = null;
				}
			}
		}
		return grab;
	}
}
