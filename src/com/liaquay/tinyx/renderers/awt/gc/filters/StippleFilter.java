package com.liaquay.tinyx.renderers.awt.gc.filters;

import java.awt.image.BufferedImage;

import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.renderers.awt.XawtDrawableListener;
import com.liaquay.tinyx.renderers.awt.gc.GraphicsContextComposite;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.FillStyle.FillStyleType;

public class StippleFilter {

	public static int process(int x, int y, GraphicsContextComposite gc) {
		int drawPixel = 0x00000000;
//
//		if (gc.getGC().getStipple() != null && (FillStyleType.getFromIndex(gc.getGC().getFillStyle()) == FillStyleType.Stippled)) {
//			if (srcPixel == gc.getGC().getBackgroundColour()) {
//				srcPixel = gc.getGC().getForegroundColour();
//			}
//		}

		
		Pixmap stipplePixmap = gc.getGC().getStipple();

		if (stipplePixmap != null) {
			if (FillStyleType.getFromIndex(gc.getGC().getFillStyle()) == FillStyleType.Stippled) {

			final XawtDrawableListener awtDrawable = (XawtDrawableListener) stipplePixmap.getDrawableListener();
			final BufferedImage srcImage = awtDrawable.getImage();

			//TODO:  Incorporate stipple origins
			int stipplePixel = srcImage.getRGB(x, y);
			if (stipplePixel != -1) {
				// draw
				drawPixel = gc.getGC().getForegroundColour();
			} else {
				// don't
				drawPixel = gc.getGC().getBackgroundColour();
			}

//			TexturePaint tp = new TexturePaint(output, new Rectangle(graphicsContext.getTileStippleXOrigin(), graphicsContext.getTileStippleYOrigin(), stipplePixmap.getWidth(), stipplePixmap.getHeight()));
//			graphics.setPaint(tp);
			}
		} 
		return drawPixel;
	}

}
