package com.liaquay.tinyx.model.font;

import java.util.HashMap;
import java.util.Map;

import com.liaquay.tinyx.renderers.awt.GlyphDetail;

public class FontDetail {

	int maxAscent;

	int maxDescent;

	int minWidth;

	int maxWidth;

	int defaultChar;

	int firstChar;

	int lastChar;

	int height;

	int leading;
	
	Map<Integer, GlyphDetail> glyphDetails = new HashMap<Integer, GlyphDetail>();

	public FontDetail() {
		//TODO: Try and get the first and last character for this font from somewhere more meaningful
		setFirstChar(32);
		setLastChar(255);
	}

	public void setMaxAscent(int maxAscent) {
		this.maxAscent = maxAscent;
	}

	public int getMaxAscent() {
		return this.maxAscent;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public int getMaxWidth() {
		return this.maxWidth;
	}

	public void setMaxDescent(int maxDescent) {
		this.maxDescent = maxDescent;
	}

	public int getMaxDescent() {
		return this.maxDescent;
	}

	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	public int getMinWidth() {
		return this.minWidth;
	}

	public void setDefaultChar(int defaultChar) {
		this.defaultChar = defaultChar;
	}

	public int getDefaultChar() {
		return defaultChar;
	}

	public int getFirstChar() {
		return firstChar;
	}

	public void setFirstChar(int firstChar) {
		this.firstChar = firstChar;
	}

	public int getLastChar() {
		return lastChar;
	}

	public void setLastChar(int lastChar) {
		this.lastChar = lastChar;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}		

	public int getLeading() {
		return leading;
	}


	public void setLeading(int leading) {
		this.leading = leading;
	}


	public GlyphDetail getGlyphDetail(int i) {
		return glyphDetails.get(i);
	}


	public void addGlyph(GlyphDetail gd) {
		glyphDetails.put((int) gd.getChr(), gd);
	}


}
