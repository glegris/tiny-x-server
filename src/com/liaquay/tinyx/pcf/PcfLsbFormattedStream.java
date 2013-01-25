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

import java.io.IOException;

public class PcfLsbFormattedStream implements PcfFormattedStream {

	private final PcfInputStream _delegate;
	
	public PcfLsbFormattedStream(final PcfInputStream delegate) {
		_delegate = delegate;
	}
	
	public int readUnsignedByte() throws IOException {
		return _delegate.readUnsignedByte();
	}
	
	@Override
	public int readUnsignedShort() throws IOException {
		final int b1 = readUnsignedByte();
		final int b2 = readUnsignedByte();
		return b1 | (b2<<8);
	}

	@Override
	public int readSignedShort() throws IOException {
		final int b1 = readUnsignedByte();
		final int b2 = readUnsignedByte();
		final int s1 = b1 | (b2<<8);
		return s1 > 0x8000 ? s1 | 0xffff0000 : s1;
	}

	@Override
	public int readInt() throws IOException {
		final int b1 = readUnsignedByte();
		final int b2 = readUnsignedByte();
		final int b3 = readUnsignedByte();
		final int b4 = readUnsignedByte();
		return b1 | (b2 << 8) | (b3 << 16) | (b4 <<24);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return _delegate.readUnsignedByte() != 0;
	}
}
