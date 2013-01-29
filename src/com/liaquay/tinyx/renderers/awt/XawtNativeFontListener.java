package com.liaquay.tinyx.renderers.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.TextExtents;

public class XawtNativeFontListener implements XawtFontListener {
	
	final java.awt.Font _awtFont;
	final FontRenderContext _awtFontRenderContext = new FontRenderContext(null, true, false);

	@Override
	public void fontClosed(final Font font) {
	}

	public XawtNativeFontListener(final FontInfo fontInfo) {
		
		final String fontFamily = fontInfo.getFamilyName();
		final int fontSize = fontInfo.getPixelSize();
		final String fontWeight = fontInfo.getWeightName();
		final String fontSlant = fontInfo.getSlant();
		final int fontStyle = 
				((fontWeight != null && fontWeight.equalsIgnoreCase("bold")) ? java.awt.Font.BOLD : 0) |
				((fontSlant != null && fontSlant.equalsIgnoreCase("i")) ? java.awt.Font.ITALIC : 0);
		
		_awtFont = new java.awt.Font(fontFamily, fontStyle, fontSize == 0 ? 12 : fontSize);
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
				(int) 0, // Left
				(int) bounds.getWidth(),// Right 
				0); // Attributes 
	}

	@Override
	public TextExtents getTextExtents(final String text) {
		
		final Rectangle2D bounds = _awtFont.getStringBounds(text, 0, text.length(), _awtFontRenderContext);
		final LineMetrics lm = _awtFont.getLineMetrics(text, 0, text.length(), _awtFontRenderContext);

		return new TextExtents(
				(int) lm.getAscent(),
				(int) lm.getDescent(),
				(int) bounds.getWidth(),
				(int) 0, // Left
				(int) bounds.getWidth(), // Right 
				0); // Attributes 
	}

	@Override
	public void drawString(
			final Graphics2D graphics, 
			final String text, 
			final int xs, 
			final int ys,
			final int color) {
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(color)); // TODO slow
		graphics.setFont(_awtFont);
		graphics.drawString(text, xs, ys);
	}
}
