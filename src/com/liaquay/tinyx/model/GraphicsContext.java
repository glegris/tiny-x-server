package com.liaquay.tinyx.model;

import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.requesthandlers.gcattribhandlers.ArcMode.ArcModeType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.CapStyle.CapStyleType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.FillRule.FillRuleType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.FillStyle.FillStyleType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.Function.FunctionType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.JoinStyle.JoinStyleType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.LineStyle.LineStyleType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.SubWindowMode.SubWindowModeType;

public class GraphicsContext extends AbstractResource {

	private final Drawable _drawable;
	
	public GraphicsContext(
			final int resourceId, 
			final Drawable drawable, 
			final Font font) {
		
		super(resourceId);
		
		_drawable = drawable;
		_font = font;
		
		// Default (4,4)
		_dashes.add(4);
		_dashes.add(4);
	}

	private FunctionType _function = FunctionType.Copy;			// Default: Copy
	private int _planeMask = 0xffffffff;						// Default: All ones
	private int _foregroundColour = 0x00000000;					// Default: Zeros
	private int _backgroundColour = 0xffffffff;					// Default: All ones
	private int _lineWidth = 0;									// Default: Zeros
	private int _lineStyle = LineStyleType.Solid.ordinal();		// Default: Solid
	private int _capStyle = CapStyleType.Butt.ordinal();		// Default: Butt
	private int _joinStyle = JoinStyleType.Miter.ordinal();		// Default: Miter
	private int _fillStyle = FillStyleType.Solid.ordinal();		// Default: Solid
	private int _fillRule = FillRuleType.EvenOdd.ordinal();		// Default: EvenOdd
	private int _arcMode = ArcModeType.PieSlice.ordinal();		// Default: PieSlice
	
	private int _subWindowMode = SubWindowModeType.ClipByChildren.ordinal(); // Default: ClipByChildren
	private boolean _graphicsExposures = true;					// Default: true

	private Pixmap _tilePixmap = null;	//TODO: Pixmap of unspecified size filled with foreground pixel
	private Pixmap _stipple = null;		//TODO: Pixmap of unspecified size filled with ones

	private int _tileStippleXOrigin = 0;		// Default: Zero
	private int _tileStippleYOrigin = 0;		// Default: Zero
	
	private Pixmap _clipMask = null;				// Default: None

	private int _clipXOrigin = 0;				// Default: Zero
	private int _clipYOrigin = 0;				// Default: Zero
	
	private int _dashOffset = 0;				// Default: Zero
	
	private Font _font;	// TODO: Server dependent font
	
	private List<Integer> _dashes = new ArrayList<Integer>();		// Default (4,4) Added in constructor
			

	
	public void setForegroundColour(final int value) {
		_foregroundColour = value;
	}

	public int getForegroundColour() {
		return _foregroundColour;
	}

	public void setBackgroundColour(final int value) {
		_backgroundColour = value;
	}

	public int getBackgroundColour() {
		return _backgroundColour;
	}
	
	public void setPlaneMask(final int planeMask) {
		_planeMask = planeMask;
	}
	
	public int getPlaneMask() {
		return _planeMask;
	}

	public void setSubWindowMode(final int subWindowMode) {
		_subWindowMode = subWindowMode;
	}

	public int getSubWindowMode() {
		return _subWindowMode;
	}

	public void setFunction(final FunctionType function) {
		_function = function;
	}

	public FunctionType getFunction() {
		return _function;
	}

	public void setLineWidth(final int lineWidth) {
		_lineWidth = lineWidth;
	}

	public int getLineWidth() {
		return _lineWidth;
	}

	public int getLineStyle() {
		return _lineStyle;
	}

	public void setLineStyle(final int lineStyle) {
		_lineStyle = lineStyle;
	}

	public int getCapStyle() {
		return _capStyle;
	}

	public void setCapStyle(final int capStyle) {
		_capStyle = capStyle;
	}

	public boolean getGraphicsExposures() {
		return _graphicsExposures;
	}
	public void setGraphicsExposures(final boolean graphicsExposures) {
		_graphicsExposures = graphicsExposures;
	}
	
	public void setArcMode(final int arcMode) {
		_arcMode = arcMode;
	}
	
	public int getArcMode() {
		return _arcMode;
	}

	public void setJoinStyle(final int joinStyle) {
		_joinStyle = joinStyle;
	}
	
	public int getJoinStyle() {
		return _joinStyle;
	}
	
	public void setFillStyle(final int fillStyle) {
		_fillStyle = fillStyle;
	}
	
	public int getFillStyle() {
		return _fillStyle;
	}

	public void setFillRule(final int fillRule) {
		_fillRule = fillRule;
	}
	
	public int getFillRule() {
		return _fillRule;
	}
	
	public void setTile(final Pixmap tilePixmap) {
		_tilePixmap = tilePixmap;
	}
	
	public Pixmap getTile() {
		return _tilePixmap;
	}
	
	public void setStipple(final Pixmap stipple) {
		_stipple = stipple;
	}
	
	public Pixmap getStipple() {
		return _stipple;
	}
	
	public void setTileStippleXOrigin(final int tileStippleXOrigin) {
		_tileStippleXOrigin = tileStippleXOrigin;
	}	
	
	public int getTileStippleXOrigin() {
		return _tileStippleXOrigin;
	}

	public void setTileStippleYOrigin(final int clipYOrigin) {
		this._tileStippleYOrigin = clipYOrigin;
	}	
	
	public int getTileStippleYOrigin() {
		return _tileStippleYOrigin;
	}

	public void setClipXOrigin(final int clipXOrigin) {
		_clipXOrigin = clipXOrigin;
	}
	
	public int getClipXOrigin() {
		return this._clipXOrigin;
	}

	public void setClipYOrigin(final int clipYOrigin) {
		_clipYOrigin = clipYOrigin;
	}
	
	public int getClipYOrigin() {
		return this._clipYOrigin;
	}

	public void setClipMask(final Pixmap clipMask) {
		_clipMask = clipMask;
	}
	
	public Pixmap getClipMask() {
		return this._clipMask;
	}

	public void setDashOffset(final int dashOffset) {
		_dashOffset = dashOffset;
	}

	public int getDashOffset() {
		return _dashOffset;
	}
	
	public void setFont(final Font font) {
		_font = font;
	}
	
	public Font getFont() {
		return _font;
	}

	public void setDashes(List<Integer> dashes) {
		_dashes = dashes;
	}
	
	public List<Integer> getDashes() {
		return _dashes;
	}
}
