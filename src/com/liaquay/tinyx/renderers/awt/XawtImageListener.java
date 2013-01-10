package com.liaquay.tinyx.renderers.awt;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.liaquay.tinyx.model.Image;

public class XawtImageListener implements Image.Listener {

	BufferedImage _image;
	Graphics _graphics;

	public XawtImageListener(BufferedImage image, Graphics graphics) {
		this._image = image;
		this._graphics = graphics;
	}
	
	public BufferedImage getXawtImage() {
		return _image;
	}
	
	public Graphics getXawtGraphics() {
		return _graphics;
	}
}
