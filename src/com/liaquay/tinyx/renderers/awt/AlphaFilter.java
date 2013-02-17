package com.liaquay.tinyx.renderers.awt;

import java.awt.image.RGBImageFilter;

import com.liaquay.tinyx.model.GraphicsContext;

public class AlphaFilter extends RGBImageFilter {

	GraphicsContext context;

	public AlphaFilter(GraphicsContext context) {
		canFilterIndexColorModel = true;
		
		this.context = context;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		if (rgb == context.getBackgroundColour()) {
			return 0xff000000 | context.getForegroundColour();
		}
		return 0x00000000;
	}

}
