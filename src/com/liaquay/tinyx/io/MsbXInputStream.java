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
package com.liaquay.tinyx.io;

import java.io.IOException;
import java.io.InputStream;

public class MsbXInputStream  extends AbstractXInputStream  {
	
	public MsbXInputStream(final InputStream inputStream) {
		super(inputStream);
	}
	
	@Override
	public int readUnsignedShort() throws IOException {
		_inputStream.read(bytes, 0, 2);
		_counter+=2;
		
		int b2 = (int) bytes[0] & 0xFF;
		int b1 = (int) bytes[1] & 0xFF;

		return b1 | (b2<<8);
	}

	@Override
	public int readSignedShort() throws IOException {
		_inputStream.read(bytes, 0, 2);
		_counter+=2;
		
		int b2 = (int) bytes[0] & 0xFF;
		int b1 = (int) bytes[1] & 0xFF;

		final int s1 = b1 | (b2<<8);
		return s1 > 0x8000 ? s1 | 0xffff0000 : s1;
	}

	byte bytes[] = new byte[4];
	
	@Override
	public int readInt() throws IOException {
		_inputStream.read(bytes, 0, 4);
		_counter+=4;
		
		int b4 = (int) bytes[0] & 0xFF;
		int b3 = (int) bytes[1] & 0xFF;
		int b2 = (int) bytes[2] & 0xFF;
		int b1 = (int) bytes[3] & 0xFF;
		
		return (b1) | (b2 << 8) | (b3 << 16) | (b4 << 24);
	}

	@Override
	public ByteOrder getByteOrder() {
		return ByteOrder.MSB;
	}
}
