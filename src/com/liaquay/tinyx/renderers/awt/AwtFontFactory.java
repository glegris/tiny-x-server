package com.liaquay.tinyx.renderers.awt;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.FontString;
import com.liaquay.tinyx.model.font.FontFactory;

public class AwtFontFactory implements FontFactory {

	List<FontString> _fontNames;
	
	public AwtFontFactory() {
		_fontNames = initFontNames();
	}
	
	private List<FontString> initFontNames() {
		List<FontString> fontList = new ArrayList<FontString>();
		
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts(); // Get the fonts
		for (Font f : fonts) {
			String familyName = f.getFamily();
			System.out.println(f);
			
			String foundryName = "*";
			String charSet = "ISO8859";
			String pointSize = "*";
			String weightName = "*";
			
			FontString fontString = new FontString("-" + foundryName + "-" + familyName + "-"+ weightName + "-" + "R" + "-*-*-*-" + pointSize + "-*-*-*-*-" + charSet + "-*");
			fontList.add(fontString);
		}
		return fontList;
	}
	
	@Override
	public List<FontString> getFontNames() {
		return _fontNames;
	}

	@Override
	public FontString getFirstMatchingFont(String requestedFontName) {
		final FontString requestedFont = new FontString(requestedFontName);

		for (int i = 0; i < _fontNames.size(); i++) {
			if (_fontNames.get(i).matches(requestedFont)) {
				return _fontNames.get(i);
			}

		}
		
		//TODO: Nasty hack until I have proper matching code in place
		return _fontNames.get(0);
	}
	
	@Override
	public List<FontString> getMatchingFonts(String requestedFontName) {
		FontString requestedFont = new FontString(requestedFontName);

		List<FontString> matchingFonts = new ArrayList<FontString>();
		
		for (int i = 0; i < _fontNames.size(); i++) {
			matchingFonts.add(_fontNames.get(i));
		}
		
		return matchingFonts;
	}

	@Override
	// THis method will be used to 
	public void getFontDetails() {
		
	}

}
