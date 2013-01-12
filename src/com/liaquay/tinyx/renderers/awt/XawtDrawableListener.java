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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;

public abstract class XawtDrawableListener implements Drawable.Listener {

	public abstract Drawable getDrawable();
	
	@Override
	public void copyArea(Drawable destDrawable, GraphicsContext graphicsContext, int srcX,
			int srcY, int width, int height, int dstX, int dstY) {
		
		java.awt.Image destGraphics = ((XawtDrawableListener) destDrawable.getListener()).getImage();

		java.awt.Image srcGraphics = ((XawtDrawableListener) getDrawable().getListener()).getImage();

		srcGraphics.getGraphics().translate(dstX,  dstY);
		srcGraphics.getGraphics().drawImage(destGraphics, width, height, null);
	}

	@Override
	public void putImage(GraphicsContext graphicsContext,
			byte[] buffer, int width, int height,
			int destinationX, int destinationY, int leftPad, int depth) { 

		if (depth == 1) {
			DataBufferByte db = new DataBufferByte(buffer, buffer.length);
			WritableRaster raster = Raster.createPackedRaster(db, width, height, 1, null);

			byte[] arr = {(byte)0x00, (byte)0xff};
			IndexColorModel colorModel = new IndexColorModel(1, 2, arr, arr, arr);
			BufferedImage image = new BufferedImage(colorModel, raster, false, null);

			java.awt.Image destGraphics = ((XawtDrawableListener) getDrawable().getListener()).getImage();
			
			destGraphics.getGraphics().drawImage(image, destinationX, destinationY, width, height, Color.CYAN, null);			
			
		} else {
			System.out.println("Unsupported depth");
		}
	}

//	@Override
//	public Image getImage() {
//		Image i = new Image();
//		if (getDrawable().getImage() instanceof XawtImageListener) {
//			BufferedImage image = ((XawtImageListener) getDrawable().getImage()).getXawtImage();
//			Graphics g = ((XawtImageListener) getDrawable().getImage()).getXawtGraphics();
//			
//			i.setListener(new XawtImageListener(image, g));
//		} else {
//			System.out.println("Unable to get image");
//		}
//		
//		return i;
//	}


//	@Override
//	public Image getImage() {
//		return _drawable.getImage();
//	}
}
