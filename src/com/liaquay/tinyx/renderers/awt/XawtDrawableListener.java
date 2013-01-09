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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Image;

public class XawtDrawableListener implements Drawable.Listener {

	final Drawable _drawable;

	public XawtDrawableListener(Drawable drawable) {
		_drawable = drawable;
		createImage(drawable);
	}

	@Override
	public void copyArea(Drawable destDrawable, GraphicsContext graphicsContext, int srcX,
			int srcY, int width, int height, int dstX, int dstY) {
		
		Image destImage = ((Drawable.Listener) destDrawable.getListener()).getImage();
		
		Image srcImage = ((Drawable.Listener) _drawable.getListener()).getImage();
		

		Graphics dg = ((XawtImageListener) destImage.getListener()).getXawtImage().createGraphics();
		Graphics sg = ((XawtImageListener) srcImage.getListener()).getXawtImage().createGraphics();
		
		dg.copyArea(srcX, srcY, width, height, dstX, dstY);
	}

	@Override
	public void putImage(GraphicsContext graphicsContext,
			byte[] buffer, int width, int height,
			int destinationX, int destinationY, int leftPad, int depth) { 

		if (depth == 1) {

			DataBufferByte db = new DataBufferByte(buffer, buffer.length);
			WritableRaster raster = Raster.createPackedRaster(db, width, height, 1, null);

			byte[] arr = {(byte)0, (byte)0xff};

			IndexColorModel colorModel 
			= new IndexColorModel(1, 2, arr, arr, arr);

			BufferedImage image = new BufferedImage(colorModel, raster, false, null);

			XawtImageListener xawtImage = new XawtImageListener(image);
			_drawable.setImage(xawtImage);
			
			
			Image srcImage = ((Drawable.Listener) _drawable.getListener()).getImage();
			Graphics sg = ((XawtImageListener) srcImage.getListener()).getXawtImage().createGraphics();
			
			sg.drawImage(image, destinationX, destinationY, width, height, null);			
			
		} else {
			System.out.println("Unsupported depth");
		}
	}


	@Override
	public void createImage(Drawable drawable) {
		BufferedImage image = new BufferedImage(drawable.getWidth(), drawable.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		
		XawtImageListener i = new XawtImageListener(image);
		drawable.setImage(i);
	}

	@Override
	public Image getImage() {
		Image i = new Image();
		if (_drawable.getImage() instanceof XawtImageListener) {
			BufferedImage image = ((XawtImageListener) _drawable.getImage()).getXawtImage();
			i.setListener(new XawtImageListener(image));
		} else {
			System.out.println("Unable to get image");
		}
		
		return i;

	}


//	@Override
//	public Image getImage() {
//		return _drawable.getImage();
//	}
}
