package com.liaquay.tinyx.renderers.awt;

import java.awt.Color;
import java.awt.Graphics2D;

import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.TextExtents;
import com.liaquay.tinyx.pcf.PcfBitmaps;
import com.liaquay.tinyx.pcf.PcfFont;
import com.liaquay.tinyx.pcf.PcfMetrics;

public class XawtPcfFontListener implements XawtFontListener {
	
	private final PcfFont _pcfFont;
	
	public XawtPcfFontListener(final PcfFont pcfFont) {
		_pcfFont = pcfFont;
	}

	@Override
	public void fontClosed(final Font font) {
	}

	@Override
	public TextExtents getTextExtents(final int character) {
		return getTextExtents("" + character);		
	}

	@Override
	public TextExtents getTextExtents(final String text) {
		return pcfMetricsToTextExtents(_pcfFont.stringMetrics(text));
	}
	
	private final TextExtents pcfMetricsToTextExtents(final PcfMetrics pcfMetrics) {
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
		_pcfFont.drawString(new PcfBitmaps.Renderer(){ // TODO only make one of these
			@Override
			public void render(int x, int y) {
				graphics.drawRect(x, y, 0, 0); // TODO slow
			}
			
		}, text, xs, ys);
	}
}
