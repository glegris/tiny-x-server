package com.liaquay.tinyx.renderers.awt;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import com.liaquay.tinyx.requesthandlers.gcattribhandlers.FillStyle.FillStyleType;

import sun.java2d.loops.CompositeType;

public class GraphicsContextCompositeContext implements CompositeContext {

	ColorModel srcCM;
	ColorModel dstCM;
	Composite composite;
	CompositeType comptype;
	GraphicsContextComposite gc;

	final static int PRECBITS = 22;
	final static int WEIGHT_R = (int) ((1 << PRECBITS) * 0.299); 
	final static int WEIGHT_G = (int) ((1 << PRECBITS) * 0.578);
	final static int WEIGHT_B = (int) ((1 << PRECBITS) * 0.114);
	final static int SRCALPHA = (int) ((1 << PRECBITS) * 0.667);

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

				int srcPixel = srcCM.getRGB(src.getDataElements(x, y, null));

				if (gc.getGC().getStipple() != null && (FillStyleType.getFromIndex(gc.getGC().getFillStyle()) == FillStyleType.Stippled)) {
					if (srcPixel == gc.getGC().getBackgroundColour()) {
						srcPixel = gc.getGC().getForegroundColour();
					}
				}

				int destPixel = dstCM.getRGB(dstIn.getDataElements(x, y, null));

				int outPixel = 0;

				switch (gc.getGC().getFunction()) {
				case Clear:
					outPixel = 0;
					break;
				case And:
					outPixel = srcPixel & destPixel;
					break;
				case AndReverse:
					outPixel = srcPixel & ~destPixel;
					break;
				case Copy:
					outPixel = srcPixel;
					break;
				case AndInverted:
					outPixel = (~srcPixel) & destPixel;
					break;
				case NoOp:
					outPixel = destPixel;
					break;
				case Xor:
					outPixel = srcPixel ^ destPixel;
					break;
				case Or:
					outPixel = srcPixel | destPixel;
					break;
				case Nor:
					outPixel = ~srcPixel & ~destPixel;
					break;
				case Equiv:
					outPixel = ~srcPixel ^ destPixel;
					break;
				case Invert:
					outPixel = ~destPixel;
					break;
				case OrReverse:
					outPixel = srcPixel | ~destPixel;
					break;
				case CopyInverted:
					outPixel = ~srcPixel;
					break;
				case OrInverted:
					outPixel = ~srcPixel | destPixel;
					break;
				case Nand:
					outPixel = ~srcPixel | ~destPixel;
					break;
				case Set:
					outPixel = 0xffffffff;
					break;
				}

				outPixel = (outPixel & gc.getGC().getPlaneMask()) | (destPixel & ~gc.getGC().getPlaneMask());

				Object data = dstCM.getDataElements(outPixel, null);
				dstOut.setDataElements(x, y, data);
			}
		}
	}

}
