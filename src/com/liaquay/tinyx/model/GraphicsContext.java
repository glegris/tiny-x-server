package com.liaquay.tinyx.model;

public class GraphicsContext extends AbstractResource {

	private Drawable _drawable;
	
	public GraphicsContext(final int resourceId, final Drawable drawable) {
		super(resourceId);
		_drawable = drawable;
	}
	
	private int _foregroundColour = 0x00000000;

	public void setForegroundColour(int value) {
		_foregroundColour = value;
	}

	public int getForegroundColour() {
		return _foregroundColour;
	}

	private int _backgroundColour = 0xffffffff;
	
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

	public int _lineStyle = 0;
	
	public int getLineStyle() {
		return _lineStyle;
	}

	public void setLineStyle(int lineStyle) {
		this._lineStyle = lineStyle;
	}

	public int _capStyle = 0;

	
	public int getCapStyle() {
		return _capStyle;
	}

	public void setCapStyle(int capStyle) {
		this._capStyle = capStyle;
	}

	public boolean graphicsExposures = false;
	
	public boolean getGraphicsExposures() {
		return this.graphicsExposures;
	}
	public void setGraphicsExposures(boolean graphicsExposures) {
		this.graphicsExposures = graphicsExposures;
	}

	int _arcMode = 0;
	
	public void setArcMode(int arcMode) {
		this._arcMode = arcMode;
	}
	
	public int getArcMode() {
		return this._arcMode;
	}

	int _joinStyle = 0;
	
	public void setJoinStyle(int joinStyle) {
		this._joinStyle = joinStyle;
	}
	
	public int getJoinStyle() {
		return this._joinStyle;
	}
	
	int _fillStyle = 0;

	public void setFillStyle(int fillStyle) {
		this._fillStyle = fillStyle;
	}
	
	public int getFillStyle() {
		return this._fillStyle;
	}

	int _fillRule = 0;
	
	public void setFillRule(int fillRule) {
		this._fillRule = fillRule;
	}
	
	public int getFillRule() {
		return this._fillRule;
	}
	
	int _tilePixmap = 0;

	public void setTile(int tilePixmap) {
		this._tilePixmap = tilePixmap;
	}
	
	public int getTile() {
		return this._tilePixmap;
	}
	
	int _stipple = 0;

	public void setStipple(int stipple) {
		this._stipple = stipple;
	}
	
	public int getStipple() {
		return this._stipple;
	}

	int _tileStippleXOrigin = 0;
	
	public void setTileStippleXOrigin(int tileStippleXOrigin) {
		this._tileStippleXOrigin = tileStippleXOrigin;
	}	
	
	public int getTileStippleXOrigin() {
		return this._tileStippleXOrigin;
	}

	int _tileStippleYOrigin = 0;
	
	public void setTileStippleYOrigin(int clipYOrigin) {
		this._tileStippleYOrigin = clipYOrigin;
	}	
	
	public int getTileStippleYOrigin() {
		return this._tileStippleYOrigin;
	}

	int _clipXOrigin = 0;
	
	public void setClipXOrigin(int clipXOrigin) {
		this._clipXOrigin = clipXOrigin;
	}
	
	public int getClipXOrigin() {
		return this._clipXOrigin;
	}

	int _clipYOrigin = 0;
	
	public void setClipYOrigin(int clipYOrigin) {
		this._clipYOrigin = clipYOrigin;
	}
	
	public int getClipYOrigin() {
		return this._clipYOrigin;
	}

	/** Weird X seems to imply that this can either be a pixmap or a bunch of rectanges.. */
	int _clipMask = 0;
	
	public void setClipMask(int clipMask) {
		this._clipMask = clipMask;
	}
	
	public int getClipMask() {
		return this._clipMask;
	}

	int _dashOffset = 0;
	
	public void setDashOffset(int dashOffset) {
		this._dashOffset = dashOffset;
	}

	public int getDashOffset() {
		return this._dashOffset;
	}

	Font _font = null;
	
	public void setFont(Font font) {
		_font = font;
	}
	
	public Font getFont() {
		return this._font;
	}
}
