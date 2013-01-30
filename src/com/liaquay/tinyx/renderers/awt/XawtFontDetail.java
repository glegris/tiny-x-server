package com.liaquay.tinyx.renderers.awt;

import java.awt.Graphics2D;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.font.FontDetailAdaptor;

public abstract class XawtFontDetail extends FontDetailAdaptor {
	
	public XawtFontDetail(final String name, final FontInfo fontInfo) {
		super(name, fontInfo);
	}

	public abstract void drawString(
			final Graphics2D graphics, 
			final String text, 
			final int xs, 
			final int ys,
			final int color);
}
