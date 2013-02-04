package com.liaquay.tinyx.renderers.awt;

public abstract class CharSetEncoderAdaptor implements CharSetEncoder {

	@Override
	public String translate(final String s) {
		final char[] c = new char[s.length()];
		for(int i =0; i < c.length; ++i) c[i] = translate(s.charAt(i));
		return new String(c);
	}
}
