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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Pixmap;

public class XawtPixmap extends XawtDrawableListener implements Pixmap.Listener {

	private BufferedImage _image;

	public XawtPixmap(final Pixmap pixmap) {
		super(pixmap);
		createImage(pixmap);
	}

	@Override
	public void createImage(final Drawable drawable) {
		BufferedImage image = null;
		if (drawable.getDepth() == 1) {
			image = new BufferedImage(drawable.getWidth(), drawable.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		} else {
			image = new BufferedImage(drawable.getWidth(), drawable.getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		setImage(image);
	}
	
	@Override
	public Graphics2D getGraphics() {
		final Graphics2D g = (Graphics2D) getImage().getGraphics();
		return g;
	}
	
	public void setImage(final BufferedImage image) {
		_image =  image;
	}

	@Override
	public BufferedImage getImage() {
		return _image;
	}

	@Override
	public void free() {
		_image = null;
	}
}
