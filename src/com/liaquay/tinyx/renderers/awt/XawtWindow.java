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
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

import com.liaquay.tinyx.model.Cursor;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Window;

/**
 * 
 * TODO use colour map to look up colours.
 *
 */
public class XawtWindow implements Window.Listener {

	private final Window _window;
	private final Canvas _canvas;

	@Override
	public void childCreated(final Window child) {
		final XawtWindow listener = new XawtWindow(child, _canvas);
		child.setListener(listener);
	}


	@Override
	public void mapped(final boolean mapped) {

		paintWindow();
		for(int i = 0; i < _window.getChildCount(); i++) {
			final Window child = _window.getChild(i);
			final XawtWindow awtChild = (XawtWindow)child.getListener();
			awtChild.mapped(mapped);
		}
	}

	@Override
	public void visible(final boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCursor(final Cursor cursor) {
		//Get the default toolkit
		final Toolkit toolkit = Toolkit.getDefaultToolkit();

		if (cursor != null) {
			final Pixmap p = cursor.getSourcePixmap();
			final Pixmap m = cursor.getMaskPixmap();

			// Buffered image that has transparency.
			final Image image = p.getImage();//new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
//leRaster newImage = p.getImage();//image.getRaster();

//			// This seems horribly inefficient, but will probably do for the time being.
//			int i = 0;
//			for (int y = 0; y < p.getHeight() - 1; y++) {
//				for (int x = 0; x < (p.getWidth()/8); x++) {
//					final int source = 0x00ff & p.getData()[i];
//					final int mask = 0x00ff & m.getData()[i++];
//
//					for (int a = 0; a < 8; a++) {
//						final byte sourcePixel = (byte) ((source >> 7-a) & 0x01);
//						final byte maskPixel = (byte) ((mask>> 7-a) & 0x01);
//
//						if (sourcePixel > 0) {
//							newImage.setSample((x*8)+a, y, 0, cursor.getForegroundColorRed());		// Red
//							newImage.setSample((x*8)+a, y, 1, cursor.getForegroundColorGreen());	// Green
//							newImage.setSample((x*8)+a, y, 2, cursor.getForegroundColorBlue());	// Blue
//						} else {
//							newImage.setSample((x*8)+a, y, 0, cursor.getBackgroundColorRed());		// Red
//							newImage.setSample((x*8)+a, y, 1, cursor.getBackgroundColorGreen());	// Green
//							newImage.setSample((x*8)+a, y, 2, cursor.getBackgroundColorBlue());	// Blue
//						}
//						newImage.setSample((x*8)+a, y, 3, maskPixel);	// Alpha
//					}
//				}
//			}

			if (image != null) {
				final Point hotSpot = new Point(cursor.getX(),cursor.getY());
				final java.awt.Cursor c = toolkit.createCustomCursor(image, hotSpot, cursor.getId() + "");
				_canvas.setCursor(c);
			}
		}
	}

	@Override
	public void drawString(
			final GraphicsContext graphicsContext, 
			final String str, 
			final int x,
			final int y) {

		final int foregroundColor = graphicsContext.getForegroundColour();
		final Color c = new Color(foregroundColor); // TODO Don't keep newing these

		final Font font = graphicsContext.getFont();
		final XawtFontListener fontListener = (XawtFontListener)font.getListener();

		final Graphics graphics = translateAndClipToWindow();
		//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final java.awt.Font awtFont = fontListener.getAwtFont();
		graphics.setFont(awtFont);
		graphics.setColor(c);
		graphics.drawString(str, x, y);
	}

	@Override
	public void polyArc(
			final GraphicsContext graphicsContext, 
			final int x, 
			final int y,
			final int width,
			final int height,
			final int angle1, 
			final int angle2,
			final boolean fill) {

		final Graphics2D graphics = translateAndClipToWindow();
		graphics.setColor(new Color(graphicsContext.getForegroundColour()));

		if (fill) {
			graphics.fillArc(x, y, width, height, angle1, angle2);
		} else {
			graphics.drawArc(x, y, width, height, angle1, angle2);
		}
	}

	@Override
	public void polyRect(
			final GraphicsContext graphicsContext, 
			final int x, 
			final int y,
			final int width, 
			final int height,
			final boolean fill) {

		final Graphics2D graphics = translateAndClipToWindow();
		graphics.setColor(new Color(graphicsContext.getForegroundColour()));

		if (fill) {
			graphics.fillRect(x, y, width, height);
		} else {
			graphics.drawRect(x, y, width, height);
		}
	}

	@Override
	public void polyFill(
			final GraphicsContext graphicsContext, 
			final int x[], 
			final int y[]) {

		final Graphics2D graphics = translateAndClipToWindow();
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(graphicsContext.getForegroundColour()));

		graphics.fillPolygon(x, y, x.length);
	}

