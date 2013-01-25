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

public class PcfBitmaps {
	private final boolean _msbBitOrder;
	private final boolean _msbByteOrder;
	private final int _glyphPadding;
	private final int _scanUnit;
	private final int[] _offsets;
	private final byte[] _data;

	public PcfBitmaps(
			final boolean msbBitOrder,
			final boolean msbByteOrder,
			final int glyphPadding,
			final int scanUnit,
			final int[] offsets,
			final byte[] data) {

		_msbBitOrder = msbBitOrder;
		_msbByteOrder = msbByteOrder;
		_glyphPadding = glyphPadding;
		_scanUnit = scanUnit;
		_offsets = offsets;
		_data = data;
	}
	
	public interface Renderer {
		public void render(final int x, final int y);
	}
	
	public void getBitmap(
			final int glyphIndex, 
			final PcfMetrics metrics,
			final int xoffset,
			final int yoffset,
			final Renderer renderer) {
		
		final int offset = _offsets[glyphIndex];
		final int bitflip = _msbBitOrder ? 7 : 0;
		final int byteflip = _msbBitOrder == _msbByteOrder ? 0 : _scanUnit - 1;
		final int height = metrics.getAscent() + metrics.getDescent();
		final int width = -metrics.getLeftSideBearing() + metrics.getRightSideBearing();
		if (height<0 || width<0) {
			System.out.println("Negative height/width for offset " + offset);
			return;
		}
		final int stride = (((width+7)>>3)+_glyphPadding-1)&~(_glyphPadding-1);
		final int ys = yoffset - metrics.getAscent();
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int bit_pos = (x&7)^bitflip;
				int byte_pos = x>>3;
				if ((_data[offset+((byte_pos+y*stride)^byteflip)]&(1<<bit_pos)) != 0) {
					renderer.render(xoffset+x + metrics.getLeftSideBearing(), ys+y);
				}
			}
		}
	}
}
