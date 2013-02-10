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

public class TextExtents {
	private final int _ascent;
	private final int _descent;
	private final int _width;
	private final int _left;
	private final int _right;
	private final int _attributes;

	public TextExtents(
			final int ascent,
			final int descent,
			final int width,
			final int left,
			final int right,
			final int attributes) {
		_ascent = ascent;
		_descent = descent;
		_width = width;
		_left = left;
		_right = right;
		_attributes = attributes;
	}

	/**
	 * The ascent in pixels of the character.  This is the number of
	 * pixels in the bitmap above the origin.  The height of the bitmap
	 * is given by ascent+descent.
	 */
	public int getAscent() {
		return _ascent;
	}

	/**
	 * The distance in pixels of the character.  This is the number of
	 * pixels in the bitmap below the origin.
	 */
	public int getDescent() {
		return _descent;
	}

	/**
	 * The distance in pixels between the character origin and the
	 * origin of the next character.
	 */
	public int getWidth() {
		return _width;
	}

	/**
	 * Ascent + Descent
	 */
	public int getHeight() {
		return _ascent + _descent;
	}

	/**
	 * The distance between the character origin and the start of the
	 * bitmap.  The leftSideBearing is zero if the character starts at
	 * the origin, positive if there is padding before the bitmap
	 * starts, or negative in the unusual case that the bitmap starts
	 * before the specified origin.
	 */
	public int getLeft() {
		return _left;
	}

	/**
	 * The distance between the character origin and the end of the
	 * bitmap.  The rightSideBearing is normally positive, specifying the
	 * number of pixels to the right of the origin.  The width of the
	 * bitmap is is given by rightSideBearing-leftSideBearing.
	 * The width of the bitmap is, in general, different from the width
	 * of the character.
	 */
	public int getRight() {
		return _right;
	}
	
	/**
	 * TODO Need to find out what these are
	 */
	public int getAttributes() {
		return _attributes;
	}
	
	public String toString() {
		return 
				"Ascent " + _ascent + 
				",Descent " + _descent +
				", Left " + _left + 
				", Right " + _right + 
				", Width " + _width;
	}
}
