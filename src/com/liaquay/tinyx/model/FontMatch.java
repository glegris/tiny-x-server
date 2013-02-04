package com.liaquay.tinyx.model;

public class FontMatch {
	
	private final String _fontName;
	private final String _mergedFontName;
	private FontInfo _fontInfo = null;
	
	public FontMatch(final String fontName, final String mergedFontName){
		_fontName = fontName;
		_mergedFontName = mergedFontName;
	}
	
	public String getFontName() {
		return _fontName;
	}
	
	public String getMergedFontName() {
		return _mergedFontName;
	}
	
	public boolean isFielded() {
		return _fontName.startsWith("-");
	}
	
	public FontInfo getFontInfo() {
		if(_fontInfo != null)return _fontInfo;
		if(!isFielded()) return null;
		final String[] split = _mergedFontName.split("-");
		final String[] snn = new  String[Math.max(14, split.length-1)];
		for(int i=0; i <split.length-1; ++i) snn[i] = split[i+1];

		final String foundry = parseString(snn[0]);
		final String familyName = parseString(snn[1]);
		final String weightName = parseString(snn[2]);
		final String slant = parseString(snn[3]);
		final String setWidthName = parseString(snn[4]);
		final String addStyleName = parseString(snn[5]);
		final int pixelSize = parseInt(snn[6]);
		final int pointSize = parseInt(snn[7]);
		final int resolutionX = parseInt(snn[8]);
		final int resolutionY = parseInt(snn[9]);
		final String spacing = parseString(snn[10]);
		final int averageWidth = parseInt(snn[11]);
		final String charsetRegistry = parseString(snn[12]);
		final String charsetEncoding = parseString(snn[13]);

		_fontInfo = new FontInfo(
				foundry,
				familyName, 
				weightName,
				slant, 
				setWidthName, 
				addStyleName,
				pixelSize, 
				pointSize, 
				resolutionX, 
				resolutionY,
				spacing, 
				averageWidth, 
				charsetRegistry,
				charsetEncoding);
		
		return _fontInfo;
	}
	
	private static String parseString(final String s) {
		return s == null ? "" : s;
	}

	private static int parseInt(final String s) {
		try {
			return s == null ? 0 : Integer.parseInt(s);
		}
		catch(final Exception e) { 
			return 0;
		}
	}
	
	public static void main(final String[] a) {
		final FontString n = new FontString("-misc-Arial-medium-r-normal--12-100-75-75-c-60-iso8859-1");
		final FontMatch m = n.getFontMatch("*");
		final FontInfo i = m.getFontInfo();
		System.out.println(i);
	}
}
