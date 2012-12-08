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

import java.util.Map;
import java.util.TreeMap;

import com.liaquay.tinyx.util.IntMap;

public class Atoms {

	private static final String[] BUILTIN = {
		"PRIMARY", 
		"SECONDARY", 
		"ARC", 
		"ATOM", 
		"BITMAP",
		"CARDINAL", 
		"COLORMAP", 
		"CURSOR", 
		"CUT_BUFFER0", 
		"CUT_BUFFER1",
		"CUT_BUFFER2", 
		"CUT_BUFFER3", 
		"CUT_BUFFER4",
		"CUT_BUFFER5", 
		"CUT_BUFFER6", 
		"CUT_BUFFER7",
		"DRAWABLE", 
		"FONT", 
		"INTEGER",
		"PIXMAP", 
		"POINT", 
		"RECTANGLE", 
		"RESOURCE_MANAGER", 
		"RGB_COLOR_MAP", 
		"RGB_BEST_MAP", 
		"RGB_BLUE_MAP",
		"RGB_DEFAULT_MAP", 
		"RGB_GRAY_MAP", 
		"RGB_GREEN_MAP", 
		"RGB_RED_MAP",
		"STRING", 
		"VISUALID", 
		"WINDOW",
		"WM_COMMAND", 
		"WM_HINTS", 
		"WM_CLIENT_MACHINE",
		"WM_ICON_NAME", 
		"WM_ICON_SIZE", 
		"WM_NAME",
		"WM_NORMAL_HINTS", 
		"WM_SIZE_HINTS", 
		"WM_ZOOM_HINTS",
		"MIN_SPACE", 
		"NORM_SPACE", 
		"MAX_SPACE", 
		"END_SPACE",
		"SUPERSC.LPT_X", 
		"SUPERSC.LPT_Y",
		"SUBSC.LPT_X", 
		"SUBSC.LPT_Y",
		"UNDERLINE_POSITION", 
		"UNDERLINE_THICKNESS",
		"STRIKEOUT_ASCENT", 
		"STRIKEOUT_DESCENT", 
		"ITALIC_ANGLE",
		"X_HEIGHT", 
		"QUAD_WIDTH", 
		"WEIGHT", 
		"POINT_SIZE",
		"RESOLUTION", 
		"COPYRIGHT", 
		"NOTICE", 
		"FONT_NAME",
		"FAMILY_NAME", 
		"FULL_NAME", 
		"CAP_HEIGHT", 
		"WM_CLASS", 
		"WM_TRANSIENT_FOR"
	};	

	private IntMap<Atom> _idToAtomNameMap = new IntMap<Atom>();
	private Map<String, Atom> _atomNameToIdMap = new TreeMap<String, Atom>();
	
	private int _nextId = 1;

	public Atoms() {
		for(int i = 0 ; i < BUILTIN.length; ++i ) {
			allocate(BUILTIN[i]);
		}
	}

	public Atom allocate(final String text) {
		final int id = _nextId++;
		final Atom atom = new Atom(id, text);
		_idToAtomNameMap.put(id, atom);
		_atomNameToIdMap.put(text, atom);
		return atom;
	}
	
	public Atom get(final int id) {
		return _idToAtomNameMap.get(id);
	}

	public Atom get(final String atomName) {
		return _atomNameToIdMap.get(atomName);
	}
	
	public Atom getOrAllocate(final String atomName) {
		final Atom a = get(atomName);
		if(a != null) return a;
		return allocate(atomName);
	}
}
