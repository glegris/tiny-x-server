package com.liaquay.tinyx.renderers.awt;

import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.TextExtents;

public class XawtFontListener implements Font.Listener {
	
	final java.awt.Font _awtFont;
	final FontRenderContext _awtFontRenderContext = new FontRenderContext(null, true, true);

	@Override
	public void fontClosed(final Font font) {
		// TODO Close the font?
	}

	public XawtFontListener(final java.awt.Font awtFont) {
		_awtFont = awtFont;
	}
	
	public java.awt.Font getAwtFont() {
		return _awtFont;
	}

	private char[] _buffer = new char[1];
	
	@Override
	public TextExtents getTextExtents(final int character) {
		_buffer[0] = (char)character;
		
		final Rectangle2D bounds = _awtFont.getStringBounds(_buffer, 0, 1, _awtFontRenderContext);
		final LineMetrics lm = _awtFont.getLineMetrics(_buffer, 0, 1, _awtFontRenderContext);

		return new TextExtents(
				(int) lm.getAscent(),
				(int) lm.getDescent(),
				(int) bounds.getWidth(),
				(int) bounds.getHeight(),
				(int) lm.getLeading(),
				(int) bounds.getWidth()); // Trailing 
	}

	@Override
	public TextExtents getTextExtents(final String text) {
		
		final Rectangle2D bounds = _awtFont.getStringBounds(text, 0, text.length(), _awtFontRenderContext);
		final LineMetrics lm = _awtFont.getLineMetrics(text, 0, text.length(), _awtFontRenderContext);

		return new TextExtents(
				(int) lm.getAscent(),
				(int) lm.getDescent(),
				(int) bounds.getWidth(),
				(int) bounds.getHeight(),
				(int) lm.getLeading(),
				(int) bounds.getWidth()); // Trailing 
	}
}
