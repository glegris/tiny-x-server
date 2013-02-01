package com.liaquay.tinyx.renderers.awt;

public interface CharSetEncoder {
	public char translate(final char c);
	public String translate(final String s);
}
