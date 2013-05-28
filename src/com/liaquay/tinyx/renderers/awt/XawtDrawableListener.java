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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.liaquay.tinyx.model.Arc;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.Format;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Image.ImageType;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Rectangle;
import com.liaquay.tinyx.model.Segment;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.renderers.generic.ByteImage;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.FillStyle.FillStyleType;

public abstract class XawtDrawableListener implements Drawable.Listener {

	private final static Logger LOGGER = Logger.getLogger(XawtDrawableListener.class.getName());

	final Drawable _drawable;

	public abstract Graphics2D getGraphics();

	public abstract BufferedImage getImage();

	public static final int GCFunction          =(1<<0);
	public static final int GCPlaneMask         =(1<<1);
	public static final int GCForeground        =(1<<2);
	public static final int GCBackground        =(1<<3);
	public static final int GCLineWidth         =(1<<4);
	public static final int GCLineStyle         =(1<<5);
	public static final int GCCapStyle          =(1<<6);
	public static final int GCJoinStyle			=(1<<7);
	public static final int GCFillStyle			=(1<<8);
	public static final int GCFillRule			=(1<<9);
	public static final int GCTile				=(1<<10);
	public static final int GCStipple			=(1<<11);
	public static final int GCTileStipXOrigin	=(1<<12);
	public static final int GCTileStipYOrigin	=(1<<13);
	public static final int GCFont				=(1<<14);
	public static final int GCSubwindowMode		=(1<<15);
	public static final int GCGraphicsExposures =(1<<16);
	public static final int GCClipXOrigin		=(1<<17);
	public static final int GCClipYOrigin		=(1<<18);
	public static final int GCClipMask			=(1<<19);
	public static final int GCDashOffset		=(1<<20);
	public static final int GCDashList			=(1<<21);
	public static final int GCArcMode			=(1<<22);

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

	public Graphics2D getGraphics(GraphicsContext graphicsContext,
			int supportedModes) {

		Graphics2D g = getGraphics();

		if(graphicsContext != null) {
			//			Pixmap tilePixmap = graphicsContext.getTile();
			//
			//			if (tilePixmap != null && (supportedModes & GCTile) > 0 && (FillStyleType.getFromIndex(graphicsContext.getFillStyle()) == FillStyleType.Tiled)) {
			//
			//				final XawtDrawableListener awtDrawable = (XawtDrawableListener) tilePixmap.getDrawableListener();
			//				final BufferedImage srcImage = awtDrawable.getImage();
			//
			//				BufferedImage output = createCompatibleImage(srcImage, new GraphicsContextComposite(graphicsContext, _drawable));
			//
			//				TexturePaint tp = new TexturePaint(output, new java.awt.Rectangle(graphicsContext.getTileStippleXOrigin(), graphicsContext.getTileStippleYOrigin(), tilePixmap.getWidth(), tilePixmap.getHeight()));
			//				g.setPaint(tp);
			//			}
			//
			//				TODO: This needs to clip and not just the same as the others!
			//			Pixmap clipPixmap = graphicsContext.getClipMask();
			//
			//			if (clipPixmap != null && (supportedModes & GCClipMask) > 0) {
			//				final XawtDrawableListener awtDrawable = (XawtDrawableListener) clipPixmap.getDrawableListener();
			//				final BufferedImage srcImage = awtDrawable.getImage();
			//
			//				BufferedImage output = createCompatibleImage(srcImage, new GraphicsContextComposite(graphicsContext, _drawable));
			//
			//				TexturePaint tp = new TexturePaint(output, new java.awt.Rectangle(graphicsContext.getClipXOrigin(), graphicsContext.getClipYOrigin(), clipPixmap.getWidth(), clipPixmap.getHeight()));
			//				g.setPaint(tp);
			//			}
			//			
			//			Bitmap stipplePixmap = graphicsContext.getStipple();
			//
			//			if (stipplePixmap != null && (supportedModes & GCStipple) > 0) {
			//				final XawtDrawableListener awtDrawable = (XawtDrawableListener) stipplePixmap.getDrawableListener();
			//				final BufferedImage srcImage = awtDrawable.getImage();
			//
			//				BufferedImage output = createCompatibleImage(srcImage, new GraphicsContextComposite(graphicsContext, supportedModes, drawable));
			//
			//				TexturePaint tp = new TexturePaint(output, new java.awt.Rectangle(graphicsContext.getTileStippleXOrigin(), graphicsContext.getTileStippleYOrigin(), stipplePixmap.getWidth(), stipplePixmap.getHeight()));
			//				g.setPaint(tp);
			//			}

			//			if ((supportedModes & GCForeground) > 0) {
			//				final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getForegroundColour());
			//				g.setColor(new Color(rgb));
			//			}
			//
			//			if ((supportedModes & GCBackground) > 0) {
			//				final int rgb = _drawable.getColorMap().getRGB(graphicsContext.getBackgroundColour());
			//				g.setBackground(new Color(rgb));
			//			}

//			g.setComposite(new GraphicsContextComposite(graphicsContext, supportedModes, _drawable));
		}		


		return g;
	}

