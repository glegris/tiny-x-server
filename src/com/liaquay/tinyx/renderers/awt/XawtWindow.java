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
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
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
 * TODO translate graphics before and after each drawing operation.
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
	public void mapped(final Window window, final boolean mapped) {

		paintWindow(window);
		for(int i = 0; i < window.getChildCount(); i++) {
			final Window child = window.getChild(i);
			mapped(child, mapped);
		}
	}

	@Override
	public void visible(final Window window, final boolean visible) {
		// TODO Auto-generated method stub

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

			final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();



		}
	}


	@Override
	public void setCursor(final Cursor cursor) {
		//Get the default toolkit
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		if (cursor != null) {
			Pixmap p = cursor.getSourcePixmap();
			Pixmap m = cursor.getMaskPixmap();

			// Buffered image that has transparency.
			BufferedImage image = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			WritableRaster newImage = image.getRaster();

			// This seems horribly inefficient, but will probably do for the time being.
			int i = 0;
			for (int y = 0; y < p.getHeight() - 1; y++) {
				for (int x = 0; x < (p.getWidth()/8); x++) {
					int source = 0x00ff & p.getData()[i];
					int mask = 0x00ff & m.getData()[i++];

					for (int a = 0; a < 8; a++) {
						byte sourcePixel = (byte) ((source >> 7-a) & 0x01);
						byte maskPixel = (byte) ((mask>> 7-a) & 0x01);

						if (sourcePixel > 0) {
							newImage.setSample((x*8)+a, y, 0, cursor.getForegroundColorRed());		// Red
							newImage.setSample((x*8)+a, y, 1, cursor.getForegroundColorGreen());	// Green
							newImage.setSample((x*8)+a, y, 2, cursor.getForegroundColorBlue());	// Blue
						} else {
							newImage.setSample((x*8)+a, y, 0, cursor.getBackgroundColorRed());		// Red
							newImage.setSample((x*8)+a, y, 1, cursor.getBackgroundColorGreen());	// Green
							newImage.setSample((x*8)+a, y, 2, cursor.getBackgroundColorBlue());	// Blue
						}
						newImage.setSample((x*8)+a, y, 3, maskPixel);	// Alpha
					}
				}
			}

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

		final Graphics2D graphics = translateAndClipToWindow();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final java.awt.Font awtFont = fontListener.getAwtFont();

		graphics.setColor(c);
		graphics.setFont(awtFont);
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
	
	private void paintWindow(final Window window) {
		final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		graphics.setClip(
				window.getClipX(), 
				window.getClipY(),
				window.getClipWidth(), 
				window.getClipHeight());

		final int borderWidth = window.getBorderWidth();
		graphics.translate(window.getAbsX()-borderWidth, window.getAbsY()-borderWidth);

		paintBorder(window, graphics);

		final int borderWidthX2 = borderWidth + borderWidth;


		graphics.setClip(
				borderWidth, 
				borderWidth, 
				window.getClipWidth() - borderWidthX2, 
				window.getClipHeight() - borderWidthX2);

		graphics.translate(borderWidth, borderWidth);

		paintContent(window, graphics);

		graphics.translate(
				-window.getAbsX() - borderWidth, 
				-window.getAbsY() - borderWidth);
	}

	private void paintBorder(final Window window, final Graphics2D graphics) {
		int borderPixel = window.getBorderPixel();

		graphics.setColor(new Color(borderPixel));
		graphics.fillRect(
				0, 
				0,
				window.getWidth() + window.getBorderWidth()+ window.getBorderWidth(), 
				window.getHeight() + window.getBorderWidth()+ window.getBorderWidth());
	}

	private void paintContent(final Window window, final Graphics2D graphics) {

		graphics.setColor(new Color(window.getBackgroundPixel()));

		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());    	

		//Lets draw any pixmaps on the screen.

	}

	public XawtWindow(final Window window, final Canvas canvas) {
		_window = window;
		_canvas = canvas;
	}
}
