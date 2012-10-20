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

import com.liaquay.tinyx.model.Property;
import com.liaquay.tinyx.model.Property.Format;


public class ShortProperty extends Property {
	
	private short[] _data;

	public ShortProperty(final int propertyAtomId, final int typeAtomId, final short[] data) {
		super(propertyAtomId, typeAtomId);
		_data = data;		
		if((_data.length &3) != 0) throw new RuntimeException("Multiples of 2 shorts please");
	}

	@Override
	public int getLengthInBytes() {
		return _data.length << 1;
	}

	@Override
	public Format getFormat() {
		return Format.ByteFormat;
	}

	@Override
	public int getLength() {
		return _data.length;
	}
	
	public short[] getData() {
		return _data;
	}	
}

