package com.liaquay.tinyx.renderers.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.TextExtents;

public class XawtNativeFontDetail extends XawtFontDetail {

	private final Font _font;
	private final TextExtents _minBounds = new TextExtents( // TODO get these from the font
			0, // Ascent
			0, // Descent
			0, // width
			0, // left
			0, // right,
			0  // attributes
			);
	private final TextExtents _maxBounds;
	private final int _ascent;
	private final int _descent;
	
	public XawtNativeFontDetail(
			final FontInfo fontInfo, 
			final Font font) {
		super(fontInfo.toString(), fontInfo);
		
		_font = font;
		
		final FontMetrics fm = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(font);

		_maxBounds = new TextExtents(
					fm.getMaxAscent(), // Ascent
					fm.getMaxDescent(), // Descent
					fm.getMaxAdvance(), // width
					0, // left
					fm.getMaxAdvance(), // right,
					0  // attributes
					);
		
		_ascent = fm.getAscent();
		_descent = fm.getDescent();
	}

	@Override
	public TextExtents getMinBounds() {
		return _minBounds;
	}

	@Override
	public TextExtents getMaxBounds() {
		return _maxBounds;
	}

	@Override
	public int getDefaultChar() {
		return 42; // TODO extract from font;
	}

	@Override
	public int getFirstChar() {
		return 32;//TODO: Try and get the first and last character for this font from somewhere more meaningful
	}

	@Override
	public int getLastChar() {
		return 255;//TODO: Try and get the first and last character for this font from somewhere more meaningful
	}

	@Override
	public boolean isLeftToRight() {
		return true; //  TODO extract from font
	}

	@Override
	public int getAscent() {
		return _ascent;
	}

	@Override
	public int getDescent() {
		return _descent;
	}
	
	private final char[] _buffer = new char[1];
	private final FontRenderContext _awtFontRenderContext = new FontRenderContext(null, true, false);

	@Override
	public TextExtents getTextExtents(final int character) {
		_buffer[0] = (char)character;
		
		final Rectangle2D bounds = _font.getStringBounds(_buffer, 0, 1, _awtFontRenderContext);
		final LineMetrics lm = _font.getLineMetrics(_buffer, 0, 1, _awtFontRenderContext);

		return new TextExtents(
				(int) lm.getAscent(),
				(int) lm.getDescent(),
				(int) bounds.getWidth(),
				(int) 0, // Left
				(int) bounds.getWidth(),// Right 
				0); // Attributes 	
		}

	@Override
	public TextExtents getTextExtents(final String t) {
		
		final Rectangle2D bounds = _font.getStringBounds(t, 0, t.length(), _awtFontRenderContext);
		final LineMetrics lm = _font.getLineMetrics(t, 0, t.length(), _awtFontRenderContext);

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
			final String t, 
			final int xs, 
			final int ys,
			final int color) {
		
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		graphics.setColor(new Color(color)); // TODO slow
		graphics.setFont(_font);
		graphics.drawString(t, xs, ys);
	}
}
