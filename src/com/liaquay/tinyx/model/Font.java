/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Nathan Ludkin
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

import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.renderers.awt.GlyphDetail;


public class Font extends AbstractResource {

	private final FontInfo _fontName;
	private final FontDetail _fontDetail;
	
	public Font(final int id, final FontInfo fontName, final FontDetail fontDetail) {
		super(id);

		_fontName = fontName;
		_fontDetail = fontDetail;
	}

	@Override
	public void free() {
		// TODO Auto-generated method stub

	}

	public FontInfo getFontInfo() {
		return _fontName;
	}
	
	public int getMaxAscent() {
		return _fontDetail.getMaxAscent();
	}

	public int getMaxWidth() {
		return _fontDetail.getMaxWidth();
}

	public int getMaxDescent() {
		return _fontDetail.getMaxDescent();
	}

	public int getMinWidth() {
		return _fontDetail.getMinWidth();
	}

	public int getDefaultChar() {
		return _fontDetail.getDefaultChar();
	}

	public int getFirstChar() {
		return _fontDetail.getFirstChar();
	}

	public int getLastChar() {
		return _fontDetail.getLastChar();
	}

	public GlyphDetail getGlyphDetail(int i) {
		return _fontDetail.getGlyphDetail(i);
	}
}
