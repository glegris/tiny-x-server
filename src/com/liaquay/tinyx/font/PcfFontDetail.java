package com.liaquay.tinyx.font;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.TextExtents;
import com.liaquay.tinyx.model.font.FontDetailAdaptor;
import com.liaquay.tinyx.pcf.PcfFont;
import com.liaquay.tinyx.pcf.PcfMetrics;

public abstract class PcfFontDetail extends FontDetailAdaptor {

	private final PcfFont _font;
	private final TextExtents _minBounds;
	private final TextExtents _maxBounds;
	
	public PcfFontDetail(
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
	
	public PcfFont getPcfFont() {
		return _font;
	}
}
