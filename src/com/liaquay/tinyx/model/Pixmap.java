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

import sun.security.util.BitArray;



public class Pixmap implements Drawable {

	private final int _resourceId;
	private final Drawable _drawable;
	private final int _depth;
	private final int _width;
	private final int _height;

	private byte[] _data;

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

	public void init(byte[] data) {
		this._data = data;
	}

	public void copyArea(Window window, GraphicsContext graphicsContext,
			int srcX, int srcY, int width, int height, int dstX, int dstY) {
		// TODO Auto-generated method stub

	}

	/* Create a blank image with the given dimensions */
	private void initImage() {
		int size = (_width * _height * _depth);
		this._data = new byte[size];
		for (int a = 0; a < size; a++) {
			this._data[a] = 0;
		}
	}

	public void putImage(GraphicsContext graphicsContext, byte[] buffer,
			int width, int height, int destinationX, int destinationY,
			int leftPad, int depth) {

		if (getData() == null) {
			initImage();
		}

		// Nice simple case 
		if (depth == 1) {

			if (_depth == 1) {
				// Depends on the Pixmap type
				for (int y = 0; y < height - 1; y++) {
					for (int x = 0; x < width - 1; x++) {
						int srcByte = (x/8) + (y * (width/8));
						int srcBit = x % 8;
						int destByte = destinationX + (destinationY * y + leftPad);
						// TODO Fix this line.. Just having a laugh atm
						this._data[destByte] = (byte) (this._data[destByte] & (buffer[srcByte] & (1 << srcBit)));
					}
				}
			} else if (_depth == 8) {
				// Depends on the Pixmap type
				for (int y = 0; y < height - 1; y++) {
					for (int x = 0; x < width - 1; x++) {
						int srcByte = (x/8) + (y * height);
						int destByte = destinationX + (destinationY * y + leftPad);
						this._data[destByte] = buffer[srcByte];
					}
				}

			}
		}

	}
}
