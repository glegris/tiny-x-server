package com.liaquay.tinyx.renderers.generic;

import com.liaquay.tinyx.model.Format;

public class ImageBase {
	int width = 0;

	int height = 0;

	Format format = null;

	public ImageBase(int width, int height, Format format) {
		this.width = width;
		this.height = height;
		this.format = format;
	}


	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Format getFormat() {
		return format;
	}
}
