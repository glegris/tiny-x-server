package com.liaquay.tinyx.renderers.awt.gc;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;

public class GraphicsContextComposite implements Composite {

	GraphicsContext gc;
	Drawable drawable;
	int _supportedModes;

	public GraphicsContextComposite(final GraphicsContext gc, int supportedModes, Drawable drawable) {
		this._supportedModes = supportedModes;
		this.gc = gc;
		this.drawable = drawable;
	}

	public GraphicsContext getGC() {
		return this.gc;
	}
	
	public Drawable getDrawable() {
		return this.drawable;
	}

	public int getSupportedModes() {
		return this._supportedModes;
	}
	
	@Override
	public CompositeContext createContext(ColorModel srcColorModel,
			ColorModel dstColorModel, RenderingHints hints) {

		return new GraphicsContextCompositeContext(this, srcColorModel, dstColorModel);
	}
}
