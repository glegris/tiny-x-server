package com.liaquay.tinyx.renderers.awt.gc.filters;

import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.renderers.awt.XawtPixmap;
import com.liaquay.tinyx.renderers.awt.gc.GraphicsContextComposite;

public class ClipFilter {

	public static boolean drawPixel(int x, int y,
			GraphicsContextComposite gc) {

		int translatedX = gc.getGC().getGraphicsOperationX() ;
		int translatedY = gc.getGC().getGraphicsOperationY() ;

		Pixmap clipMask =  gc.getGC().getClipMask();
		Pixmap stippleMask = gc.getGC().getStipple();
		Pixmap tile = gc.getGC().getTile();
		
		boolean drawPixel = false;
		if (clipMask != null) {
			drawPixel = ((XawtPixmap) clipMask.getDrawableListener()).getImage().getRGB(x,y) > 0;
		}
		
		if (stippleMask != null) {
			drawPixel |= ((XawtPixmap) stippleMask.getDrawableListener()).getImage().getRGB(x,y) > 0;
			// - translatedX + gc.getGC().getClipXOrigin(), y - translatedY + gc.getGC().getClipYOrigin()) == -1;
		}

		if (tile != null) {
			drawPixel = ((XawtPixmap) tile.getDrawableListener()).getImage().getRGB(x,y) > 0;
		}

		return drawPixel;
	}

}
