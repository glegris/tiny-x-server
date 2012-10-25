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

public class TrueColorMap extends ColorMap {

	public TrueColorMap(final int id) {
		super(id);
	}

	@Override
	public int getBlackPixel() {
		return 0xff000000;
	}

	@Override
	public int getWhitePixel() {
		return 0xffffffff;
	}

	@Override
	public boolean isValidColor(final int pixel) {
		return true;
	}

	@Override
	public void getColor(final int pixel, final Color color) {
		final int r = pixel & 0x0000ff;
		final int g = pixel & 0x00ff00;
		final int b = pixel & 0xff0000;
		color._red = r | (r << 8);
		color._green = g | (g >>8);
		color._blue = (b >> 8) | (b >> 16);
	}
}
