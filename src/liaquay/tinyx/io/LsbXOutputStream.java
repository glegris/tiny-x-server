package com.liaquay.tinyx.io;

import java.io.IOException;
import java.io.OutputStream;

public class LsbXOutputStream extends AbstractXOutputStream {

	public LsbXOutputStream(final OutputStream outputStream) {
		super(outputStream);
	}

	@Override
	public void writeShort(final int s) throws IOException {
		final int b1 = s & 0xff;
		final int b2 = (s>>8) & 0xff;
		writeByte(b1);
		writeByte(b2);
	}

	@Override
	public void writeInt(final int i) throws IOException {
		final int b1 = i & 0xff;
		final int b2 = (i>>8) & 0xff;
		final int b3 = (i>>16) & 0xff;
		final int b4 = (i>>24) & 0xff;
		writeByte(b1);
		writeByte(b2);
		writeByte(b3);
		writeByte(b4);
	}

	@Override
	public ByteOrder getByteOrder() {
		return ByteOrder.LSB;
	}
}
