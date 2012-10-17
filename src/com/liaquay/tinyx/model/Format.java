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

public class Format {

	private final int _depth;
	private final int _bpp;
	private final int _scanLinePad;

	public Format(final int depth, final int bpp, final int scanLinePad) {
		_depth = depth; 
		_bpp = bpp; 
		_scanLinePad = scanLinePad;
	}

	public int getDepth() {
		return _depth;
	}

	public int getBpp() {
		return _bpp;
	}

	public int getScanLinePad() {
		return _scanLinePad;
	}
}