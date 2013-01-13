package com.liaquay.tinyx.renderers.awt;

import java.awt.image.BufferedImage;

import com.liaquay.tinyx.model.Image;

public class XawtImageListener implements Image.Listener {

	BufferedImage _image;

	public XawtImageListener(BufferedImage image) {
		this._image = image;
	}
	
	public BufferedImage getXawtImage() {
		return _image;
	}
}
