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

//		if (clipMask != null) {
//			return ((XawtPixmap) clipMask.getDrawableListener()).getImage().getRGB(x - translatedX + gc.getGC().getClipXOrigin(), y - translatedY + gc.getGC().getClipYOrigin()) == -1;
//		}

		return true;
	}

}
