package com.liaquay.tinyx.renderers.awt;

public class GlyphDetail {

	int width;
	
	int descent;
	
	int ascent;
	
	char chr;
	
	public GlyphDetail(char c) {
		this.chr = c;
	}

	public char getChr() {
		return chr;
	}

	public void setChr(char chr) {
		this.chr = chr;
	}

	public int getWidth() {
		return width;
	}

	public int getDescent() {
		return descent;
	}

	public int getAscent() {
		return ascent;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setDescent(int descent) {
		this.descent = descent;
	}

	public void setAscent(int ascent) {
		this.ascent = ascent;
	}
}
