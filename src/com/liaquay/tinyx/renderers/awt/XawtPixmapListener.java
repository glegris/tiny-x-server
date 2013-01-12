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

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Pixmap;

public class XawtPixmapListener extends XawtDrawableListener implements Pixmap.Listener {

	final Pixmap _pixmap;
	BufferedImage _image = null;

	public XawtPixmapListener(Pixmap pixmap) {
		_pixmap = pixmap;
		createImage(pixmap);
	}

	@Override
	public Image getImage() {
		return _image;
//		Image i = new Image();
		
//		((XawtDrawableListener) getDrawable()).getgetListener())
		
//		if (getDrawable().getImage() instanceof XawtImageListener) {
//			BufferedImage image = ((XawtImageListener) getDrawable().getImage()).getXawtImage();
//			Graphics g = ((XawtImageListener) getDrawable().getImage()).getXawtGraphics();
//			
//			i.setListener(new XawtImageListener(image, g));
//		} else {
//			System.out.println("Unable to get image");
//		}
		
//		return null;
	}
	
	@Override
	public void createImage(Drawable drawable) {
		BufferedImage image = new BufferedImage(drawable.getWidth(), drawable.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		_image = image;
//		XawtImageListener i = new XawtImageListener(image, image.getGraphics());
//		drawable.setImage(i);
	}

	@Override
	public Pixmap getDrawable() {
		return _pixmap;
	}

}
