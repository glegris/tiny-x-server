package com.liaquay.tinyx.font;

public abstract class FontEncodingAdaptor implements FontEncoding {

	@Override
	public String translate(final String s) {
		final char[] c = new char[s.length()];
		for(int i =0; i < c.length; ++i) c[i] = translate(s.charAt(i));
		return new String(c);
	}
}
