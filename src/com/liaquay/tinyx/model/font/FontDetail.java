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
package com.liaquay.tinyx.model.font;

import com.liaquay.tinyx.model.TextExtents;


public class FontDetail {

	private final TextExtents _minBounds;
	private final TextExtents _maxBounds;
	private final int _defaultChar;
	private final int _firstChar;
	private final int _lastChar;
	private final boolean _leftToRight;
	private final int _ascent;
	private final int _descent;

	public FontDetail(
			final TextExtents minBounds,
			final TextExtents maxBounds,
			final int defaultChar,
			final int firstChar,
			final int lastChar,
			final boolean leftToRight, 
			final int ascent, 
			final int descent) {
		
		_minBounds = minBounds;
		_maxBounds = maxBounds;
		_defaultChar = defaultChar;
		_firstChar = firstChar;
		_lastChar = lastChar;
		_leftToRight = leftToRight;
		_ascent = ascent;
		_descent = descent;
	}

	public TextExtents getMinBounds() {
		return _minBounds;
	}

	public TextExtents getMaxBounds() {
		return _maxBounds;
	}

	public int getDefaultChar() {
		return _defaultChar;
	}

	public int getFirstChar() {
		return _firstChar;
	}

	public int getLastChar() {
		return _lastChar;
	}
	
	public boolean isLeftToRight() {
		return _leftToRight;
	}
	
	public int getAscent() {
		return _ascent;
	}

	public int getDescent() {
		return _descent;
	}	
}
