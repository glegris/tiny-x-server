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

public class PseudoColorMap extends ColorMap {

	public PseudoColorMap(final int id) {
		super(id);
	}

	@Override
	public int getBlackPixel() {
		return 0x00000000;
	}

	@Override
	public int getWhitePixel() {
		return 0x00ffffff;
	}

	@Override
	public boolean isValidColor(final int pixel) {
		return true;
	}

	@Override
	public int getExactRed(final int pixel) {
		final int r = pixel & 0xff0000;
		return (r >> 8) | (r >> 16);
	}

	@Override
	public int getExactGreen(final int pixel) {
		final int g = pixel & 0x00ff00;
		return g | (g >>8);
	}

	@Override
	public int getExactBlue(final int pixel) {
		final int b = pixel & 0x0000ff;
		return b | (b << 8);
	}

	@Override
	public int getVisualRed(final int pixel) {
		final int r = pixel & 0xff0000;
		return (r >> 8) | (r >> 16);
	}

	@Override
	public int getVisualGreen(final int pixel) {
		final int g = pixel & 0x00ff00;
		return g | (g >>8);
	}

	@Override
	public int getVisualBlue(final int pixel) {
		final int b = pixel & 0x0000ff;
		return b | (b << 8);
	}
	
	@Override
	public int lookupNamedColor(final String colorName) {
		final Color c = NAMED_COLOR_MAP.get(colorName.toLowerCase());
		if(c == null) return -1;
		return c.getRGB();
	}

	@Override
	public int allocNamedColor(final String colorName) {
		return lookupNamedColor(colorName);
	}
	
	@Override
	public int allocColor(int exactRed, int exactGreen, int exactBlue) {
		return ((exactRed & 0xff00) << 8) | (exactGreen & 0xff00) | ((exactBlue &0xff00) >> 8);
	}

	@Override
	public boolean isReadOnlyColor(final int pixel) {
		return true;
	}

	@Override
	public int getRGB(final int pixel) {
		return pixel | 0xff000000;
	}
}
