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
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Image.ImageType;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.renderers.awt.gc.CopyPlaneComposite;
import com.liaquay.tinyx.renderers.awt.gc.GraphicsContextComposite;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.FillStyle.FillStyleType;

public abstract class XawtDrawableListener implements Drawable.Listener {

	private final static Logger LOGGER = Logger.getLogger(XawtDrawableListener.class.getName());

	final Drawable _drawable;

	public abstract Graphics2D getGraphics(GraphicsContext gc);

	public abstract Graphics2D getGraphics();

	public abstract BufferedImage getImage();

	public XawtDrawableListener(final Drawable drawable) {
		_drawable = drawable;
	}


	public static void writeImage(final BufferedImage image, final String filename) {
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

//
//	public static  BufferedImage canvasToImage(Canvas cnvs) {
//		int w = cnvs.getWidth();
//		int h = cnvs.getHeight();
//		int type = BufferedImage.TYPE_INT_BGR;
//		BufferedImage image = new BufferedImage(w,h,type);
//		Graphics2D g2 = image.createGraphics();
//		cnvs.paint(g2);
//		g2.dispose();
//		return image;
//	}

	@Override
	public abstract void createImage(Drawable drawable);

	@Override
	public void copyArea(
			final Drawable srcDrawable, 
			final GraphicsContext graphicsContext, 
			final int srcX,
			final int srcY,
			final int width, 
			final int height,
			final int dstX, 
			final int dstY) {

		final XawtDrawableListener awtDrawable = (XawtDrawableListener)srcDrawable.getDrawableListener();
		final BufferedImage srcImage = awtDrawable.getImage().getSubimage(_drawable.getX() + srcX, _drawable.getY() + srcY, width, height);

		getGraphics(graphicsContext).drawImage(srcImage, dstX, dstY, dstX + width, dstY + height, 0, 0, width, height, null);//, srcY, srcX + width, srcY + height, null);
		
//		final BufferedImage srcImage = awtDrawable.getImage();
//		getGraphics(graphicsContext).drawImage(srcImage, dstX, dstY, dstX + width, dstY + height, srcX, srcY, srcX + width, srcY + height, null);

	}

	@Override
	public void copyPlane(
			final Drawable srcDrawable,
			final GraphicsContext graphicsContext, 
			final int bitplane,
			final int srcX,
			final int srcY,
			final int width,
			final int height,
			final int dstX, 
			final int dstY) {

		final XawtDrawableListener awtDrawable = (XawtDrawableListener)srcDrawable.getDrawableListener();
		final BufferedImage srcImage = awtDrawable.getImage().getSubimage(_drawable.getX() + srcX, _drawable.getY() + srcY, width, height);		

		final BufferedImage destImage = createCompatibleImage(srcImage, new CopyPlaneComposite(bitplane, graphicsContext.getForegroundColour(), graphicsContext.getBackgroundColour()));

		final Graphics2D g = getGraphics(graphicsContext);
		g.drawImage(destImage, dstX, dstY, dstX + width, dstY + height, 0, 0, width, height, null);
	}

	@Override
	public void putImage(
			final GraphicsContext graphicsContext, 
			final ImageType imageType, 
			final byte[] buffer, 
			final int width, 
			final int height,
			final int destinationX, 
			final int destinationY, 
			final int leftPad, 
			final int depth) { 

		BufferedImage image = null;

		LOGGER.info("Put image with type: " + imageType.name() + " Width: " + width + " Height: " + height + " and depth " + depth + " leftpad: " + leftPad);

//		int newDepth = depth;

//		int planeSize = width * height;
//		int planes = (buffer.length * 8) / planeSize ;
//		if (depth == 1 && planes == 4) {
//			try {
//				byte[] buffer2 = newBuffer(buffer);
//
//				DataBufferByte db = new DataBufferByte(buffer2, buffer.length);
//				WritableRaster raster = Raster.createPackedRaster(db, width, height, 1, null);
//
//				byte[] arr = {(byte)0x00, (byte)0xff};
//				IndexColorModel colorModel = new IndexColorModel(1, 2, arr, arr, arr);
//				image = new BufferedImage(colorModel, raster, false, null);
////				writeImage(image, "test-fishy");
//			} catch (RasterFormatException e) {
//				LOGGER.warning("putImage: " + e.getMessage());
//			}
//		} else 

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

				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
				image.setData(raster);

			} else {
				LOGGER.severe("PutImage doesn't currently support a depth of " + depth);
			}

			BufferedImage newImage = createCompatibleImage(image, new GraphicsContextComposite(graphicsContext, _drawable));

//			writeImage(newImage, "test");
			Graphics sg = getGraphics(graphicsContext);
			sg.drawImage(newImage, destinationX, destinationY, width, height, null);
	}

