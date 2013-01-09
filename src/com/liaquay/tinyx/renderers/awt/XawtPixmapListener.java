/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.renderers.awt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Image;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Window;

public class XawtPixmapListener implements Pixmap.Listener {

	final Pixmap _pixmap;

	Image _image;
	
	public XawtPixmapListener(Pixmap pixmap) {
		_pixmap = pixmap;
	}

	@Override
	public void copyArea(Drawable srcDrawable, Drawable destDrawable, GraphicsContext graphicsContext, int srcX,
			int srcY, int width, int height, int dstX, int dstY) {

		if (destDrawable instanceof Window) {
			Window w = (Window) destDrawable;
			w.renderDrawable(((Pixmap) srcDrawable).getImage(), graphicsContext, srcX, srcY, width, height, dstX, dstY);
		}
	}

	@Override
	public void putImage(GraphicsContext graphicsContext,
			byte[] buffer, int width, int height,
			int destinationX, int destinationY, int leftPad, int depth) { 

		if (depth == 1) {

			DataBufferByte db = new DataBufferByte(buffer, buffer.length);
            WritableRaster raster = Raster.createPackedRaster(db, width, height, 1, null);

            byte[] arr = {(byte)0, (byte)0xff};
            
            IndexColorModel colorModel = new IndexColorModel(1, 2, arr, arr, arr);
            
            BufferedImage image = new BufferedImage(colorModel, raster, false, null);
            
            _pixmap.setImage(image);
//            _image.setImage(image);
		}
	}


	@Override
	public com.liaquay.tinyx.model.Image createImage(Pixmap pixmap) {

		
		return _image;
	}

//	public java.awt.ImageCapabilities getAwtImage() {
//		return _image;
//	}

}
