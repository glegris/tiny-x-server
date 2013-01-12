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
import java.util.logging.Logger;

import com.liaquay.tinyx.Connection;

public abstract class AbstractXInputStream implements XInputStream {

	private final static Logger LOGGER = Logger.getLogger(AbstractXInputStream.class.getName());

	private final InputStream _inputStream;
	private int _counter = 0;

	@Override
	public final int getCounter() {
		return _counter;
	}

	@Override
	public final void resetCounter() {
		_counter = 0;
	}

	public AbstractXInputStream(final InputStream inputStream) {
		_inputStream = inputStream;
	}

	@Override
	public final int readUnsignedByte() throws IOException {
		final int d = _inputStream.read();
		if(d < 0) throw new IOException("END OF STREAM!");
		_counter++;
		return d;
	}

	@Override
	public final int readSignedByte() throws IOException {
		final int d = _inputStream.read();
		if(d < 0) throw new IOException("END OF STREAM!");
		_counter++;
		return d > 0x80 ? d | 0xffffff00 : d;
	}

	@Override
	public final int read(byte[] data, int start, int length) throws IOException {
		_counter+= length;
		return _inputStream.read(data, start, length);
	}

	@Override
	public final int readNibbles(byte[] data, int start, int length) throws IOException {
		int v = 0;
		for(int i = 0; i < length; ++i) {
			if((i&1) == 0) {
				v = readUnsignedByte();
				_counter++;
				data[i] = (byte)(v & 0x0f);
			}
			else {
				data[i] = (byte)(v >> 4);				
			}
		}
		return 0;
	}

	@Override
	public final void skip(final int length) throws IOException {
		_inputStream.skip(length);
		_counter+=length;
	}

	@Override
	public final void close() throws IOException {
		_inputStream.close();
	}

	private byte[] _stringBuffer = new byte[64];
	
	public String readString() throws IOException {
		final int length = readUnsignedShort();
	    skip(-_counter & 3);
	    final String text = readString(length);
	    skip((-length) & 3);
	    return text;
	}
	
	public String readString(final int length) throws IOException {
	    final byte[] buffer;
	    if(length <= _stringBuffer.length) {
	    	buffer = _stringBuffer;
	    }
	    else {
	    	buffer = new byte[length];
	    }
	    read(buffer, 0, length);
	    return new String(buffer, 0, length);
	}
	
	public String readString16() throws IOException {
		final int length = readUnsignedShort();
	    skip(-_counter & 3);
	    final String text = readString16(length);
	    skip((-length) & 3);
	    return text;
	}
	
	public String readString16(final int length) throws IOException {
	    final char[] buffer = new char[length];;
	    for(int i = 0; i < length; ++i) {
			final int ah = readUnsignedByte();
			final int al = readUnsignedByte();
	    	buffer[i] = (char) (ah<<8 | al);
	    }
	    return new String(buffer, 0, length);
	}	
	
	public int readInlineFontId() throws IOException {
		final int b4 = readUnsignedByte();
		final int b3 = readUnsignedByte();
		final int b2 = readUnsignedByte();
		final int b1 = readUnsignedByte();
		return b1 | (b2 << 8) | (b3 << 16) | (b4 <<24);
	}
}

