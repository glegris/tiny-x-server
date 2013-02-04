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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.FilteredImageSource;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Event;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Image.ImageType;
import com.liaquay.tinyx.model.Window;

public abstract class XawtDrawableListener implements Drawable.Listener {

	private final static Logger LOGGER = Logger.getLogger(XawtDrawableListener.class.getName());

	final Drawable _drawable;

	public abstract Graphics2D getGraphics();

	public XawtDrawableListener(Drawable drawable) {
		_drawable = drawable;
	}


	public static void writeImage(BufferedImage image, String filename) {
		if (image != null) {
			try {
				// create a file to write the image to (make sure it exists), then use the ImageIO class
				// to write the RenderedImage to disk as a PNG file.
				File file = new File("/home/ncludki/tinyx/" + filename + System.currentTimeMillis() + ".png");
				file.createNewFile();
				ImageIO.write(image, "png", file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static  BufferedImage canvasToImage(Canvas cnvs) {
        int w = cnvs.getWidth();
        int h = cnvs.getHeight();
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage image = new BufferedImage(w,h,type);
        Graphics2D g2 = image.createGraphics();
        cnvs.paint(g2);
        g2.dispose();
        return image;
    }
 

	@Override
	public abstract void createImage(Drawable drawable);

	@Override
	public void copyArea(Drawable srcDrawable, GraphicsContext graphicsContext, int srcX,
			int srcY, int width, int height, int dstX, int dstY) {

		//		BufferedImage destImage = getImage();
		BufferedImage srcImage = srcDrawable.getDrawableListener().getImage();

		//		getGraphics().translate(_drawable.getX() + dstX,  _drawable.getY() + dstY);
		getGraphics().drawImage(srcImage, dstX, dstY, dstX + width, dstY + height, srcX, srcY, srcX + width, srcY + height, null);
	}

	@Override
	public void copyPlane(Drawable srcDrawable, GraphicsContext graphicsContext, int bitplane, int srcX, int srcY,
			int width, int height, int dstX, int dstY) {

		BufferedImage srcImage = srcDrawable.getDrawableListener().getImage();


		BitmapColourFilter filter = new BitmapColourFilter(0xffffffff);

		FilteredImageSource filteredSrc = new FilteredImageSource(srcImage.getSource(), filter);
		Image newImage = Toolkit.getDefaultToolkit().createImage(filteredSrc);

		//		getGraphics().translate(_drawable.getX() + dstX,  _drawable.getY() + dstY);
		getGraphics().drawImage(newImage, srcX, srcY, width, height, null);
	}

	@Override
	public void putImage(GraphicsContext graphicsContext, ImageType imageType, 
			byte[] buffer, int width, int height,
			int destinationX, int destinationY, int leftPad, int depth) { 

		BufferedImage image = null;

		LOGGER.info("Put image with type: " + imageType.name() + " Width: " + width + " Height: " + height + " and depth " + depth + " leftpad: " + leftPad);
		if (depth == 1) {
			DataBufferByte db = new DataBufferByte(buffer, buffer.length);
			WritableRaster raster = Raster.createPackedRaster(db, width, height, 1, null);

			byte[] arr = {(byte)0x00, (byte)0xff};
			IndexColorModel colorModel = new IndexColorModel(1, 2, arr, arr, arr);
			image = new BufferedImage(colorModel, raster, false, null);

		} else if (depth == 32) {
			// Only 3 bytes per pixel appear to be sent..
			DataBufferByte db = new DataBufferByte(buffer, buffer.length);

			WritableRaster raster = Raster.createInterleavedRaster(db, // dataBuffer
					width, // width
					height, // height
					width * 3, // scanlineStride
					3, // pixelStride
					new int[]{2, 1, 0}, // bandOffsets
					null); // location

			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			image.setData(raster);

		} else {
			LOGGER.severe("PutImage doesn't currently support a depth of " + depth);
		}



		Graphics sg = getGraphics();
		sg.drawImage(image, destinationX, destinationY, width, height, null);
	}

	BufferedImage createCompatibleImage(BufferedImage image)
	{
		GraphicsConfiguration gc = GraphicsEnvironment.
				getLocalGraphicsEnvironment().
				getDefaultScreenDevice().
				getDefaultConfiguration();

		BufferedImage newImage = gc.createCompatibleImage(
				image.getWidth(), 
				image.getHeight(), 
				Transparency.TRANSLUCENT);

		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return newImage;
	}

	@Override
	public byte[] getImageData(int x, int y, int width, int height,
			ImageType imageType, int planeMask) {

		System.out.println("GetImageData: " + imageType.name() + " Depth: " + _drawable.getDepth());
		if (planeMask == 0xffffffff && imageType.equals(ImageType.ZPixmap)) {

			int size = (width * height * _drawable.getDepth())/8;

			byte[] byteArr = new byte[size];

			getImage().getRaster().getDataElements(x, y, width, height, byteArr);

			//			byte data[] = new byte[intData.length * 4];
			//			
			//			for (int i = 0; i < intData.length; i++) {
			//				data[i*4+0] = (byte) ((0xff000000 & intData[i]) >> 48);
			//			    data[i*4+1] = (byte) ((0x00ff0000 & intData[i]) >> 32);
			//				data[i*4+2] = (byte) ((0x0000ff00 & intData[i]) >> 16);
			//				data[i*4+3] = (byte) ((0x000000ff & intData[i]) >> 0);
			//			}
			return byteArr;
		} else {
			LOGGER.severe("GetImageData with a planemask of " + Integer.toBinaryString(planeMask) + " and imageType of " + imageType.name() + " is not currently supported");
		}


		return null;
	}

	@Override
	public void polyLine(GraphicsContext graphicsContext, int[] xCoords,
			int[] yCoords) {
		final Graphics2D graphics = getGraphics();
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				XAwtGraphicsContext.tile(graphics, graphicsContext);

		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));

		//		Shape s = new Polygon(xCoords, yCoords, xCoords.length);
		//		Stroke stroke = XAwtGraphicsContext.lineSetup(graphics, graphicsContext);
		//		graphics.draw(stroke.createStrokedShape(s));
		graphics.drawPolygon(xCoords, yCoords, xCoords.length);
	}

	@Override
	public void drawLine(GraphicsContext graphicsContext, int[] x1, int[] y1,
			int[] x2, int[] y2) {

		final Graphics2D graphics = getGraphics();
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));

				XAwtGraphicsContext.tile(graphics, graphicsContext);

		//		Stroke stroke = XAwtGraphicsContext.lineSetup(graphics, graphicsContext);

		for (int i = 0; i < x1.length; i++) {
			//			Shape l = new Line2D.Float(x1[i], y1[i], x2[i], y2[i]);
			//			graphics.draw(stroke.createStrokedShape(l));
			graphics.drawLine(x1[i], y1[i], x2[i], y2[i]);
		}

	}

