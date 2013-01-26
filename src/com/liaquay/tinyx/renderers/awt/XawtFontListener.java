package com.liaquay.tinyx.renderers.awt;

import java.awt.Graphics2D;

import com.liaquay.tinyx.model.Font;

public interface XawtFontListener extends Font.Listener {
	
	public void drawString(
			final Graphics2D graphics, 
			final String text, 
			final int xs, 
			final int ys,
			final int color);
}
