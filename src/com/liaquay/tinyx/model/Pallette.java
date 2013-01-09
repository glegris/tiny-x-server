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

public class Pallette extends ColorMap {
	
	private int[] _red;
	private int[] _green;
	private int[] _blue;

	public Pallette(final int id) {
		super(id);
	}

	@Override
	public int getBlackPixel() {
		return 0; // TODO
	}

	@Override
	public int getWhitePixel() {
		return 0; // TODO
	}
	
	public boolean isValidColor(final int pixel) {
		return pixel >= 0 && pixel < _red.length;
	}
	

	@Override
	public int lookupNamedColor(String colorName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getExactRed(int pixel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getExactGreen(int pixel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getExactBlue(int pixel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVisualRed(int pixel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVisualGreen(int pixel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVisualBlue(int pixel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int allocNamedColor(String colorName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int allocColor(int exactRed, int exactGreen, int exactBlue) {
		// TODO Auto-generated method stub
		return 0;
	}
}
