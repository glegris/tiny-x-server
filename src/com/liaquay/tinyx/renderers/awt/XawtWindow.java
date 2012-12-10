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
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import sun.awt.image.ByteBandedRaster;

import com.liaquay.tinyx.model.Cursor;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.Window.Listener;

public class XawtWindow  {

	private static Color[] COLORS = new Color[] {
		Color.BLACK,
		Color.BLUE,
		Color.CYAN,
		Color.GREEN,
		Color.MAGENTA,
		Color.ORANGE
	};

	private static int _ci = 0;
	private static Color getColor() {
		_ci = (_ci + 1) % COLORS.length;
		return COLORS[_ci];
	}

	private Server _server;

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
		public void setCursor(int id) {
			//Get the default toolkit
			Toolkit toolkit = Toolkit.getDefaultToolkit();

			Cursor xcursor = (Cursor) _server.getResources().get(id, Cursor.class);

			if (xcursor != null) {
				Pixmap p = xcursor.getSourcePixmap();

				BufferedImage image = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);

				int i = 0;
				for (int y = 0; y < p.getHeight() - 1; y++) {
					for (int x = 0; x < (p.getWidth()/8); x++) {
						int b = 0x00ff & p.getData()[i++];
						System.out.println(Integer.toBinaryString(b));

						for (int a = 0; a < 8; a++) {
							byte c = (byte) ((b >> 7-a) & 0x01);
							
							if (c > 0) {
								image.setRGB((x*8) + a, y, 200);
							} else {
								image.setRGB((x*8) + a, y, 0);
							}
						}
					}
				}

				if (image != null) {
					//Load an image for the cursor
					//				Image image = toolkit.getImage("pencil.gif");
					Point hotSpot = new Point(0,0);
					java.awt.Cursor c = toolkit.createCustomCursor(image, hotSpot, "Pencil");
					_canvas.setCursor(c);
				}
			}
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
		graphics.setColor(getColor());
		graphics.fillRect(
				0, 
				0,
				window.getWidth() + window.getBorderWidth()+ window.getBorderWidth(), 
				window.getHeight() + window.getBorderWidth()+ window.getBorderWidth());
	}

	private void paintContent(final Window window, final Graphics2D graphics) {
		graphics.setColor(getColor());
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());    	

		//Lets draw any pixmaps on the screen.

	}

	public XawtWindow(final Server server, final Window window) {
		this._server = server;

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
				System.out.println(String.format("Button %d, x=%d y=%d window=%x08 ", e.getButton(), e.getX(),e.getY(),evw.getId()));
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
				System.out.println(String.format("Button pressed %d, x=%d y=%d window=%x08 ", e.getButton(), e.getX(),e.getY(),evw.getId()));
				// TODO pass in correct screen index
				server.buttonPressed(0, e.getX(), e.getY(), e.getButton(), e.getWhen());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				final Window evw = window.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button release %d, x=%d y=%d window=%x08 ", e.getButton(), e.getX(),e.getY(),evw.getId()));
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
