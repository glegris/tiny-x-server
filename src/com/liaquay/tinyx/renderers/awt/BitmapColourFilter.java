package com.liaquay.tinyx.renderers.awt;

import java.awt.image.RGBImageFilter;

public class BitmapColourFilter extends RGBImageFilter {

	int colourValue;
	
	public BitmapColourFilter(int colourValue) {
		canFilterIndexColorModel = true;
		this.colourValue = colourValue;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		if (x == -1) {
		}
		return rgb & colourValue;
	}

}
