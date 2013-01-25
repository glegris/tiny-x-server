/*
 *  PCF font reader
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
package com.liaquay.tinyx.pcf;

public class PcfMetrics {
	/**
	 * The distance between the character origin and the start of the
	 * bitmap.  The leftSideBearing is zero if the character starts at
	 * the origin, positive if there is padding before the bitmap
	 * starts, or negative in the unusual case that the bitmap starts
	 * before the specified origin.
	 */
	private final int _leftSideBearing;

	/**
	 * The distance between the character origin and the end of the
	 * bitmap.  The rightSideBearing is normally positive, specifying the
	 * number of pixels to the right of the origin.  The width of the
	 * bitmap is is given by rightSideBearing-leftSideBearing.
	 * The width of the bitmap is, in general, different from the width
	 * of the character.
	 */
	private final int _rightSideBearing;

	/**
	 * The distance in pixels between the character origin and the
	 * origin of the next character.
	 */
	private final int _Width;

	/**
	 * The ascent in pixels of the character.  This is the number of
	 * pixels in the bitmap above the origin.  The height of the bitmap
	 * is given by ascent+descent.
	 */
	private final int _ascent;

	/**
	 * The distance in pixels of the character.  This is the number of
	 * pixels in the bitmap below the origin.
	 */
	private final int _descent;

	private final int _attributes;
	
	public String toString() {
		return
				"leftSideBearing="+_leftSideBearing+
				", rightSideBearing="+_rightSideBearing+
				", characterWidth="+_Width+
				", ascent="+_ascent+
				", descent="+_descent;
	}

	public int getLeftSideBearing() {
		return _leftSideBearing;
	}

	public int getRightSideBearing() {
		return _rightSideBearing;
	}

	public int getWidth() {
		return _Width;
	}

	public int getAscent() {
		return _ascent;
	}

	public int getDescent() {
		return _descent;
	}

	public int getAttributes() {
		return _attributes;
	}
	
	public int getHeight() {
		return _ascent + _descent;
	}
	
	public PcfMetrics(
			final int leftSideBearing, 
			final int rightSideBearing,
			final int characterWidth, 
			final int ascent, 
			final int descent,
			final int attributes) {
		
		_leftSideBearing = leftSideBearing;
		_rightSideBearing = rightSideBearing;
		_Width = characterWidth;
		_ascent = ascent;
		_descent = descent;
		_attributes = attributes;
	}
}