	/** nibbles in.. bytes out */
	byte[] newBuffer(byte[] buffer) {
		byte[] newBuffer = new byte[buffer.length/4];

		int a = 0;
		int i = 0;
		while (i < buffer.length) {
			// For each nibble in the source stream, convert to a single bit in the output stream
			byte out = 0;

			out+= (byte) (buffer[i] & 16) << 3;
			out+= (byte) (buffer[i] & 1) << 6;
			i++;

			out+= (byte) (buffer[i] & 16) << 1;
			out+= (byte) (buffer[i] & 1) << 4;
			i++;

			out+= (byte) (buffer[i] & 16) >> 1;
			out+= (byte) (buffer[i] & 1) << 2;
			i++;

			out+= (byte) (buffer[i] & 16) >> 3;
			out+= (byte) (buffer[i] & 1);
			i++;

			newBuffer[a++] = out;
		}

		return newBuffer;

	}


	BufferedImage createCompatibleImage(BufferedImage image, Composite composite)
	{
		GraphicsConfiguration gc = GraphicsEnvironment.
				getLocalGraphicsEnvironment().
				getDefaultScreenDevice().
				getDefaultConfiguration();

		BufferedImage newImage = gc.createCompatibleImage(
				image.getWidth(), 
				image.getHeight(), Transparency.TRANSLUCENT);

		Graphics2D g = newImage.createGraphics();

		if (composite != null) {
			g.setComposite(composite);
		}		

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
		} else if (planeMask == 0x00000000 && imageType.equals(ImageType.ZPixmap)) {
			int size = (width * height * _drawable.getDepth())/8;

			byte[] byteArr = new byte[size];
			Arrays.fill(byteArr, (byte) 0);

			return byteArr;			
		} else {

			LOGGER.severe("GetImageData with a planemask of " + Integer.toBinaryString(planeMask) + " and imageType of " + imageType.name() + " is not currently supported");
		}


		return null;
	}

	@Override
	public void polyLine(GraphicsContext graphicsContext, int[] xCoords,
			int[] yCoords) {
		final Graphics2D graphics = getGraphics(graphicsContext);
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//		XAwtGraphicsContext.tile(graphics, graphicsContext);

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

		final Graphics2D graphics = getGraphics(graphicsContext);
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));

		//		XAwtGraphicsContext.tile(graphics, graphicsContext);

//		Stroke stroke = XAwtGraphicsContext.lineSetup(graphics, graphicsContext);

		for (int i = 0; i < x1.length; i++) {
//						Shape l = new Line2D.Float(x1[i], y1[i], x2[i], y2[i]);
//						graphics.draw(stroke.createStrokedShape(l));
			graphics.drawLine(x1[i], y1[i], x2[i], y2[i]);
		}
	}

	@Override
	public void drawLine(GraphicsContext graphicsContext, int x1, int y1,
			int x2, int y2) {
		final Graphics2D graphics = getGraphics(graphicsContext);
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
		final Graphics2D graphics = getGraphics(graphicsContext);
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
		final Graphics2D graphics = getGraphics(graphicsContext);
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

		//		            function: Copy
		//		          plane-mask: ffffffff
		//		          foreground: 00fffafa
		//		          background: 00000001
		//		          fill-style: Stippled
		//		             stipple: PXM 0040000a

		final Graphics2D graphics = getGraphics(graphicsContext);

		final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
		graphics.setColor(new Color(rgb));


		Pixmap tilePixmap = graphicsContext.getTile();

		if (tilePixmap != null && (FillStyleType.getFromIndex(graphicsContext.getFillStyle()) == FillStyleType.Tiled)) {

			final XawtDrawableListener awtDrawable = (XawtDrawableListener) tilePixmap.getDrawableListener();
			final BufferedImage srcImage = awtDrawable.getImage();

			BufferedImage output = createCompatibleImage(srcImage, new GraphicsContextComposite(graphicsContext, _drawable));

			TexturePaint tp = new TexturePaint(output, new Rectangle(graphicsContext.getTileStippleXOrigin(), graphicsContext.getTileStippleYOrigin(), tilePixmap.getWidth(), tilePixmap.getHeight()));
			graphics.setPaint(tp);
		}


		//TODO: This needs to clip and not just the same as the others!
//		Pixmap clipPixmap = graphicsContext.getClipMask();
//
//		if (clipPixmap != null) {
//
//			final XawtDrawableListener awtDrawable = (XawtDrawableListener) clipPixmap.getDrawableListener();
//			final BufferedImage srcImage = awtDrawable.getImage();
//
//			BufferedImage output = createCompatibleImage(srcImage, new GraphicsContextComposite(graphicsContext, x, y));
//
//			TexturePaint tp = new TexturePaint(output, new Rectangle(graphicsContext.getClipXOrigin(), graphicsContext.getClipYOrigin(), clipPixmap.getWidth(), clipPixmap.getHeight()));
//			graphics.setPaint(tp);
//		}

		if (fill) {
			graphics.fillRect(x, y, width, height);
		} else {
			graphics.drawRect(x, y, width, height);
		}
	}
	private static BufferedImage imageToBufferedImage(Image image, int width, int height)
	{
		BufferedImage dest = new BufferedImage(
				width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return dest;
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
}
