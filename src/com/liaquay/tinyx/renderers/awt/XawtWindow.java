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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.liaquay.tinyx.model.Cursor;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

/**
 *
 */
public class XawtWindow extends XawtDrawableListener implements Window.Listener {

	private final Window _window;
	private final Canvas _canvas;
	private final Server _server;

	@Override
	public void childCreated(final Window child) {
		final XawtWindow listener = new XawtWindow(_server, child, _canvas);
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
		// TODO Auto-generated method stub//

	}

	@Override
	public void setCursor(final Cursor cursor) {
		//Get the default toolkit
		final Toolkit toolkit = Toolkit.getDefaultToolkit();

		if (cursor != null) {
			final Pixmap p = cursor.getSourcePixmap();
			final Pixmap m = cursor.getMaskPixmap();

			// Buffered image that has transparency.
			//			final Image image = p.getImage();//new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			//			image.getListener();
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

			//			if (image != null) {
			//				final Point hotSpot = new Point(cursor.getX(),cursor.getY());
			//				final java.awt.Cursor c = toolkit.createCustomCursor(image, hotSpot, cursor.getId() + "");
			//				_canvas.setCursor(c);
			//			}
		}
	}

	@Override
	public void drawString(
			final GraphicsContext graphicsContext, 
			final String str, 
			final int x,
			final int y) {

		super.drawString(graphicsContext, str, x, y);
		
		updateCanvas();
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

		super.polyArc(graphicsContext, x, y, width, height, angle1, angle2, fill);

		updateCanvas();
	}

	@Override
	public void polyRect(
			final GraphicsContext graphicsContext, 
			final int x, 
			final int y,
			final int width, 
			final int height,
			final boolean fill) {

		super.polyRect(graphicsContext, x, y, width, height, fill);
		
		updateCanvas();
	}

	@Override
	public void polyFill(
			final GraphicsContext graphicsContext, 
			final int x[], 
			final int y[]) {

		super.polyFill(graphicsContext, x, y);
		
		updateCanvas();
	}

	@Override
	public void polyLine(
			final GraphicsContext graphicsContext, 
			final int x[], 
			final int y[]) {

		super.polyLine(graphicsContext, x, y);
		
		updateCanvas();
	}

	@Override
	public void drawLine(
			final GraphicsContext graphicsContext, 
			final int x1, 
			final int y1,
			final int x2, 
			final int y2) {

		super.drawLine(graphicsContext, x1, y1, x2, y2);
		
		updateCanvas();
	}

	@Override
	protected Graphics2D getGraphics() {
		return (Graphics2D) _image.getGraphics();
//		return translateAndClipToWindow();
	}
	
//	private Graphics2D translateAndClipToWindow() {
//		final Graphics2D graphics = (Graphics2D) _image.getGraphics();
//		graphics.translate(_window.getAbsX(), _window.getAbsY());
//		return graphics;
//	}

	private void paintWindow() {
		final Graphics2D graphics = (Graphics2D) _image.getGraphics();

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
		updateCanvas();
	}

	private void paintBorder(final Graphics2D graphics) {
		final int rgb = _window.getColorMap().getRGB(_window.getBorderPixel());
		graphics.setColor(new Color(rgb));
		graphics.fillRect(
				0, 
				0,
				_window.getWidth() + _window.getBorderWidth()+ _window.getBorderWidth(), 
				_window.getHeight() + _window.getBorderWidth()+ _window.getBorderWidth());
	}

	private void paintContent(final Graphics2D graphics) {
		final int rgb = _window.getColorMap().getRGB(_window.getBackgroundPixel());
		graphics.setColor(new Color(rgb));
		graphics.fillRect(0, 0, _window.getWidth(), _window.getHeight());    	
	}

	@Override
	public void clearArea(boolean exposures, int x, int y, int width, int height) {
		super.clearArea(exposures, x, y, width, height);
		
		updateCanvas();
	}

	@Override
	public void copyArea(Drawable destDrawable,
			GraphicsContext graphicsContext, int srcX, int srcY, int width,
			int height, int dstX, int dstY) {

		super.copyArea(destDrawable, graphicsContext, srcX, srcY, width, height, dstX,
				dstY);
		updateCanvas();
	}


	@Override
	public void putImage(GraphicsContext graphicsContext, byte[] buffer,
			int width, int height, int destinationX, int destinationY,
			int leftPad, int depth) {

		super.putImage(graphicsContext, buffer, width, height, destinationX,
				destinationY, leftPad, depth);
		updateCanvas();
	}


	@Override
	public void copyPlane(Drawable s, 		
int bitplane, int srcX, int srcY,
			int width, int height, int dstX, int dstY) {
		super.copyPlane(s, bitplane, srcX, srcY, width, height, dstX, dstY);
		updateCanvas();
	}
	
	@Override
	public void polyPoint(GraphicsContext graphicsContext, int[] xCoords,
			int[] yCoords) {
		super.polyPoint(graphicsContext, xCoords, yCoords);
		updateCanvas();
	}

	public XawtWindow(final Server server, final Window window, final Canvas canvas) {
		super(server, window);

		_server = server;
		_window = window;
		_canvas = canvas;

		paintWindow();
	}


	@Override
	public int getPixel(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void createImage(Drawable drawable) {
		BufferedImage image = new BufferedImage(drawable.getWidth(), drawable.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		setImage(image);
	}

	private void updateCanvas() {
		_canvas.getGraphics().setClip(0, 0, 800, 600);
		_canvas.getGraphics().drawImage(getImage(), _window.getAbsX(), _window.getAbsY(), _window.getWidth(), _window.getHeight(), null);
	}
}
