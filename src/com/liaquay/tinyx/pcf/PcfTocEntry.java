/*
 *  PCF font reader
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
package com.liaquay.tinyx.pcf;

public class PcfTocEntry {
	private final int _type;
	private final int _format;
	private final int _size;
	private final int _offset;
	
	public PcfTocEntry(
			final int type,
			final int format,
			final int size,
			final int offset
			) {
		_type = type;
		_format = format;
		_size = size;
		_offset = offset;
	}
	
	public String toString() {
		return "Type " + _type + ", Format " +_format+", size "+_size+", offset " +_offset;
	}
	
	public int getType() {
		return _type;
	}
	
	public int getFormat() {
		return _format;
	}
	
	public int getSize() {
		return _size;
	}
	
	public int getOffset() {
		return _offset;
	}
}
