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
package com.liaquay.tinyx.model.properties;

import com.liaquay.tinyx.model.Atom;
import com.liaquay.tinyx.model.Property.Format;


public class IntPropertyValue extends PropertyValue {
	
	private int[] _data;

	public IntPropertyValue(final Atom typeAtom, final int[] data) {
		super(typeAtom);
		_data = data;
	}
	
	@Override
	public Format getFormat() {
		return Format.IntFormat;
	}

	@Override
	public int getLengthInBytes() {
		return _data.length << 2;
	}	

	@Override
	public int getLength() {
		return _data.length;
	}
	
	public int[] getData() {
		return _data;
	}	
	
	public void prepend(final int[] data) {
		final int[] n = new int[_data.length + data.length];
		System.arraycopy(data, 0, n, 0, data.length);
		System.arraycopy(_data, 0, n, data.length, _data.length);
		_data = n;
	}	
	
	public void append(final int[] data) {
		final int[] n = new int[_data.length + data.length];
		System.arraycopy(_data, 0, n, 0, _data.length);
		System.arraycopy(data, 0, n, _data.length, data.length);
		_data = n;
	}	
}

