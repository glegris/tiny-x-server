package com.liaquay.tinyx.model;

public class GraphicsContext extends AbstractResource {

	private Drawable _drawable;
	
	public GraphicsContext(final int resourceId, final Drawable drawable) {
		super(resourceId);
		_drawable = drawable;
	}
	
	private int _foregroundColour = 0xffffffff;

	public void setForegroundColour(int value) {
		_foregroundColour = value;
	}

	public int getForegroundColour() {
		return _foregroundColour;
	}

	private int _backgroundColour = 0x00000000;
	
	public void setBackgroundColour(int value) {
		_backgroundColour = value;
	}

	public int getBackgroundColour() {
		return _backgroundColour;
	}

}
