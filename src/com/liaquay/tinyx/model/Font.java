package com.liaquay.tinyx.model;


public class Font extends AbstractResource {


	//    FOUNDRY: Type foundry - vendor or supplier of this font
	//    FAMILY_NAME: Typeface family
	//    WEIGHT_NAME: Weight of type
	//    SLANT: Slant (upright, italic, oblique, reverse italic, reverse oblique, or "other")
	//    SETWIDTH_NAME: Proportionate width (e.g. normal, condensed, narrow, expanded/double-wide)
	//    ADD_STYLE_NAME: Additional style (e.g. (Sans) Serif, Informal, Decorated)
	//    PIXEL_SIZE: Size of characters, in pixels; 0 (Zero) means a scalable font
	//    POINT_SIZE: Size of characters, in tenths of points
	//    RESOLUTION_X: Horizontal resolution in dots per inch (DPI), for which the font was designed
	//    RESOLUTION_Y: Vertical resolution, in DPI
	//    SPACING: monospaced, proportional, or "character cell"
	//    AVERAGE_WIDTH: Average width of characters of this font; 0 means scalable font
	//    CHARSET_REGISTRY: Registry defining this character set
	//    CHARSET_ENCODING: Registry's character encoding scheme for this set

	//	"-*-*-*-R-*-*-*-120-*-*-*-*-ISO8859-*"

	private String _foundry;

	private String _familyName;

	private String weightName;

	private Slant _slant;

	private String _setWidthName;

	private String _addStyleName;

	private int _pixelSize;

	private int _pointSize;

	private int _resolutionX;

	private int _resolutionY;

	private String _spacing;

	private int _averageWidth;

	private String _charsetRegistry;

	private String _charsetEncoding;

	private String _requestedName;

	private String _chosenFontName;

	public Font(int id, String requestedName) {
		super(id);

		String split[] = requestedName.split("-");
		// TODO Auto-generated method stub




		this._requestedName = requestedName;
	}

	public String getRequestedName() {
		return _requestedName;
	}

	@Override
	public void free() {
		// TODO Auto-generated method stub

	}

	//TODO: Needs proper matching code
	public boolean matches(String fontName) {
		if (true) { //getRequestedName().equals(fontName)) {
			return true;
		}
		return false;
	}

	public void setFont(String fontName) {
		_chosenFontName = fontName;
	}

	public String getFontName() {
		return _chosenFontName;
	}

	public int getMaxAscent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxDescent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMinWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDefaultChar() {
		return 32;
	}
}