	public Graphics2D getGraphics(GraphicsContext graphicsContext) {
		return getGraphics(graphicsContext, 0);
	}

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

		int supportedModes =  GCFunction | GCPlaneMask | GCSubwindowMode | GCGraphicsExposures | GCClipXOrigin | GCClipYOrigin | GCClipMask;

		final XawtDrawableListener awtDrawable = (XawtDrawableListener)srcDrawable.getDrawableListener();
		try {
			final BufferedImage srcImage = awtDrawable.getImage();

			getGraphics(graphicsContext, supportedModes).drawImage(
					srcImage, 
					dstX, 
					dstY, 
					dstX + width, 
					dstY + height, 
					srcDrawable.getAbsX() + srcX, 
					srcDrawable.getAbsY() + srcY, 
					srcDrawable.getAbsX() + srcX + width,
					srcDrawable.getAbsY() + srcY + height, 
					null);//, srcY, srcX + width, srcY + height, null);

		} catch (final RasterFormatException e) {
			e.printStackTrace();
		}
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
		final BufferedImage srcImage = awtDrawable.getImage();//.getSubimage(_drawable.getX() + srcX, _drawable.getY() + srcY, width, height);		

		//		final BufferedImage destImage = createCompatibleImage(srcImage, new CopyPlaneComposite(bitplane, graphicsContext.getForegroundColour(), graphicsContext.getBackgroundColour()));

