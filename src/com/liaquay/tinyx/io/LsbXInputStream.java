package com.liaquay.tinyx.io;

import java.io.IOException;
import java.io.InputStream;

public class LsbXInputStream extends AbstractXInputStream {

	public LsbXInputStream(final InputStream inputStream) {
		super(inputStream);
	}
	
	@Override
	public int readUnsignedShort() throws IOException {
		final int b1 = readUnsignedByte();
		final int b2 = readUnsignedByte();
		return b1 | (b2<<8);
	}

	@Override
	public int readSignedShort() throws IOException {
		final int b1 = readUnsignedByte();
		final int b2 = readUnsignedByte();
		final int s1 = b1 | (b2<<8);
		return s1 > 0x8000 ? s1 | 0xffff0000 : s1;
	}

	@Override
	public int readInt() throws IOException {
		final int b1 = readUnsignedByte();
		final int b2 = readUnsignedByte();
		final int b3 = readUnsignedByte();
		final int b4 = readUnsignedByte();
		return b1 | (b2 << 8) | (b3 << 16) | (b4 <<24);
	}

	@Override
	public ByteOrder getByteOrder() {
		return ByteOrder.LSB;
	}
}
