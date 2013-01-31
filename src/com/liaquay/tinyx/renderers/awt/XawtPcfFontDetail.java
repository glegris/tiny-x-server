package com.liaquay.tinyx.renderers.awt;

import java.awt.Color;
import java.awt.Graphics2D;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.TextExtents;
import com.liaquay.tinyx.pcf.PcfBitmaps;
import com.liaquay.tinyx.pcf.PcfFont;
import com.liaquay.tinyx.pcf.PcfMetrics;

public class XawtPcfFontDetail extends XawtFontDetail {

	private final PcfFont _font;
	private final TextExtents _minBounds;
	private final TextExtents _maxBounds;
	
	public XawtPcfFontDetail(
			final String name,
			final PcfFont font,
			final FontInfo fontInfo) {
		super(name, fontInfo);
		
		_font = font;
		
		_minBounds = pcfMetricsToTextExtents(font.getMinBounds());
		_maxBounds = pcfMetricsToTextExtents(font.getMaxBounds());
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
		return _font.getDefaultCharacter();
	}

	@Override
	public int getFirstChar() {
		return _font.getMinCharacter();
	}

	@Override
	public int getLastChar() {
		return _font.getMaxCharacter();
	}

	@Override
	public boolean isLeftToRight() {
		return _font.isDrawLeftToRight();
	}

	@Override
	public int getAscent() {
		return _font.getAscent();
	}

	@Override
	public int getDescent() {
		return _font.getDescent();
	}

	@Override
	public TextExtents getTextExtents(final int character) {
		return getTextExtents("" + character);		
	}

	@Override
	public TextExtents getTextExtents(final String text) {
		return pcfMetricsToTextExtents(_font.stringMetrics(text));
	}
	
	private static final TextExtents pcfMetricsToTextExtents(final PcfMetrics pcfMetrics) {
		return new TextExtents(
				pcfMetrics.getAscent(), // Ascent
				pcfMetrics.getDescent(), // Descent
				pcfMetrics.getWidth(), // width
				pcfMetrics.getLeftSideBearing(), // left
				pcfMetrics.getRightSideBearing(), // right,
				pcfMetrics.getAttributes()  // attributes
				);
	}
	
	@Override
	public void drawString(
			final Graphics2D graphics, 
			final String text, 
			final int xs, 
			final int ys,
			final int color) {
		
		graphics.setColor(new Color(color)); // TODO slow
		_font.drawString(new PcfBitmaps.Renderer(){ // TODO only make one of these
			@Override
			public void render(int x, int y) {
				graphics.drawRect(x, y, 0, 0); // TODO slow
			}
			
		}, text, xs, ys);
	}

}