		final Graphics2D g = getGraphics(graphicsContext);
		g.drawImage(
				srcImage, 
				dstX, 
				dstY, 
				dstX + width, 
				dstY + height, 
				srcDrawable.getAbsX() + srcX, 
				srcDrawable.getAbsY() + srcY, 
				srcDrawable.getAbsX() + srcX + width,
				srcDrawable.getAbsY() + srcY + height, 
				null);
	}

	//	public int lookupColor(int color) {
	//		if (color > 8) {
	//			return 0xffffffff;
	//		} else {
	//			return 0xff00ff00;
	//		}
	//	}

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

		Format f = lookupFormat(depth);

		LOGGER.warning("Depth: " + depth + " BPP: " + f.getBpp() + "Width: " + width + " Height: " + height + "DestX: " + destinationX + " DestY: " + destinationY);

		final ByteImage bi = new ByteImage(width, height, f);
		bi.setData(buffer);

		int supportedModes =  GCFunction | GCPlaneMask | GCSubwindowMode | GCClipXOrigin | GCClipYOrigin | GCClipMask | GCForeground | GCBackground;

		final Graphics sg = getGraphics(graphicsContext, supportedModes);
		
		final BufferedImage bufImage = ImageConverter.convertByteImage(bi, imageType, graphicsContext);
		sg.drawImage(bufImage, destinationX, destinationY, width, height, null);
	}

	private Format lookupFormat(int depth) {
		Format format = null;
		for (Format f : Server.getFormats()) {
			if (f.getDepth() == depth) {
				format = f;
				break;
			}
		}
		return format;
	}

	BufferedImage createCompatibleImage(final BufferedImage image, final Composite composite)
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

			//		Shape s = new Polygon(xCoords, yCoords, xCoords.length);
			//		Stroke stroke = XAwtGraphicsContext.lineSetup(graphics, graphicsContext);
			//		graphics.draw(stroke.createStrokedShape(s));

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

			int bpp = 1;
			Format[] formats = Server.getFormats();
			for (Format f : formats) {
				if (f.getDepth() == _drawable.getDepth()) {
					bpp = f.getBpp();
				}
			}

			int size = (width * height * bpp)/8;

			LOGGER.severe("Size in bytes: " + size + " Depth: " + bpp +  Integer.toHexString(size));

			byte[] byteArr = new byte[size];
			Arrays.fill(byteArr, (byte) 0);

			return byteArr;			
		} else {

			LOGGER.severe("GetImageData with a planemask of " + Integer.toBinaryString(planeMask) + " and imageType of " + imageType.name() + " is not currently supported");
		}


		return null;
	}

	@Override
	public void polyLine(
			final GraphicsContext graphicsContext, 
			final int[] xCoords,
			final int[] yCoords) {
		int supportedModes = GCForeground | GCBackground | GCTile | GCStipple | GCTileStipXOrigin | GCTileStipYOrigin | GCDashOffset | GCDashList;
		final Graphics2D graphics = getGraphics(graphicsContext, supportedModes);

		graphics.drawPolygon(xCoords, yCoords, xCoords.length);
	}

	@Override
	public void drawLine(GraphicsContext graphicsContext, int[] x1, int[] y1,
			int[] x2, int[] y2) {

		int supportedModes = GCForeground | GCBackground | GCTile | GCStipple | GCTileStipXOrigin | GCTileStipYOrigin | GCDashOffset | GCDashList;
		final Graphics2D graphics = getGraphics(graphicsContext, supportedModes);

		for (int i = 0; i < x1.length; i++) {
			graphics.drawLine(x1[i], y1[i], x2[i], y2[i]);
		}
	}

	@Override
	public void drawLine(GraphicsContext graphicsContext, int x1, int y1,
			int x2, int y2) {

		int supportedModes = GCForeground | GCBackground | GCTile | GCStipple | GCTileStipXOrigin | GCTileStipYOrigin | GCDashOffset | GCDashList;
		final Graphics2D graphics = getGraphics(graphicsContext, supportedModes);

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
	public void polyRectangle(
			final GraphicsContext graphicsContext,
			final Collection<Rectangle> rectangles,
			final boolean fill) {

		//		            function: Copy
		//		          plane-mask: ffffffff
		//		          foreground: 00fffafa
		//		          background: 00000001
		//		          fill-style: Stippled
		//		             stipple: PXM 0040000a

		//		graphicsContext.setGraphicsOperationX(x);
		//		graphicsContext.setGraphicsOperationY(y);
		//		graphicsContext.setGraphicsOperationHeight(height);
		//		graphicsContext.setGraphicsOperationWidth(width);

		int supportedModes = GCForeground | GCBackground | GCTile | GCStipple | GCTileStipXOrigin | GCTileStipYOrigin | GCDashOffset | 
				GCDashList | GCFunction | GCPlaneMask | GCLineWidth | GCLineStyle | GCCapStyle | GCJoinStyle | GCFillStyle | GCSubwindowMode | GCClipXOrigin | GCClipYOrigin | GCClipMask;

		final Graphics2D graphics = getGraphics(graphicsContext, supportedModes);


		for (Rectangle r : rectangles) {

			byte arr[] = { (byte) 0x0, (byte) 0xff};
			IndexColorModel cm = new IndexColorModel(1, 2, arr, arr, arr, arr);
			
			// Step 1. Select source pixels
			BufferedImage clipImage = new BufferedImage(r.getWidth(), r.getHeight(), BufferedImage.TYPE_BYTE_BINARY, cm);

			
			Pixmap clipMask = graphicsContext.getClipMask();
			if (clipMask != null && (supportedModes & GCClipMask) > 0) {
					final XawtDrawableListener awtDrawable = (XawtDrawableListener) clipMask.getDrawableListener();
					final BufferedImage srcImage = awtDrawable.getImage();


					int startX = 0;
					if ((supportedModes & GCClipXOrigin) > 0) {
						startX = graphicsContext.getClipXOrigin();
					}
					int startY = 0;
					if ((supportedModes & GCClipYOrigin) > 0) {
						startY = graphicsContext.getClipYOrigin();
					}

					Rectangle2D r1 = new java.awt.Rectangle(startX, startY, r.getWidth(), r.getHeight());//srcImage.getHeight(), startY + srcImage.getWidth());
					TexturePaint tp = new TexturePaint(srcImage, r1);
					graphics.setPaint(tp);
					
					Graphics2D gr = (Graphics2D) clipImage.getGraphics();
					gr.setPaint(tp);
					gr.fillRect(0, 0, r.getWidth(), r.getHeight());

			} else {
				for (int x=0; x<r.getWidth(); x++) {
					for (int y=0; y<r.getHeight(); y++) {
						clipImage.setRGB(x, y, 0xffffffff);
					}
				}
			}


//			XawtDrawableListener.writeImage(clipImage, "clipImage");



			// Step 2. Colouring of the pixels

//			Graphics2D outputGraphics = ((Graphics2D) output.getGraphics());

			// If fill_style is fillsolid, then the foreground colour should be used for all pixels set to 1 in the source image.

			BufferedImage stage2 = new BufferedImage(r.getWidth(), r.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D stage2Gfx = (Graphics2D) stage2.getGraphics();

			if (graphicsContext.getFillStyle() == FillStyleType.Solid.ordinal()) {
				for (int x=0; x<r.getWidth(); x++) {
					for (int y=0; y<r.getHeight();y++) {
//						if (sourcePixels.getRGB(x, y)==0xffffffff) {
							stage2.setRGB(x, y, 0xff000000 | graphicsContext.getForegroundColour());
//						}
					}
				}

			} else if (graphicsContext.getFillStyle() == FillStyleType.Stippled.ordinal()) {

				Pixmap stipple = graphicsContext.getStipple();

				if (stipple != null) {
					final XawtDrawableListener awtDrawable = (XawtDrawableListener) stipple.getDrawableListener();
					final BufferedImage srcImage = awtDrawable.getImage();
					
					BufferedImage newSrcImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

					// Map the colours so that the foreground is the stipple 1's
					for (int x=0; x<srcImage.getWidth(); x++) {
						for (int y=0; y<srcImage.getHeight();y++) {
							int pixel = srcImage.getRGB(x, y);
							if (pixel==-1) {
								newSrcImage.setRGB(x, y, 0xff000000 | graphicsContext.getForegroundColour());
							}
						}
					}
					
					int startX = 0;
					if ((supportedModes & GCTileStipXOrigin) > 0) {
						startX = graphicsContext.getTileStippleXOrigin();
					}
					int startY = 0;
					if ((supportedModes & GCTileStipYOrigin) > 0) {
						startY = graphicsContext.getTileStippleYOrigin();
					}

					Rectangle2D r1 = new java.awt.Rectangle(0, 0, r.getWidth(), r.getHeight());//srcImage.getHeight(), startY + srcImage.getWidth());
					
					// Paint the stipple
					TexturePaint tp = new TexturePaint(newSrcImage, r1);
					
					stage2Gfx.setPaint(tp);
					
					if (fill) {
						stage2Gfx.fillRect(0, 0, r.getWidth(), r.getHeight());
					} else {
						stage2Gfx.drawRect(0, 0, r.getWidth(), r.getHeight());
					}
				}
			}

			// Clip to the clipImage
			stage2Gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
			stage2Gfx.drawImage(clipImage, 0, 0, r.getWidth(),  r.getHeight(), null);
			
			graphics.drawImage(stage2, r.getX(), r.getY(), r.getWidth(), r.getHeight(), null);

		}
	}

	@Override
	public void polyPoint(GraphicsContext graphicsContext, int[] xCoords,
			int[] yCoords) {

		int supportedModes = GCFunction | GCPlaneMask | GCForeground | GCSubwindowMode | GCClipXOrigin | GCClipYOrigin | GCClipMask;
		final Graphics2D graphics = getGraphics(graphicsContext, supportedModes);

		for (int i=0; i < xCoords.length; i++) {
			int x = xCoords[i];
			int y = yCoords[i];

			graphics.drawLine(x, y, x, y);
		}
	}

	public void polyArc(GraphicsContext graphicsContext, Collection<Arc> arcs, boolean fill) {
		int supportedModes = GCForeground | GCBackground | GCTile | GCStipple | GCTileStipXOrigin | GCTileStipYOrigin | GCDashOffset | GCDashList;
		final Graphics2D graphics = getGraphics(graphicsContext, supportedModes);

		for (Arc a : arcs) {
			if (fill) {
				graphics.fillArc(a.getX(), a.getY(), a.getWidth(), a.getHeight(), a.getAngle1(), a.getAngle2());
			} else {
				graphics.drawArc(a.getX(), a.getY(), a.getWidth(), a.getHeight(), a.getAngle1(), a.getAngle2());
			}
		}
	}

	public void polyFill(GraphicsContext graphicsContext, int[] x, int[] y) {

		int supportedModes = GCForeground | GCBackground | GCTile | GCStipple | GCTileStipXOrigin | GCTileStipYOrigin ;
		final Graphics2D graphics = getGraphics(graphicsContext, supportedModes);

		graphics.fillPolygon(x, y, x.length);
	}

	@Override
	public void drawSegments(GraphicsContext graphicsContext,
			Collection<Segment> segments) {

		int supportedModes = GCForeground | GCBackground | GCTile | GCStipple | GCTileStipXOrigin | GCTileStipYOrigin | GCDashOffset | GCDashList;
		final Graphics2D graphics = getGraphics(graphicsContext, supportedModes);

		for (Segment s : segments) {
			graphics.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
		}		
	}


}
