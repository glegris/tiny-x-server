package com.liaquay.tinyx.renderers.awt;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.Slant;
import com.liaquay.tinyx.model.font.FontFactory;

public class AwtFontFactory implements FontFactory {

	List<String> _fontNames;
	
	public AwtFontFactory() {
		_fontNames = initFontNames();
	}
	
	private List<String> initFontNames() {
		List<String> fontList = new ArrayList<String>();
		
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts(); // Get the fonts
		for (Font f : fonts) {
			String familyName = f.getFamily();
			System.out.println(f);
			
			String foundryName = "*";
			String charSet = "ISO8859";
			String pointSize = "*";
			String weightName = "*";
			
			fontList.add("-" + foundryName + "-" + familyName + "-"+ weightName + "-" + Slant.R.toString() + "-*-*-*-" + pointSize + "-*-*-*-*-" + charSet + "-*");
		}
		return fontList;
	}
	
	@Override
	public List<String> getFontNames() {
		return _fontNames;
	}

}