	@Override
	public void polyLine(
			final GraphicsContext graphicsContext, 
			final int x[], 
			final int y[]) {

		final Graphics2D graphics = translateAndClipToWindow();
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(graphicsContext.getForegroundColour()));

		graphics.drawPolyline(x, y, x.length);
	}

	@Override
	public void drawLine(
			final GraphicsContext graphicsContext, 
			final int x1, 
			final int y1,
			final int x2, 
			final int y2) {

		final Graphics2D graphics = translateAndClipToWindow();
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(graphicsContext.getForegroundColour()));

		graphics.drawLine(x1, y1, x2, y2);
	}

	private Graphics2D translateAndClipToWindow() {
		final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
		graphics.translate(_window.getAbsX(), _window.getAbsY());
		return graphics;
	}

	private void paintWindow() {
		final Graphics2D graphics = (Graphics2D) _canvas.getGraphics();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		graphics.setClip(
				_window.getClipX(), 
				_window.getClipY(),
				_window.getClipWidth(), 
				_window.getClipHeight());

		final int borderWidth = _window.getBorderWidth();
		graphics.translate(_window.getAbsX()-borderWidth, _window.getAbsY()-borderWidth);

		paintBorder(graphics);

		final int borderWidthX2 = borderWidth + borderWidth;

		graphics.setClip(
				borderWidth, 
				borderWidth, 
				_window.getClipWidth() - borderWidthX2, 
				_window.getClipHeight() - borderWidthX2);

		graphics.translate(borderWidth, borderWidth);

		paintContent(graphics);

		graphics.translate(
				-_window.getAbsX() - borderWidth, 
				-_window.getAbsY() - borderWidth);
	}

	private void paintBorder(final Graphics2D graphics) {
		final int borderPixel = _window.getBorderPixel();

		graphics.setColor(new Color(borderPixel));
		graphics.fillRect(
				0, 
				0,
				_window.getWidth() + _window.getBorderWidth()+ _window.getBorderWidth(), 
				_window.getHeight() + _window.getBorderWidth()+ _window.getBorderWidth());
	}

	private void paintContent(final Graphics2D graphics) {
		graphics.setColor(new Color(_window.getBackgroundPixel()));
		graphics.fillRect(0, 0, _window.getWidth(), _window.getHeight());    	
	}

	public XawtWindow(final Window window, final Canvas canvas) {
		_window = window;
		_canvas = canvas;
	}

	@Override
	public void renderDrawable(
			final Drawable drawable,
			final GraphicsContext graphicsContext, 
			final int srcX,
			final int srcY,
			final int width,
			final int height, 
			final int dstX,
			final int dstY) {

		System.out.println("Render drawable: " + drawable + " X: " + srcX + " Y: " + srcY);

		if (drawable instanceof Pixmap) {
			final Pixmap p = (Pixmap) drawable;

			final BufferedImage image = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
			image.setData(p.toRaster());

			final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
			graphics.drawImage(image, dstX, dstY, width, height, null);
		}
	}


	@Override
	public int getPixel(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void putImage(GraphicsContext graphicsContext, byte[] buffer,
			int width, int height, int destinationX, int destinationY,
			int leftPad, int depth) {

		int[] arr = {(byte)0xff};

		//        IndexColorModel colorModel = new IndexColorModel(1, 2, arr, arr, arr);
		//        Raster raster = Raster.createPackedRaster(DataBuffer.TYPE_BYTE,
		//                                           width, height, 1, 1, null);


		Raster r = WritableRaster.createWritableRaster(new SinglePixelPackedSampleModel(DataBuffer.TYPE_BYTE, width, height, leftPad, arr), 
				new DataBufferByte(buffer, buffer.length), new Point(destinationX, destinationY));

		System.out.println("Raster: " + r);
	}
}
