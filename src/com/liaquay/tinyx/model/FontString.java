package com.liaquay.tinyx.model;


public class FontString {

	//	"-*-*-*-R-*-*-*-120-*-*-*-*-ISO8859-*"

	//    FOUNDRY: Type foundry - vendor or supplier of this font
	private String _foundry;

	//    FAMILY_NAME: Typeface family
	private String _familyName;

	//    WEIGHT_NAME: Weight of type
	private String _weightName;

	//    SLANT: Slant (upright, italic, oblique, reverse italic, reverse oblique, or "other")
	private String _slant;

	//    SETWIDTH_NAME: Proportionate width (e.g. normal, condensed, narrow, expanded/double-wide)
	private String _setWidthName;

	//    ADD_STYLE_NAME: Additional style (e.g. (Sans) Serif, Informal, Decorated)
	private String _addStyleName;

	//    PIXEL_SIZE: Size of characters, in pixels; 0 (Zero) means a scalable font
	private int _pixelSize;

	//    POINT_SIZE: Size of characters, in tenths of points
	private int _pointSize;

	//    RESOLUTION_X: Horizontal resolution in dots per inch (DPI), for which the font was designed
	private int _resolutionX;

	//    RESOLUTION_Y: Vertical resolution, in DPI
	private int _resolutionY;

	//    SPACING: monospaced, proportional, or "character cell"
	private String _spacing;

	//    AVERAGE_WIDTH: Average width of characters of this font; 0 means scalable font
	private int _averageWidth;

	//    CHARSET_REGISTRY: Registry defining this character set
	private String _charsetRegistry;

	//    CHARSET_ENCODING: Registry's character encoding scheme for this set
	private String _charsetEncoding;

	private String _requestedName;

	private String _chosenFontName;

	public FontString(String name) {
		String split[] = name.split("-");
		if (split.length == 1) {
			// Just a font family name has been specified such as "cursor"

			if (name.equalsIgnoreCase("cursor") || name.equalsIgnoreCase("fixed")) {
				setFamilyName("bitstream charter");
			} else {
				setFamilyName(name);
			}
		} else if (split.length == 15){
			setFoundry(split[1]);
			setFamilyName(split[2]);
			setWeightName(split[3]);
			setSlant(split[4]);
			setWidthName(split[5]);
			setAddStyleName(split[6]);
			try {
				setPixelSize(Integer.parseInt(split[7]));
			} catch (NumberFormatException e) {
			}
			try {
				setPointSize(Integer.parseInt(split[8]));
			} catch (NumberFormatException e) {
			}
			try {
				setResolutionX(Integer.parseInt(split[9]));
			} catch (NumberFormatException e) {
			}
			try {
				setResolutionY(Integer.parseInt(split[10]));
			} catch (NumberFormatException e) {
			}
			setSpacing(split[11]);
			try {
				setAverageWidth(Integer.parseInt(split[12]));
			} catch (NumberFormatException e) {
			}
			setCharsetRegistry(split[13]);
			setCharsetEncoding(split[14]);
		} else {
			System.out.println("Kaboom: Name: " + name + "  Len: " + split.length);
		}
	}

	public FontString() {
		// TODO Auto-generated constructor stub
	}

	//TODO: Needs proper matching code
	public boolean matches(FontString fontName) {

		if (fontName.getFamilyName().equalsIgnoreCase(getFamilyName())) {
			return true;
		}

		return false;
	}

	public String getFoundry() {
		return _foundry;
	}

	public void setFoundry(String foundry) {
		this._foundry = foundry;
	}

	public String getFamilyName() {
		return _familyName;
	}

	public void setFamilyName(String familyName) {
		this._familyName = familyName;
	}

	public String getWeightName() {
		return _weightName;
	}

	public void setWeightName(String weightName) {
		this._weightName = weightName;
	}

	public String getSlant() {
		return _slant;
	}

	public void setSlant(String slant) {
		this._slant = slant;
	}

	public String getWidthName() {
		return _setWidthName;
	}

	public void setWidthName(String setWidthName) {
		this._setWidthName = setWidthName;
	}

	public String getAddStyleName() {
		return _addStyleName;
	}

	public void setAddStyleName(String addStyleName) {
		this._addStyleName = addStyleName;
	}

	public int get_pixelSize() {
		return _pixelSize;
	}

	public void setPixelSize(int pixelSize) {
		this._pixelSize = pixelSize;
	}

	public int getPointSize() {
		return _pointSize;
	}

	public void setPointSize(int pointSize) {
		this._pointSize = pointSize;
	}

	public int getResolutionX() {
		return _resolutionX;
	}

	public void setResolutionX(int resolutionX) {
		this._resolutionX = resolutionX;
	}

	public int getResolutionY() {
		return _resolutionY;
	}

	public void setResolutionY(int resolutionY) {
		this._resolutionY = resolutionY;
	}

	public String getSpacing() {
		return _spacing;
	}

	public void setSpacing(String spacing) {
		this._spacing = spacing;
	}

	public int getAverageWidth() {
		return _averageWidth;
	}

	public void setAverageWidth(int averageWidth) {
		this._averageWidth = averageWidth;
	}

	public String getCharsetRegistry() {
		return _charsetRegistry;
	}

	public void setCharsetRegistry(String charsetRegistry) {
		this._charsetRegistry = charsetRegistry;
	}

	public String getCharsetEncoding() {
		return _charsetEncoding;
	}

	public void setCharsetEncoding(String charsetEncoding) {
		this._charsetEncoding = charsetEncoding;
	}

	public String getRequestedName() {
		return _requestedName;
	}

	public void setRequestedName(String requestedName) {
		this._requestedName = requestedName;
	}

	public String getChosenFontName() {
		return _chosenFontName;
	}

	public void setChosenFontName(String chosenFontName) {
		this._chosenFontName = chosenFontName;
	}

	public String toString() {
		return "-" + getFoundry() + "-" +
				getFamilyName() + "-" +
				getWeightName() + "-" +
				getSlant() + "-" +
				getWidthName() + "-" +
				getAddStyleName() + "-" +
				get_pixelSize() + "-" +
				getPointSize() + "-" +
				getResolutionX() + "-" +
				getResolutionY() + "-" +
				getSpacing() + "-" +
				getAverageWidth() + "-" +
				getCharsetRegistry() + "-" +
				getCharsetEncoding();
	}

	//	public int getMaxAscent() {
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}
	//
	//	public int getMaxWidth() {
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}
	//
	//	public int getMaxDescent() {
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}
	//
	//	public int getMinWidth() {
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}
	//
	//	public int getDefaultChar() {
	//		return 32;
	//	}


}
