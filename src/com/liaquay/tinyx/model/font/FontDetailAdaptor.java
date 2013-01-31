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

import com.liaquay.tinyx.model.FontInfo;


public abstract class FontDetailAdaptor implements FontDetail {

	private final String _name;
	private final FontInfo _fontInfo;
	
	public FontDetailAdaptor(
			final String name,
			final FontInfo fontInfo) {
		
		_name = name;
		_fontInfo = fontInfo;
	}	
	
	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getFoundry() {
		return _fontInfo.getFoundry();
	}

	@Override
	public String getFamilyName() {
		return _fontInfo.getFamilyName();
	}

	@Override
	public String getWeightName() {
		return _fontInfo.getWeightName();
	}

	@Override
	public String getSlant() {
		return _fontInfo.getSlant();
	}

	@Override
	public String getWidthName() {
		return _fontInfo.getWidthName();
	}

	@Override
	public String getAddStyleName() {
		return _fontInfo.getAddStyleName();
	}

	@Override
	public int getPixelSize() {
		return _fontInfo.getPixelSize();
	}

	@Override
	public int getPointSize() {
		return _fontInfo.getPointSize();
	}

	@Override
	public int getResolutionX() {
		return _fontInfo.getResolutionX();
	}

	@Override
	public int getResolutionY() {
		return _fontInfo.getResolutionY();
	}

	@Override
	public String getSpacing() {
		return _fontInfo.getSpacing();
	}

	@Override
	public int getAverageWidth() {
		return _fontInfo.getAverageWidth();
	}

	@Override
	public String getCharsetRegistry() {
		return _fontInfo.getCharsetRegistry();
	}

	@Override
	public String getCharsetEncoding() {
		return _fontInfo.getCharsetEncoding();
	}
	
	public String toString() {
		return _name;
	}
}
