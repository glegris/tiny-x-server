package com.liaquay.tinyx.x11font;

public class NullFontEncoding implements FontEncoding {

	public static final NullFontEncoding INSTANCE = new NullFontEncoding();
	
	@Override
	public char encode(final char c) {
		return c;
	}

	@Override
	public String encode(final String s) {
		return s;
	}

	@Override
	public char decode(final char c) {
		return c;
	}

	@Override
	public char getFirstCharacter() {
		return 0;
	}

	@Override
	public char getLastCharacter() {
		return 255;
	}
}
