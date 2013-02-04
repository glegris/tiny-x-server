package com.liaquay.tinyx.font;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.TextExtents;
import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.x11font.FontEncoding;

public class EncodingFontDetail implements FontDetail {

	private final FontDetail _delegate;
	private final FontEncoding _mapping;
	private final int _defaultChar;
	private final char _firstChar;
	private final char _lastChar;
	
	public EncodingFontDetail(
			final FontDetail delegate,
			final FontEncoding mapping) {
		
		_delegate = delegate;
		_mapping = mapping;
		_defaultChar = mapping.decode((char)delegate.getDefaultChar()) &0xffff;
		_firstChar = mapping.getFirstCharacter();
		_lastChar = mapping.getLastCharacter();
	}

	public String getName() {
		return _delegate.getName();
	}

	public String getFoundry() {
		return _delegate.getFoundry();
	}

	public String getFamilyName() {
		return _delegate.getFamilyName();
	}

	public String getWeightName() {
		return _delegate.getWeightName();
	}

	public String getSlant() {
		return _delegate.getSlant();
	}

	public String getWidthName() {
		return _delegate.getWidthName();
	}

	public String getAddStyleName() {
		return _delegate.getAddStyleName();
	}

	public int getPixelSize() {
		return _delegate.getPixelSize();
	}

	public int getPointSize() {
		return _delegate.getPointSize();
	}

	public int getResolutionX() {
		return _delegate.getResolutionX();
	}

	public int getResolutionY() {
		return _delegate.getResolutionY();
	}

	public String getSpacing() {
		return _delegate.getSpacing();
	}

	public int getAverageWidth() {
		return _delegate.getAverageWidth();
	}

	public String getCharsetRegistry() {
		return _delegate.getCharsetRegistry();
	}

	public String getCharsetEncoding() {
		return _delegate.getCharsetEncoding();
	}

	public TextExtents getMinBounds() {
		return _delegate.getMinBounds();
	}

	public TextExtents getMaxBounds() {
		return _delegate.getMaxBounds();
	}

	public int getDefaultChar() {
		return _defaultChar;
	}

	public int getFirstChar() {
		return _firstChar &0xffff;
	}

	public int getLastChar() {
		return _lastChar &0xffff;
	}

	public boolean isLeftToRight() {
		return _delegate.isLeftToRight();
	}

	public int getAscent() {
		return _delegate.getAscent();
	}

	public int getDescent() {
		return _delegate.getDescent();
	}

	public TextExtents getTextExtents(final int character) {
		return _delegate.getTextExtents(_mapping.encode((char)character));
	}

	public TextExtents getTextExtents(final String text) {
		return _delegate.getTextExtents(text);
	}

	@Override
	public void drawString(
			final Drawable drawable, 
			final String text, 
			final int xs, 
			final int ys, 
			final int color) {
		_delegate.drawString(drawable, _mapping.encode(text), xs, ys, color);
	}

	@Override
	public void drawString(
			final Drawable drawable, 
			final String text, 
			final int xs, 
			final int ys, 
			final int color, 
			final int bx, 
			final int by,
			final int bw, 
			final int bh, 
			final int bgColor) {
		_delegate.drawString(drawable, _mapping.encode(text), xs, ys, color, bx, by, bw, bh, bgColor);
	}
}
