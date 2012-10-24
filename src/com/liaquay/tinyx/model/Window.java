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

import com.liaquay.tinyx.util.Tree;

public class Window extends Tree<Window> implements Drawable {

	private final int _resourceId;
	private final Visual _visual;
	private final Properties _properties = new Properties();

	private final int _depth;		/* depth of window */
	private int _x, _y;				/* location of window */
	private int _width, _height;	/* width and height of window */
	private int _border_width;		/* border width of window */
	
	private int _bit_gravity;		/* one of bit gravity values */
	private int _win_gravity;		/* one of the window gravity values */

	
	
	
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
			final int depth) {
		
		super(parent);
		_resourceId = resourceId;
		_visual = visual;
		_depth = depth;
	}

	@Override
	public int getId() {
		return _resourceId;
	}
	
	public void free() {}

	public Property getProperty(final int propertyId) {
		return _properties.get(propertyId);
	}

	public Property deleteProperty(final int propertyId) {
		return _properties.remove(propertyId);
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
}
