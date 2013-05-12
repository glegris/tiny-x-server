package com.liaquay.tinyx.renderers.generic;

public class ImageBase {
	int width = 0;

	int height = 0;

	int planes = 0;


	public ImageBase(int width, int height, int planes) {
		this.width = width;
		this.height = height;
		this.planes = planes;
	}


	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}


	public int getPlanes() {
		return planes;
	}
	
	public void setPlanes(int planes) {
		this.planes = planes;
	}
}
