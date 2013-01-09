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

import java.awt.image.BufferedImage;




public class Pixmap extends Drawable {

	public interface Listener extends Drawable.Listener {
		Image createImage(Pixmap pixmap);	
	}


	private static final Listener NULL_LISTENER = new NullListener();

	private Listener _listener = NULL_LISTENER;

	public void setListener(final Listener listener) {
		_listener = listener;
	}
	private static final class NullListener implements Listener {

		@Override
		public void copyArea(Drawable srcDrawable, Drawable d, GraphicsContext graphicsContext,
				int srcX, int srcY, int width, int height, int dstX, int dstY) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Image createImage(Pixmap pixmap) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void putImage(GraphicsContext graphicsContext, byte[] buffer,
				int width, int height, int destinationX, int destinationY,
				int leftPad, int depth) {

		}

	}
	
	private final int _resourceId;
	private final Drawable _drawable;
	private final int _depth;
	private final int _width;
	private final int _height;
	BufferedImage _image;
	
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

	public BufferedImage getImage() {
		return this._image;
	}
	
	@Override
	public int getId() {
		return _resourceId;
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

	public void putImage(GraphicsContext graphicsContext, byte[] buffer,
			int width, int height, int destinationX, int destinationY,
			int leftPad, int depth) {

		_listener.putImage(graphicsContext, buffer, width, height, destinationX, destinationY, leftPad, depth);
	}

	@Override
	public com.liaquay.tinyx.model.Drawable.Listener getListener() {
		return _listener;
	}

	public void setImage(BufferedImage image) {
		this._image = image;
		
	}
}
