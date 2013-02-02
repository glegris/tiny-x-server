package com.liaquay.tinyx.x11font;

public abstract class FontEncodingAdaptor implements FontEncoding {
	@Override
	public String encode(final String s) {
		final char[] c = new char[s.length()];
		for(int i =0; i < c.length; ++i) c[i] = encode(s.charAt(i));
		return new String(c);
	}
}
