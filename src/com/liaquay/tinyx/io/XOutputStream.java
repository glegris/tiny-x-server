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

public interface XOutputStream {
	public void writeString(final String str) throws IOException;
	public void writeBoolean(final boolean b) throws IOException;
	public void writeByte(final byte b) throws IOException;
	public void writeByte(final int i) throws IOException;
	public void writeShort(final int s) throws IOException;
	public void writeInt(final int i) throws IOException;
	public void write(final byte[] data, final int start, final int length) throws IOException;
	public void send() throws IOException;
	public void writePad(int i) throws IOException;
	public int getCounter();
	public void resetCounter();
	public ByteOrder getByteOrder();
	public void close() throws IOException;
}
