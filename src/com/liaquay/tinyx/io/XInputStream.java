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
