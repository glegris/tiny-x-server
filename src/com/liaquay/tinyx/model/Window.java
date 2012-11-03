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

import com.liaquay.tinyx.model.Window.Gravity;
import com.liaquay.tinyx.util.Tree;

public class Window extends Tree<Window> implements Drawable {
	
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

	private final int _depth;		/* depth of window */
	private int _x, _y;			/* location of window */
	private int _widthPixels, _heightPixels;	/* width and height of window in pixels */
	private int _borderWidth;		/* border width of window */
	private WindowClass _windowClass;

	private int _backgroundPixel = 0; // TODO Default value
	private int _borderPixel = 0; // TODO Default value
	
	private Pointer _pointer = new Pointer();
	private boolean _mapped = false;
	// TODO values are rubbish
	private int _backingPlanes = 0;
	private int _backingPixel = 0;
	private int _saveUnders = 0;
	private int _eventMask = 0;
	private boolean _overrideRedirect = false;
	private ColorMap _colorMap = null;
	private int _doNotPropagateMask = 0;

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
			final WindowClass windowClass) {
		
		super(parent);
		_resourceId = resourceId;
		_visual = visual;
		_depth = depth;
		_widthPixels = width;
		_heightPixels = height;
		_x = x;
		_y = y;
		_borderWidth = borderWidth;
		_windowClass = windowClass;
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
		// TODO Unlink from parent
	}

	public Properties getProperties() {
		return _properties;
	}

	@Override
	public Screen getScreen() {
		return getParent().getScreen();
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
			// TODO issue some mapped event
		}
	}
	
	/**
	 * Performs a MapWindow request on all unmapped children of the window,
	 * in top-to-bottom stacking order.
	 */
	public void mapSubwindows() {
		// TODO not sure the order is correct
		for(Window w = getFirstchild(); w != null; w = w.getNextSibling()) {
			w.mapSubwindows();
		}
	}
	
	public void unmap() {
		if(_mapped) {
			_mapped = false;
			// TODO issue some unmapped event
		}
	}
	
	/**
	 * Performs an UnmapWindow request on all mapped children of the window,
	 * in bottom-to-top stacking order
	 */
	public void unmapSubwindows() {
		// TODO not sure the order is correct
		for(Window w = getLastchild(); w != null; w = w.getPrevSibling()) {
			w.mapSubwindows();
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
	
	public int getSaveUnders() {
		return _saveUnders;
	}
	
	public int getEventMask() {
		return _eventMask;
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
}