	@Override
	public void drawLine(GraphicsContext graphicsContext, int x1, int y1,
			int x2, int y2) {
		final Graphics2D graphics = getGraphics();
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));

		//		Stroke stroke = XAwtGraphicsContext.lineSetup(graphics, graphicsContext);
		//		Shape l = new Line2D.Float(x1, y1, x2, y2);
		//		graphics.draw(stroke.createStrokedShape(l));
		graphics.drawLine(x1, y1, x2, y2);
	}

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
		final XawtFontDetail fontDetail = (XawtFontDetail)font.getFontDetail();
		final Graphics2D graphics = getGraphics();
		graphics.setColor(new Color(_drawable.getColorMap().getRGB(graphicsContext.getBackgroundColour())));
		graphics.fillRect(bx, by, bw, bh);
		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		fontDetail.drawString(graphics, str, x, y, rgb);
	}

	public void drawString(
			final GraphicsContext graphicsContext, 
			final String str, 
			final int x,
			final int y) {

		final Font font = graphicsContext.getFont();
		final XawtFontDetail fontDetail = (XawtFontDetail)font.getFontDetail();
		final Graphics2D graphics = getGraphics();
		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		fontDetail.drawString(graphics, str, x, y, rgb);
	}

	@Override
	public void polyRect(
			final GraphicsContext graphicsContext,
			final int x,
			final int y,
			final int width,
			final int height, 
			final boolean fill) {

		final Graphics2D graphics = getGraphics();

		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));

		XAwtGraphicsContext.tile(graphics, graphicsContext);

		XAwtGraphicsContext.stipple(graphics, graphicsContext);


		if (fill) {
			graphics.fillRect(x, y, width, height);
		} else {
			graphics.drawRect(x, y, width, height);
		}
	}

	@Override
	public void polyPoint(GraphicsContext graphicsContext, int[] xCoords,
			int[] yCoords) {

		final Graphics2D graphics = getGraphics();
		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));

		for (int i=0; i < xCoords.length; i++) {
			int x = xCoords[i];
			int y = yCoords[i];

			graphics.drawLine(x, y, x, y);
		}
	}

	public void polyArc(GraphicsContext graphicsContext, int x, int y,
			int width, int height, int angle1, int angle2, boolean fill) {
		final Graphics2D graphics = getGraphics();
		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));

		if (fill) {
			graphics.fillArc(x, y, width, height, angle1, angle2);
		} else {
			graphics.drawArc(x, y, width, height, angle1, angle2);
		}
	}

	public void polyFill(GraphicsContext graphicsContext, int[] x, int[] y) {
		final Graphics2D graphics = getGraphics();
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));

		graphics.fillPolygon(x, y, x.length);
	}

	public void clearArea(boolean exposures, int x, int y, int width, int height) {
		final Graphics2D graphics = getGraphics();

		final int rgb = _drawable.getColorMap().getRGB(_drawable.getBackgroundPixel());
		graphics.setBackground(new Color(rgb));

		graphics.clearRect(x, y, width, height);

		if (exposures && _drawable instanceof Window) {
			Window w = (Window) _drawable;

			final Event exposeEvent = w.getEventFactories().getExposureFactory().create(w.getId(), w.getX(), w.getY(), w.getClipWidth(), w.getClipHeight(), 0);
			w.deliver(exposeEvent, Event.ExposureMask);
		}
	}
}
