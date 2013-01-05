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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.liaquay.tinyx.renderers.awt.GlyphDetail;

public class FontDetail {

	private final int _maxAscent;
	private final int _maxDescent;
	private final int _minWidth;
	private final int _maxWidth;
	private final int _defaultChar;
	private final int _firstChar;
	private final int _lastChar;
	private final int _height;
	private final int _leading;
	
	private final Map<Integer, GlyphDetail> _glyphDetails = new HashMap<Integer, GlyphDetail>();

	public FontDetail(
			final int maxAscent,
			final int maxDescent,
			final int minWidth,
			final int maxWidth,
			final int defaultChar,
			final int firstChar,
			final int lastChar,
			final int height,
			final int leading,
			final Collection<GlyphDetail> glyphDetails) {

		_maxAscent = maxAscent;
		_maxDescent = maxDescent;
		_minWidth = minWidth;
		_maxWidth = maxWidth;
		_defaultChar = defaultChar;
		_firstChar = firstChar;
		_lastChar = lastChar;
		_height = height;
		_leading = leading;
		
		for(final GlyphDetail glyphDetail : glyphDetails) {
			_glyphDetails.put((int) glyphDetail.getChr(), glyphDetail);
		}
	}

	public int getMaxAscent() {
		return _maxAscent;
	}

	public int getMaxWidth() {
		return _maxWidth;
	}

	public int getMaxDescent() {
		return _maxDescent;
	}

	public int getMinWidth() {
		return _minWidth;
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

	public int getHeight() {
		return _height;
	}	

	public int getLeading() {
		return _leading;
	}

	public GlyphDetail getGlyphDetail(int i) {
		return _glyphDetails.get(i);
	}
}
