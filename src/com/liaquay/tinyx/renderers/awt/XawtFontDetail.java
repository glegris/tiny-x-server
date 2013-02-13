package com.liaquay.tinyx.renderers.awt;

import java.awt.Color;
import java.awt.Graphics2D;

import com.liaquay.tinyx.font.FontDetailAdaptor;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.FontInfo;

public abstract class XawtFontDetail extends FontDetailAdaptor {
	
	public XawtFontDetail(final String name, final FontInfo fontInfo) {
		super(name, fontInfo);
	}

	@Override
	public void drawString(
			final Drawable drawable, 
			final String text, 
			final int xs, 
			final int ys, 
			final int color) {

		final XawtDrawableListener drawableListener = (XawtDrawableListener)drawable.getDrawableListener();
		final Graphics2D graphics = drawableListener.getGraphics(null);
		final int rgb = drawable.getColorMap().getRGB(color);
		drawString(graphics, text, xs, ys, rgb);
		
		// TODO Total hack - updates should be handled by the graphics object 
		if(drawableListener instanceof XawtWindow){
			final XawtWindow xawtWindow = (XawtWindow)drawableListener;
			xawtWindow.updateCanvas();
		}
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
		
		System.out.println("Text: '" + text + "'");
		
		final XawtDrawableListener drawableListener = (XawtDrawableListener)drawable.getDrawableListener();
		final Graphics2D graphics = drawableListener.getGraphics(null);
		graphics.setColor(new Color(drawable.getColorMap().getRGB(bgColor)));
		graphics.fillRect(bx, by, bw, bh);
		final int rgb = drawable.getColorMap().getRGB(color);
		drawString(graphics, text, xs, ys, rgb);
		
		// TODO Total hack - updates should be handled by the graphics object 
		if(drawableListener instanceof XawtWindow){
			final XawtWindow xawtWindow = (XawtWindow)drawableListener;
			xawtWindow.updateCanvas();
		}
	}
	
	public abstract void drawString(
			final Graphics2D graphics, 
			final String text, 
			final int xs, 
			final int ys,
			final int color);
}
