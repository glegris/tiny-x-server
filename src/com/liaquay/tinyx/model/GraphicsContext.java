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

	private int _planeMask = 0xffffffff;
	
	public void setPlaneMask(int planeMask) {
		_planeMask = planeMask;
	}
	
	public int getPlaneMask() {
		return this._planeMask;
	}

	private int _subWindowMode = 0;
	
	public void setSubWindowMode(int subWindowMode) {
		this._subWindowMode = subWindowMode;
	}

	public int getSubWindowMode() {
		return _subWindowMode;
	}

	private int _function = 0;
	
	public void setFunction(int function) {
		_function = function;
	}

	public int getFunction() {
		return _function;
	}

	public int _lineWidth = 0;
	
	public void setLineWidth(int lineWidth) {
		this._lineWidth = lineWidth;
	}

	public int getLineWidth() {
		return _lineWidth;
	}
}
