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
	
	public enum AllocType {
		AllocNone,
		AllocAll;
		
		public static AllocType getFromIndex(final int index) {
			final AllocType[] allocTypes = values();
			if (index<allocTypes.length && index>=0)
				return allocTypes[index];
			return null;
		}
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
	
	/**
	 * Return the pixel of the named colour
	 * @param colorName
	 * @return the pixel of the named colour
	 */
	public abstract int lookupNamedColor(final String colorName);
	public abstract int allocNamedColor(final String colorName);
	public abstract int allocColor(final int exactRed, final int exactGreen, final int exactBlue);
	public abstract boolean isValidColor(final int pixel);
	public abstract int getExactRed(final int pixel);
	public abstract int getExactGreen(final int pixel);
	public abstract int getExactBlue(final int pixel);
	public abstract int getVisualRed(final int pixel);
	public abstract int getVisualGreen(final int pixel);
	public abstract int getVisualBlue(final int pixel);

}
