package com.liaquay.tinyx.renderers.awt.gc;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import sun.java2d.loops.CompositeType;

import com.liaquay.tinyx.renderers.awt.XawtDrawableListener;
import com.liaquay.tinyx.renderers.awt.gc.filters.FunctionFilter;

public class GraphicsContextCompositeContext implements CompositeContext {

	ColorModel srcCM;
	ColorModel dstCM;
	Composite composite;
	CompositeType comptype;
	GraphicsContextComposite gc;

	public GraphicsContextCompositeContext(GraphicsContextComposite gc,
			ColorModel s, ColorModel d)
	{
		if (s == null) {
			throw new NullPointerException("Source color model cannot be null");
		}
		if (d == null) {
			throw new NullPointerException("Destination color model cannot be null");
		}
		srcCM = s;
		dstCM = d;
		this.gc = gc;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * This method composes the two source tiles
	 * and places the result in the destination tile. Note that
	 * the destination can be the same object as either
	 * the first or second source.
	 * @param src1 The first source tile for the compositing operation.
	 * @param src2 The second source tile for the compositing operation.
	 * @param dst The tile where the result of the operation is stored.
	 */
	public void compose(final Raster src, final Raster dstIn, final WritableRaster dstOut) {
		final int w = Math.min(src.getWidth(), dstIn.getWidth());
		final int h = Math.min(src.getHeight(), dstIn.getHeight());

		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {

				// Remove the alpha information
				int srcPixel =  srcCM.getRGB(src.getDataElements(x, y, null));
				int destPixel = dstCM.getRGB(dstIn.getDataElements(x, y, null));

				// Do we draw the pixel?
				boolean drawPixel = true;
				
//				if ((gc.getSupportedModes() & XawtDrawableListener.GCClipMask) > 0) {
//					drawPixel = ClipFilter.drawPixel(x, y, gc);
//				}

				int outPixel = srcPixel;

				if (drawPixel) {

//					if () {
//						int stipplePixel = StippleFilter.process(x, y, gc);
//						if (stipplePixel > 0) {
//							outPixel = stipplePixel;
//						}
//					}

					if ((gc.getSupportedModes() & XawtDrawableListener.GCFunction) > 0) {
						// Process the function
						outPixel = FunctionFilter.processPixel(outPixel, destPixel, x, y, gc);
					}

					outPixel =  (outPixel & gc.getGC().getPlaneMask()) | (destPixel & ~gc.getGC().getPlaneMask());
				} else {
					outPixel = 0xffff0000;
				}

				Object data = dstCM.getDataElements(outPixel, null);
				dstOut.setDataElements(x, y, data);
			}
		}
	}

}
