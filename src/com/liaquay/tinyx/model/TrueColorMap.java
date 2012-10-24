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
		color._red = (pixel & 0xff) << 8;
		color._green = pixel & 0xff00;
		color._blue = (pixel & 0xff0000) >> 8;
	}

}
