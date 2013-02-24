package com.liaquay.tinyx.renderers.awt.gc.filters;

import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.renderers.awt.XawtPixmap;
import com.liaquay.tinyx.renderers.awt.gc.GraphicsContextComposite;

public class ClipFilter {

	public static boolean drawPixel(int x, int y,
			GraphicsContextComposite gc) {

		int translatedX = gc.getDrawable().getX() + x;
		int translatedY = gc.getDrawable().getY() + y;

		Pixmap clipMask =  gc.getGC().getClipMask();

		if (clipMask != null) {
			if (translatedX >= gc.getGC().getClipXOrigin() &&  translatedX < gc.getGC().getClipXOrigin() + clipMask.getWidth()) {
				if (translatedY >= gc.getGC().getClipYOrigin() &&  translatedY < gc.getGC().getClipYOrigin() + clipMask.getHeight()) {
					return ((XawtPixmap) clipMask.getDrawableListener()).getImage().getRGB(x, y) == 0;
				}
			}
		}

		return true;
	}

}
