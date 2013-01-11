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
	private final int _height;
	private final int _left;
	private final int _right;

	public TextExtents(
			final int ascent,
			final int descent,
			final int width,
			final int height,
			final int left,
			final int right) {
		_ascent = ascent;
		_descent = descent;
		_width = width;
		_height = height;
		_left = left;
		_right = right;
	}

	public int getAscent() {
		return _ascent;
	}

	public int getDescent() {
		return _descent;
	}

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

	public int getLeft() {
		return _left;
	}

	public int getRight() {
		return _right;
	}
}
