package com.liaquay.tinyx.model;


public class Font extends AbstractResource {

	private FontString fontName;

	private FontCharacterDetails chars[];
	
	public Font(int id, FontString fontName) {
		super(id);

		this.fontName = fontName;
	}

	@Override
	public void free() {
		// TODO Auto-generated method stub

	}

	public FontString getFontName() {
		return fontName;
	}

	public void setFontName(FontString fontName) {
		this.fontName = fontName;
	}

	
	public int getMaxAscent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxDescent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMinWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDefaultChar() {
		return 32;
	}
	
	
}
