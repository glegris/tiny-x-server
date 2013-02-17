package com.liaquay.tinyx.renderers.awt;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

import com.liaquay.tinyx.model.GraphicsContext;

public class GraphicsContextComposite implements Composite {

	GraphicsContext gc;
	
	public GraphicsContextComposite(final GraphicsContext gc) {
		this.gc = gc;
	}
	
	public GraphicsContext getGC() {
		return this.gc;
	}
	
	@Override
	public CompositeContext createContext(ColorModel srcColorModel,
			ColorModel dstColorModel, RenderingHints hints) {

		return new GraphicsContextCompositeContext(this, srcColorModel, dstColorModel);
	}
}
