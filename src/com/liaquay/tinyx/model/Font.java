package com.liaquay.tinyx.model;

import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.renderers.awt.GlyphDetail;


public class Font extends AbstractResource {

	private FontInfo fontName;

	private FontDetail fontDetail;
	
	public Font(final int id, final FontInfo fontName, final FontDetail fontDetail) {
		super(id);

		this.fontName = fontName;
		this.fontDetail = fontDetail;
	}

	@Override
	public void free() {
		// TODO Auto-generated method stub

	}

	public FontInfo getFontInfo() {
		return fontName;
	}

//	public void setFontName(FontString fontName) {
//		this.fontName = fontName;
//	}

	
	public int getMaxAscent() {
		return fontDetail.getMaxAscent();
	}

	public int getMaxWidth() {
		return fontDetail.getMaxWidth();
}

	public int getMaxDescent() {
		return fontDetail.getMaxDescent();
	}

	public int getMinWidth() {
		return fontDetail.getMinWidth();
	}

	public int getDefaultChar() {
		return fontDetail.getDefaultChar();
	}

	public int getFirstChar() {
		return fontDetail.getFirstChar();
	}

	public int getLastChar() {
		return fontDetail.getLastChar();
	}

	public GlyphDetail getGlyphDetail(int i) {
		return fontDetail.getGlyphDetail(i);
	}
	
	
}
