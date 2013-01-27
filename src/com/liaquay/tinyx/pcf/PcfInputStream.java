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
import java.io.InputStream;

public class PcfInputStream extends InputStream {

	private long _counter = 0;
	private final InputStream _delegate;
	
	public PcfInputStream(final InputStream delegate) {
		_delegate = delegate;
	}
	
	@Override
	public int read() throws IOException {
		final int b = _delegate.read();
		_counter += b >= 0 ? 1 : 0;
		return b;
	}

	@Override
	public int read(byte[] b) throws IOException {
		for(int p = 0; p < b.length ;) {
			final int c = _delegate.read(b, p, b.length-p);
			if(c < 0) throw new IOException("END OF STREAM!");
			p += c;
		}

		_counter += b.length;
		return b.length;
	}
	
	@Override
	 public int read(byte[] b, int off, int len) throws IOException {
		for(int p = 0; p < len ;) {
			final int c = _delegate.read(b, p+off, len-p);
			if(c < 0) throw new IOException("END OF STREAM!");
			p += c;
		}

		_counter += len;
		return len;
	 }
	
	public long getOffset() {
		return _counter;
	}
	
	public void seekAbsolute(final long pos) throws IOException {
		if(pos < _counter) throw new IOException("Sorry can't seek backwards");
		if(pos == _counter) return;
		final long offset = pos - _counter;
		final long c = _delegate.skip(offset);
		if(c != offset) throw new IOException("Could not seek to position " + pos);
		_counter = pos;
	}
	
	public int readUnsignedByte() throws IOException {
		final int d = read();
		if(d < 0) throw new IOException("END OF STREAM!");
		return d;
	}	
	
	public int readLsbInt() throws IOException {
		final int b1 = readUnsignedByte();
		final int b2 = readUnsignedByte();
		final int b3 = readUnsignedByte();
		final int b4 = readUnsignedByte();
		return b1 | (b2 << 8) | (b3 << 16) | (b4 <<24);		
	}	
}
