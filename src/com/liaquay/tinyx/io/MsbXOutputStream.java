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
		final int b2 = s & 0xff;
		final int b1 = (s>>8) & 0xff;
		writeByte(b1);
		writeByte(b2);
	}

	@Override
	public void writeInt(final int i) throws IOException {
		final int b4 = i & 0xff;
		final int b3 = (i>>8) & 0xff;
		final int b2 = (i>>16) & 0xff;
		final int b1 = (i>>24) & 0xff;
		writeByte(b1);
		writeByte(b2);
		writeByte(b3);
		writeByte(b4);
	}

	@Override
	public ByteOrder getByteOrder() {
		return ByteOrder.MSB;
	}	
}
