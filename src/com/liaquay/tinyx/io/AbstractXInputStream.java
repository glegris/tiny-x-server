package com.liaquay.tinyx.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractXInputStream implements XInputStream {

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
	
	private byte[] _stringBuffer = new byte[64];
	
	public String readString() throws IOException {
		final int length = readUnsignedShort();
	    skip(2);
	    final byte[] buffer;
	    if(length <= _stringBuffer.length) {
	    	buffer = _stringBuffer;
	    }
	    else {
	    	buffer = new byte[length];
	    }
	    read(buffer, 0, length);
	    skip((-length) & 3);
	    return new String(buffer, 0, length);
	}
}

