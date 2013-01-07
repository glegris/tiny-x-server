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
package com.liaquay.tinyx.model;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import com.liaquay.tinyx.renderers.awt.XawtScreen.PixmapProducer;



public class Pixmap implements Drawable {

	public interface Listener {
		public void putImage(GraphicsContext graphicsContext, byte[] buffer, int width, int height, int destinationX, int destinationY, int leftPad, int depth);
		public void createImage(int width, int height);
		public void copyArea(Window window, GraphicsContext graphicsContext,
				int srcX, int srcY, int width, int height, int dstX, int dstY);
		public Image getImage();
	}

	private static final class NullListener implements Listener {

		@Override
		public void createImage(int width, int height) {

		}

		@Override
		public void putImage(GraphicsContext graphicsContext, byte[] buffer,
				int width, int height, int destinationX, int destinationY,
				int leftPad, int depth) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void copyArea(Window window, GraphicsContext graphicsContext,
				int srcX, int srcY, int width, int height, int dstX, int dstY) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public BufferedImage getImage() {
			// TODO Auto-generated method stub
			return null;
		}

	}
	private final int _resourceId;
	private final Drawable _drawable;
	private final int _depth;
	private final int _width;
	private final int _height;

	private byte[] _data;

	private static final Listener NULL_LISTENER = new NullListener();

	private Listener _listener = NULL_LISTENER;
	
	public Pixmap(final int resourceId,
			final Drawable drawable,
			final int depth,
			final int width,
			final int height) {
		_resourceId = resourceId;
		_depth = depth;
		_width = width;
		_height = height;
		_drawable = drawable;
		_listener = PixmapProducer.getPixmapListener();

		_listener.createImage(width, height);
	}

	@Override
	public int getId() {
		return _resourceId;
	}

	public byte[] getData() {
		return this._data;
	}

	@Override
	public void free() {
		// TODO Auto-generated method stub

	}

	@Override
	public Screen getScreen() {
		// TODO Not sure this is correct
		return _drawable.getScreen();
	}

	@Override
	public Visual getVisual() {
		// TODO Not sure this is correct
		return _drawable.getVisual();
	}

	public int getDepth() {
		return _depth;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getWidth() {
		return _width;
	}

	@Override
	public int getHeight() {
		return _height;
	}

	@Override
	public int getBorderWidth() {
		return 0;
	}

	public void copyArea(Window window, GraphicsContext graphicsContext,
			int srcX, int srcY, int width, int height, int dstX, int dstY) {

		_listener.copyArea(window, graphicsContext, srcX, srcY, width, height, dstX, dstY);
	}

	public void clear() {
		Arrays.fill(this._data, 0, this._data.length, (byte) 0xff);
	}

	public void print() {
		int widthInBytes = getWidth() / 8;

		for (int y=0; y < getHeight(); y++) {
			for (int x=0; x < widthInBytes; x++) {
				int i = (int) getData()[(widthInBytes * y) + x] & 0xFF;
				String str = Integer.toBinaryString(i);

				if (str.length() < 8) {
					for (int j=0; j < (8-str.length()); j++) {
						System.out.print("0");
					}
				}
				System.out.print(str);
			}
			System.out.println();
		}
	}

	public void putImage(GraphicsContext graphicsContext, byte[] buffer,
			int width, int height, int destinationX, int destinationY,
			int leftPad, int depth) {

		_listener.putImage(graphicsContext, buffer, width, height, destinationX, destinationY, leftPad, depth);

//		if (getData() == null) {
//			initImage();
//		}
//
//		// Nice simple case 
//		if (depth == 1) {
//
//			if (_depth == 1) {
//
//				// Depends on the Pixmap type
//				for (int y = 0; y < height ; y++) {
//					for (int x = 0; x < width ; x++) {
//						int srcByte = (x/8) + (y * (width/8));
//						int srcBit = x % 8;
//						int destPixel = (destinationX + x) + ((destinationY + y) * getWidth() + leftPad);
//						int destByte = destPixel/8;
//						int destBitMask = 0x01 << (7 - destPixel % 8);
//						String mask = Integer.toBinaryString(destBitMask);
//						//System.out.println("X: " + x + " Y: " + y + "  Dest pixel: " + destPixel + "  Mask: " + mask);
//						// TODO Fix this line.. Just having a laugh atm
//						if (destByte < this._data.length) {
//							this._data[destByte] = (byte) (this._data[destByte] | (buffer[srcByte] & destBitMask));
//						}
//					}
//				}
//
//			} else {
//				System.out.println("Unsupported depth at present: " + _depth);
//			}
//			print();
//		}

	}

	public static void main(String args[]) {
		Pixmap p = new Pixmap(1, null, 1, 8, 8);
		p.clear();
		p.print();

		System.out.println("-------------------");

		Pixmap p1 = new Pixmap(1, null, 1, 16, 16);
		p1.putImage(null, p.getData(), p.getWidth(), p.getHeight(), 4, 4, 0, 1);
		p1.print();
	}

	public Raster toRaster() {
		WritableRaster raster = Raster.createPackedRaster(DataBuffer.TYPE_BYTE,
				getWidth(), getHeight(), 1, 1, null);

		return raster;
	}

	public Image getImage() {
		return _listener.getImage();
	}

}
