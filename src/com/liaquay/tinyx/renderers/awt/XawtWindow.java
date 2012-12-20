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
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import sun.awt.X11.XCustomCursor;
import sun.awt.image.ByteBandedRaster;

import com.liaquay.tinyx.model.Cursor;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.Window.Listener;
import com.sun.java.swing.plaf.windows.resources.windows;

public class XawtWindow  {

	private Server _server;

	private Window _window;

	private Window.Listener _windowListener = new Listener() {

		@Override
		public void childCreated(final Window child) {
			child.setListener(_windowListener);
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
		public void renderDrawable(Drawable drawable,
				GraphicsContext graphicsContext, int srcX, int srcY, int width,
				int height, int dstX, int dstY) {

			System.out.println("Render drawable: " + drawable + " X: " + srcX + " Y: " + srcY);

			if (drawable instanceof Pixmap) {
				Pixmap p = (Pixmap) drawable;

				BufferedImage image = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

				final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();



			}
		}

		@Override
		public void setCursor(Cursor cursor) {
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
					Point hotSpot = new Point(cursor.getX(),cursor.getY());
					java.awt.Cursor c = toolkit.createCustomCursor(image, hotSpot, cursor.getId() + "");
					_canvas.setCursor(c);
				}
			}
		}

		@Override
		public void drawString(GraphicsContext graphicsContext, String str, int x, int y) {
			int foregroundColor = graphicsContext.getForegroundColour();
			Color c = new Color(foregroundColor);

			Font f = graphicsContext.getFont();
			f.getFontName();

			final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
			graphics.setColor(c);
			java.awt.Font newFont = new java.awt.Font(f.getFontName().getFamilyName(), java.awt.Font.PLAIN, 10);

			graphics.setFont(newFont);

			graphics.drawString(str, x, y);
		}

		@Override
		public void polyArc(GraphicsContext graphicsContext, int x, int y,
				int width, int height, int angle1, int angle2, boolean fill) {

			final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
			graphics.setColor(new Color(graphicsContext.getForegroundColour()));

			if (fill) {
				graphics.fillArc(x, y, width, height, angle1, angle2);
			} else {
				graphics.drawArc(x, y, width, height, angle1, angle2);
			}
		}

		@Override
		public void polyRect(GraphicsContext graphicsContext, int x, int y,
				int width, int height, boolean fill) {

			final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
			graphics.setColor(new Color(graphicsContext.getForegroundColour()));

			if (fill) {
				graphics.fillRect(x, y, width, height);
			} else {
				graphics.drawRect(x, y, width, height);
			}
		}
		
		@Override
		public void polyFill(GraphicsContext graphicsContext, int x[], int y[]) {

			final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
			graphics.setColor(new Color(graphicsContext.getForegroundColour()));

			graphics.fillPolygon(x, y, x.length);
		}
		
		@Override
		public void polyLine(GraphicsContext graphicsContext, int x[], int y[]) {

			final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
			System.out.println(Integer.toHexString(graphicsContext.getForegroundColour()));
			
			int red = (byte) (graphicsContext.getForegroundColour() & 0xff000000) >> 24;
			int blue = (byte)(graphicsContext.getForegroundColour() & 0x00ff0000) >> 16;
			int green = (byte)(graphicsContext.getForegroundColour() & 0x0000ff00) >> 8;
			
			graphics.setColor(new Color(red, blue, green));

			graphics.drawPolyline(x, y, x.length);
		}
		
	};

	private Canvas _canvas = new Canvas();

	private void paintWindow(final Window window) {
		final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();

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

	public XawtWindow(final Server server, final Window window) {
		this._server = server;
		this._window = window;

		_canvas.setBounds(
				window.getX(),
				window.getY(),
				window.getWidthPixels() + window.getBorderWidth() + window.getBorderWidth(), 
				window.getHeightPixels() + window.getBorderWidth() + window.getBorderWidth());

		window.setListener(_windowListener);

		_canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				final Window evw = window.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button %d, x=%d y=%d", e.getButton(), e.getX(),e.getY()));
				if (evw != null) {
					System.out.println(String.format("window=%x08", evw.getId()));
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				final Window evw = window.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button pressed %d, x=%d y=%d", e.getButton(), e.getX(),e.getY()));
				if (evw != null) {
					System.out.println(String.format("window=%x08", evw.getId()));
				}

				// TODO pass in correct screen index
				server.buttonPressed(0, e.getX(), e.getY(), e.getButton(), e.getWhen());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				final Window evw = window.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button release %d, x=%d y=%d",  e.getButton(), e.getX(),e.getY()));
				if (evw != null) {
					System.out.println(String.format("window=%x08", evw.getId()));
				}
				// TODO pass in correct screen index
				server.buttonReleased(0, e.getX(), e.getY(), e.getButton(), e.getWhen());
			}
		});

		_canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				System.out.println("Released ");
				System.out.println("Keycode " + e.getKeyCode());
				System.out.println("Location " + e.getKeyLocation());
				System.out.println("Modifiers " + e.getModifiersEx());
				server.keyReleased(e.getKeyCode(), e.getWhen());
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				System.out.println("Pressed ");
				System.out.println("Keycode " + e.getKeyCode());
				System.out.println("Location " + e.getKeyLocation());
				System.out.println("Modifiers " + e.getModifiersEx());
				server.keyPressed(e.getKeyCode(), e.getWhen());
			}
		});
	}

	public Canvas getCanvas() {
		return _canvas;
	}
}
