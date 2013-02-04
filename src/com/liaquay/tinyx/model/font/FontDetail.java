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

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.TextExtents;

public interface FontDetail {
	
	public enum Slant {
		r,i
	}

	public enum Weight {
		medium, bold
	}
	
	public String getName();
	public String getFoundry();
	public String getFamilyName();
	public String getWeightName();
	public String getSlant();
	public String getWidthName();
	public String getAddStyleName();
	public int getPixelSize();
	public int getPointSize();
	public int getResolutionX();
	public int getResolutionY();
	public String getSpacing();
	public int getAverageWidth();
	public String getCharsetRegistry();
	public String getCharsetEncoding();
	public TextExtents getMinBounds();
	public TextExtents getMaxBounds();
	public int getDefaultChar(); // TODO should this be a char
	public int getFirstChar();
	public int getLastChar();
	public boolean isLeftToRight();
	public int getAscent();
	public int getDescent();
	public TextExtents getTextExtents(final int character);
	public TextExtents getTextExtents(final String text);
	
	public void drawString(
			final Drawable drawable, 
			final String text, 
			final int xs, 
			final int ys, 
			final int color);
	
	public void drawString(
			final Drawable drawable,
			final String text,
			final int xs,
			final int ys,
			final int color,
			final int bx,
			final int by, 
			final int bw,
			final int bh,
			final int bgColor);
}
