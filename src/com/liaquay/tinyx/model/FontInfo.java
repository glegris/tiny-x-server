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

/**
 * Holds info about a font derived from a font string. 
 */
public class FontInfo implements Comparable<FontInfo> {
	
	private final String _asText;

	// Type foundry - vendor or supplier of this font
	private final String _foundry;

	// Typeface family
	private final String _familyName;

	// Weight of type
	private final String _weightName;

	// Slant (upright, italic, oblique, reverse italic, reverse oblique, or "other")
	private final String _slant;

	// Proportionate width (e.g. normal, condensed, narrow, expanded/double-wide)
	private final String _setWidthName;

	// Additional style (e.g. (Sans) Serif, Informal, Decorated)
	private final String _addStyleName;

	// Size of characters, in pixels; 0 (Zero) means a scalable font
	private final int _pixelSize;

	// Size of characters, in tenths of points
	private final int _pointSize;

	// Horizontal resolution in dots per inch (DPI), for which the font was designed
	private final int _resolutionX;

	// Vertical resolution, in DPI
	private final int _resolutionY;

	// monospaced, proportional, or "character cell"
	private final String _spacing;

	// Average width of characters of this font; 0 means scalable font
	private final int _averageWidth;

	//  Registry defining this character set
	private final String _charsetRegistry;

	// Registry's character encoding scheme for this set
	private final String _charsetEncoding;

	public FontInfo(
			final String foundry,
			final String familyName, 
			final String weightName,
			final String slant, 
			final String setWidthName, 
			final String addStyleName,
			final int pixelSize, 
			final int pointSize, 
			final int resolutionX, 
			final int resolutionY,
			final String spacing, 
			final int averageWidth, 
			final String charsetRegistry,
			final String charsetEncoding) {
		
		_foundry = foundry;
		_familyName = familyName;
		_weightName = weightName;
		_slant = slant;
		_setWidthName = setWidthName;
		_addStyleName = addStyleName;
		_pixelSize = pixelSize;
		_pointSize = pointSize;
		_resolutionX = resolutionX;
		_resolutionY = resolutionY;
		_spacing = spacing;
		_averageWidth = averageWidth;
		_charsetRegistry = charsetRegistry;
		_charsetEncoding = charsetEncoding;
		_asText =  "-" + _foundry + "-" + _familyName + "-" + _weightName + "-" + _slant + "-" + _setWidthName + "-" + _addStyleName + "-" + _pixelSize + "-" + _pointSize + "-" + _resolutionX + "-" + _resolutionY + "-" + _spacing + "-" + _averageWidth + "-" + _charsetRegistry + "-" + _charsetEncoding;
	}

	public String getFoundry() {
		return _foundry;
	}

	public String getFamilyName() {
		return _familyName;
	}

	public String getWeightName() {
		return _weightName;
	}
	
	public String getSlant() {
		return _slant;
	}

	public String getWidthName() {
		return _setWidthName;
	}

	public String getAddStyleName() {
		return _addStyleName;
	}

	public int getPixelSize() {
		return _pixelSize;
	}

	public int getPointSize() {
		return _pointSize;
	}

	public int getResolutionX() {
		return _resolutionX;
	}

	public int getResolutionY() {
		return _resolutionY;
	}

	public String getSpacing() {
		return _spacing;
	}

	public int getAverageWidth() {
		return _averageWidth;
	}

	public String getCharsetRegistry() {
		return _charsetRegistry;
	}

	public String getCharsetEncoding() {
		return _charsetEncoding;
	}

	
	public String toString() {
		return _asText;
	}

	@Override
	public int compareTo(final FontInfo o) {
		final FontInfo fontInfo = (FontInfo)o;
		return _asText.compareTo(fontInfo._asText);
	}
	
	@Override
	public int hashCode() {
		return _asText.hashCode();
	}
	
	@Override
	public boolean equals(final Object o) {
		final FontInfo fontInfo = (FontInfo)o;
		return _asText.equals(fontInfo._asText);
	}
}

