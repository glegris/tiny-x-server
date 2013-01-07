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
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Window;

public class XawtPixmap implements Pixmap.Listener {

	Canvas _canvas;
	
	Image _pixmap;
	
	public XawtPixmap(Canvas canvas) {
		this._canvas = canvas;
	}
	
	@Override
	public void putImage(GraphicsContext graphicsContext, byte[] buffer,
			int width, int height, int destinationX, int destinationY,
			int leftPad, int depth) {

		
	}

	@Override
	public void createImage(int width, int height) {
		_pixmap = _canvas.createImage(width, height);
	}

	@Override
	public void copyArea(Window window, GraphicsContext graphicsContext,
			int srcX, int srcY, int width, int height, int dstX, int dstY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getImage() {
		return _pixmap;
	}


}
