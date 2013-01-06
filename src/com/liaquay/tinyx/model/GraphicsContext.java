package com.liaquay.tinyx.model;

public class GraphicsContext extends AbstractResource {

	private final Drawable _drawable;
	
	public GraphicsContext(
			final int resourceId, 
			final Drawable drawable, 
			final Font font) {
		
		super(resourceId);
		
		_drawable = drawable;
		_font = font;
	}
	
	private int _foregroundColour = 0x00000000;

	public void setForegroundColour(final int value) {
		_foregroundColour = value;
	}

	public int getForegroundColour() {
		return _foregroundColour;
	}

	private int _backgroundColour = 0xffffffff;
	
	public void setBackgroundColour(final int value) {
		_backgroundColour = value;
	}

	public int getBackgroundColour() {
		return _backgroundColour;
	}

	private int _planeMask = 0xffffffff;
	
	public void setPlaneMask(final int planeMask) {
		_planeMask = planeMask;
	}
	
	public int getPlaneMask() {
		return _planeMask;
	}

	private int _subWindowMode = 0;
	
	public void setSubWindowMode(final int subWindowMode) {
		_subWindowMode = subWindowMode;
	}

	public int getSubWindowMode() {
		return _subWindowMode;
	}

	private int _function = 0;
	
	public void setFunction(final int function) {
		_function = function;
	}

	public int getFunction() {
		return _function;
	}

	private int _lineWidth = 0;
	
	public void setLineWidth(final int lineWidth) {
		_lineWidth = lineWidth;
	}

	public int getLineWidth() {
		return _lineWidth;
	}

	private int _lineStyle = 0;
	
	public int getLineStyle() {
		return _lineStyle;
	}

	public void setLineStyle(final int lineStyle) {
		_lineStyle = lineStyle;
	}

	private int _capStyle = 0;

	
	public int getCapStyle() {
		return _capStyle;
	}

	public void setCapStyle(final int capStyle) {
		_capStyle = capStyle;
	}

	private boolean _graphicsExposures = false;
	
	public boolean getGraphicsExposures() {
		return _graphicsExposures;
	}
	public void setGraphicsExposures(final boolean graphicsExposures) {
		_graphicsExposures = graphicsExposures;
	}

	private int _arcMode = 0;
	
	public void setArcMode(final int arcMode) {
		_arcMode = arcMode;
	}
	
	public int getArcMode() {
		return _arcMode;
	}

	private int _joinStyle = 0;
	
	public void setJoinStyle(final int joinStyle) {
		_joinStyle = joinStyle;
	}
	
	public int getJoinStyle() {
		return _joinStyle;
	}
	
	private int _fillStyle = 0;

	public void setFillStyle(final int fillStyle) {
		_fillStyle = fillStyle;
	}
	
	public int getFillStyle() {
		return _fillStyle;
	}

	private int _fillRule = 0;
	
	public void setFillRule(final int fillRule) {
		_fillRule = fillRule;
	}
	
	public int getFillRule() {
		return _fillRule;
	}
	
	private int _tilePixmap = 0;

	public void setTile(final int tilePixmap) {
		_tilePixmap = tilePixmap;
	}
	
	public int getTile() {
		return _tilePixmap;
	}
	
	private int _stipple = 0;

	public void setStipple(final int stipple) {
		_stipple = stipple;
	}
	
	public int getStipple() {
		return _stipple;
	}

	private int _tileStippleXOrigin = 0;
	
	public void setTileStippleXOrigin(final int tileStippleXOrigin) {
		_tileStippleXOrigin = tileStippleXOrigin;
	}	
	
	public int getTileStippleXOrigin() {
		return _tileStippleXOrigin;
	}

	private int _tileStippleYOrigin = 0;
	
	public void setTileStippleYOrigin(final int clipYOrigin) {
		this._tileStippleYOrigin = clipYOrigin;
	}	
	
	public int getTileStippleYOrigin() {
		return _tileStippleYOrigin;
	}

	private int _clipXOrigin = 0;
	
	public void setClipXOrigin(final int clipXOrigin) {
		_clipXOrigin = clipXOrigin;
	}
	
	public int getClipXOrigin() {
		return this._clipXOrigin;
	}

	private int _clipYOrigin = 0;
	
	public void setClipYOrigin(final int clipYOrigin) {
		_clipYOrigin = clipYOrigin;
	}
	
	public int getClipYOrigin() {
		return this._clipYOrigin;
	}

	/** Weird X seems to imply that this can either be a pixmap or a bunch of rectanges.. */
	private int _clipMask = 0;
	
	public void setClipMask(final int clipMask) {
		_clipMask = clipMask;
	}
	
	public int getClipMask() {
		return this._clipMask;
	}

	private int _dashOffset = 0;
	
	public void setDashOffset(final int dashOffset) {
		_dashOffset = dashOffset;
	}

	public int getDashOffset() {
		return _dashOffset;
	}

	private Font _font;
	
	public void setFont(final Font font) {
		_font = font;
	}
	
	public Font getFont() {
		return _font;
	}
}
