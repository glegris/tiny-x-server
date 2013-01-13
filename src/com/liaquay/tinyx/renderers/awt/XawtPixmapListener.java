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


import java.awt.image.BufferedImage;

import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Image;
import com.liaquay.tinyx.model.Pixmap;

public class XawtPixmapListener extends XawtDrawableListener implements Pixmap.Listener {

	final Pixmap _pixmap;
	XawtImageListener _imageListener;

	public XawtPixmapListener(Pixmap pixmap) {
		_pixmap = pixmap;
		
		createImage();
	}
//
//	@Override
//	public Image getImage() {
//		return _image;
////		Image i = new Image();
//		
////		((XawtDrawableListener) getDrawable()).getgetListener())
//		
////		if (getDrawable().getImage() instanceof XawtImageListener) {
////			BufferedImage image = ((XawtImageListener) getDrawable().getImage()).getXawtImage();
////			Graphics g = ((XawtImageListener) getDrawable().getImage()).getXawtGraphics();
////			
////			i.setListener(new XawtImageListener(image, g));
////		} else {
////			System.out.println("Unable to get image");
////		}
//		
////		return null;
//	}
	
	@Override
	public void createImage() {
		BufferedImage image = new BufferedImage(_pixmap.getWidth(), _pixmap.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		
		XawtImageListener i = new XawtImageListener(image);
		_imageListener = i;
	}

	@Override
	public Pixmap getDrawable() {
		return _pixmap;
	}


	public Image.Listener getImage() {
		return (Image.Listener) _imageListener;
	}
}
