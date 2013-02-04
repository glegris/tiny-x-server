package com.liaquay.tinyx.x11font;

public interface FontEncoding {
	public char encode(final char c);
	public String encode(final String s);
	public char decode(final char c);
	public char getFirstCharacter();
	public char getLastCharacter();
}
