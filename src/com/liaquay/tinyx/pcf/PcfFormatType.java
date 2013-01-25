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

public enum PcfFormatType {
	
	GlyphPad(0, 2),
	MsbByteOrder(2, 1),
	MsbBitOrder(3,1),
	ScanUnit(4,2),
	InkBounds(9,1),
	AccelWInkBounds(8,1),
	CompressedMetrics(8,1);
	
	private final int _position;
	private final int _width;
	private final int _mask;
	
	private PcfFormatType(final int position, final int width) {
		_position = position;
		_width = width;
		_mask = ((1 << width) - 1) << position;
	}
	
	public int getValue(final int format) {
		return (format & _mask) >> _position;
	}
	
	public boolean getBooleanValue(final int format) {
		if(_width != 1) throw new RuntimeException("Boolean value requested but width is " + _width);
		return getValue(format) != 0;
	}
}
