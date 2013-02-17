package com.liaquay.tinyx.renderers.awt;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

import com.liaquay.tinyx.model.GraphicsContext;

public class CopyPlaneComposite implements Composite {

	int bitplane;
	
	int fgColor;
	int bgColor;
	
	public CopyPlaneComposite(final int bitplane, final int foregroundColor, final int backgroundColor) {
		this.bitplane = bitplane;
		this.fgColor = foregroundColor;
		this.bgColor = backgroundColor;
	}
	
	@Override
	public CompositeContext createContext(ColorModel srcColorModel,
			ColorModel dstColorModel, RenderingHints hints) {

		return new CopyPlaneCompositeContext(this, srcColorModel, dstColorModel);
	}

	public int getBitplane() {
		return bitplane;
	}

	public void setBitplane(int bitplane) {
		this.bitplane = bitplane;
	}

	public int getFgColor() {
		return fgColor;
	}

	public void setFgColor(int fgColor) {
		this.fgColor = fgColor;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}
	
	
}
