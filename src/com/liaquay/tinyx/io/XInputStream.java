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

public interface XInputStream {
	public String readString() throws IOException;
	public int readUnsignedByte() throws IOException;
	public int readSignedByte() throws IOException;
	public int readUnsignedShort() throws IOException;
	public int readSignedShort() throws IOException;
	public int readInt() throws IOException;
	public int read(final byte[] data, final int start, final int length) throws IOException;
	public int readNibbles(final byte[] data, final int start, final int length) throws IOException;
	public int getCounter();
	public void resetCounter();
	public void skip(int remaining) throws IOException;
	public ByteOrder getByteOrder();
}
