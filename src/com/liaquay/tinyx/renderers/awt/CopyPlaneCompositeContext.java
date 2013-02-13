package com.liaquay.tinyx.renderers.awt;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import sun.java2d.loops.CompositeType;

public class CopyPlaneCompositeContext implements CompositeContext {

	ColorModel srcCM;
	ColorModel dstCM;
	Composite composite;
	CompositeType comptype;
	CopyPlaneComposite comp;
	
	
	public CopyPlaneCompositeContext(CopyPlaneComposite comp,
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
		this.comp = comp;
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

    public void compose(final Raster src, final Raster dstIn, final WritableRaster dstOut) {
        final int w = Math.min(src.getWidth(), dstIn.getWidth());
        final int h = Math.min(src.getHeight(), dstIn.getHeight());
        
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
            	
                int srcPixel = srcCM.getRGB(src.getDataElements(x, y, null));
                
                int outPixel = comp.getBgColor();
                		
                if ((srcPixel & comp.getBitplane()) > 0) {
        			outPixel = comp.getFgColor();
                }

                Object data = dstCM.getDataElements(outPixel, null);
                dstOut.setDataElements(x, y, data);
            }
        }
    }

}
