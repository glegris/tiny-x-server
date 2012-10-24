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

public abstract class ColorMap extends AbstractResource {

	public static class Color {
		public int _red;
		public int _green;
		public int _blue;
	}
	
	public enum AllocType {
		AllocNone,
		AllocAll
	}
	
	public ColorMap(final int id) {
		super(id);
	}
	
	/**
	 * Return the value of a black pixel.
	 *
	 * @return	The value of a black pixel.
	 */
	public abstract int getBlackPixel ();

	/**
	 * Return the value of a white pixel.
	 *
	 * @return	The value of a white pixel.
	 */
	public abstract int getWhitePixel ();
	
	public abstract boolean isValidColor(final int pixel);
	
	public abstract void getColor(final int pixel, final Color color);
}
