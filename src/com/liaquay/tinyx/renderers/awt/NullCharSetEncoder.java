package com.liaquay.tinyx.renderers.awt;

public class NullCharSetEncoder implements CharSetEncoder {

	public static final CharSetEncoder ENCODER = new NullCharSetEncoder();

	@Override
	public char translate(final char c) {
		return c;
	}

	@Override
	public String translate(final String s) {
		return s;
	}
}
