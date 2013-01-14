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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.GraphicsContext;

public abstract class XawtDrawableListener implements Drawable.Listener {

	final Drawable _drawable;
	
	BufferedImage _image;

	protected abstract Graphics2D getGraphics();
	
	public XawtDrawableListener(Drawable drawable) {
		_drawable = drawable;
		createImage(drawable);
	}

	public void setImage(BufferedImage image) {
		this._image =  image;
	}

	@Override
	public abstract void createImage(Drawable drawable);

	@Override
	public void copyArea(Drawable destDrawable, GraphicsContext graphicsContext, int srcX,
			int srcY, int width, int height, int dstX, int dstY) {
		
		BufferedImage srcImage = getImage();
		BufferedImage destImage = destDrawable.getDrawableListener().getImage();

		srcImage.getGraphics().translate(dstX,  dstY);
		srcImage.getGraphics().drawImage(destImage, srcX, srcY, width, height, null);
	}

	@Override
	public void copyPlane(Drawable destDrawable, int bitplane, int srcX, int srcY,
			int width, int height, int dstX, int dstY) {
		
		BufferedImage srcImage = getImage();
		BufferedImage destImage = destDrawable.getDrawableListener().getImage();

		srcImage.getGraphics().translate(dstX,  dstY);
		srcImage.getGraphics().drawImage(destImage, srcX, srcY, width, height, null);
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
//			setImage(image);

			Graphics sg = _image.getGraphics();
			sg.drawImage(image, destinationX, destinationY, width, height, null);			
			
		} else {
			System.out.println("Unsupported depth");
		}
	}

//	@Override
//	public Image getImage() {
//		Image i = new Image();
//		if (_drawable.getImage() instanceof XawtImageListener) {
//			BufferedImage image = ((XawtImageListener) _drawable.getImage()).getXawtImage();
//			Graphics g = ((XawtImageListener) _drawable.getImage()).getXawtGraphics();
//			
//			i.setListener(new XawtImageListener(image, g));
//		} else {
//			System.out.println("Unable to get image");
//		}
//		
//		return i;
//	}

	@Override
	public void drawString(
			final GraphicsContext graphicsContext, 
			final String str, 
			final int x,
			final int y, 
			final int bx,
			final int by, 
			final int bw,
			final int bh) {
		
		final Font font = graphicsContext.getFont();
		final XawtFontListener fontListener = (XawtFontListener)font.getListener();
		final Graphics2D graphics = getGraphics();
		graphics.setColor(new Color(_drawable.getColorMap().getRGB(graphicsContext.getBackgroundColour())));
		graphics.fillRect(bx, by, bw, bh);
		graphics.setColor(new Color(_drawable.getColorMap().getRGB(graphicsContext.getForegroundColour())));
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final java.awt.Font awtFont = fontListener.getAwtFont();
		graphics.setFont(awtFont);
		graphics.drawString(str, x, y);		
	}

	@Override
	public BufferedImage getImage() {
		return _image;
	}
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

}
