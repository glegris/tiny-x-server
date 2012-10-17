package com.liaquay.tinyx.io;

import java.io.IOException;

public interface XOutputStream {
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
}
