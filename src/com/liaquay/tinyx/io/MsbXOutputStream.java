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
import java.io.OutputStream;

public class MsbXOutputStream extends AbstractXOutputStream {

	public MsbXOutputStream(final OutputStream outputStream) {
		super(outputStream);
	}
	
	@Override
	public void writeShort(final int s) throws IOException {
		byte[] bytes = new byte[2];

		bytes[1] = (byte) (s & 0xff);
		bytes[0] = (byte) ((s>>8) & 0xff);

		_outputStream.write(bytes);
		_counter += 2;
	}

	@Override
	public void writeInt(final int i) throws IOException {
		byte[] bytes = new byte[4];
		
		bytes[3] = (byte) (i & 0xff);
		bytes[2] = (byte) ((i>>8) & 0xff);
		bytes[1] = (byte) ((i>>16) & 0xff);
		bytes[0] = (byte) ((i>>24) & 0xff);
		
		_outputStream.write(bytes);
		_counter += 4;		
	}

	@Override
	public ByteOrder getByteOrder() {
		return ByteOrder.MSB;
	}	
}
