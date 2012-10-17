package com.liaquay.tinyx.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public abstract class AbstractXOutputStream implements XOutputStream {
	
	private final OutputStream _outputStream;
	private int _counter = 0;
	
	@Override
	public final int getCounter() {
		return _counter;
	}
	
	@Override
	public final void resetCounter() {
		_counter = 0;
	}
	
	public AbstractXOutputStream(final OutputStream outputStream) {
		_outputStream = outputStream;
	}
	
	@Override
	public final void writeByte(final byte b) throws IOException {
		_counter++;
		_outputStream.write(((int)b) &0xff);
	}

	@Override
	public final void writeByte(final int i) throws IOException {
		_counter++;
		_outputStream.write(i);
	}
	
	@Override
	public final void write(final byte[] data, final int start, final int length) throws IOException {
		_counter+= length;
		_outputStream.write(data, start, length);
	}
	
	@Override
	public void send() throws IOException {
		_outputStream.flush();
	}
	
	@Override
	public void writePad(final int i) throws IOException {
		for(int j = 0; j< i; j++) writeByte(0);
	}
}
