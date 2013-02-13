package com.liaquay.tinyx.renderers.awt;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.liaquay.tinyx.font.FontFactoryAdaptor;
import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.FontMatch;

public class XawtNativeFontFactory extends FontFactoryAdaptor {
	
	public XawtNativeFontFactory() {
		
//		addFontAlias("variable", "-*-Arial-medium-r-normal-*-*-120-*-*-*-*-iso8859-1");
		
		final GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Font[] fonts = e.getAllFonts(); // Get the fonts
		for (final Font f : fonts) {
			for(final FontDetail.Slant slant : FontDetail.Slant.values()) {
				for(final FontDetail.Weight weight :FontDetail. Weight.values()) {
					final String foundry = "";
					final String familyName = f.getFamily();
					final String setWidthName ="normal";
					final String addStyleName = "";
					final int pixelSize = 0;
					final int pointSize = 0;
					final int resolutionX = 0;
					final int resolutionY = 0;
					final String spacing = "c";
					final int averageWidth = 0;
					final String charsetRegistry = "ISO8859";
					final String charsetEncoding ="1";

					final FontInfo fontInfo = new FontInfo(
							foundry,
							familyName, 
							weight.name(),
							slant.name(), 
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

					addFontName(fontInfo.toString());

					System.out.println(fontInfo.toString());
				}
			}
		}
		
		for(final FontDetail.Slant slant : FontDetail.Slant.values()) {
			for(final FontDetail.Weight weight :FontDetail. Weight.values()) {
				addFontName("-adobe-symbol-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-adobe-symbol");
				addFontName("-adobe-helvetica-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-iso8859-1");
				addFontName("-adobe-times new roman-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-iso8859-1");
				addFontName("-adobe-times-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-iso8859-1");
				addFontName("-adobe-courier-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-iso8859-1");
				addFontName("-adobe-helvetic-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-iso8859-1");
				addFontName("-adobe-new century schoolbook-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-iso8859-1");
			}
		}
	}

	private static final Map<String, String> FAMILY_MAP = new TreeMap<String, String>();
	
	static {
		FAMILY_MAP.put("symbol", "Monospaced");
		FAMILY_MAP.put("times", "Serif");
		FAMILY_MAP.put("times new roman", "Serif");
		FAMILY_MAP.put("new century schoolbook", "Serif");
		FAMILY_MAP.put("helvetica", "SansSerif");
		FAMILY_MAP.put("helvetic", "SansSerif");
		FAMILY_MAP.put("courier", "SansSerif");
	}
	
	private static final String translateFamilyName(final String name) {
		final String translation = FAMILY_MAP.get(name);
		if(translation != null) {
			return translation;
		}
		return name;
	}
	
	@Override
	public FontDetail open(final FontMatch fontMatch) throws IOException {
		if(!fontMatch.isFielded()) return null; // Only cope with TTF with nice long names for now.
		final FontInfo fontInfo = fontMatch.getFontInfo();
		final String fontFamily = translateFamilyName(fontInfo.getFamilyName());
		final int fontSize = 
				fontInfo.getPixelSize() > 0 ? fontInfo.getPixelSize() :
					fontInfo.getPointSize() > 0 ? fontInfo.getPointSize()/10 :
						12;
		final String fontWeight = fontInfo.getWeightName();
		final String fontSlant = fontInfo.getSlant();
		final int fontStyle = 
				((fontWeight != null && fontWeight.equalsIgnoreCase("bold")) ? java.awt.Font.BOLD : 0) |
				((fontSlant != null && fontSlant.equalsIgnoreCase("i")) ? java.awt.Font.ITALIC : 0);
		
		final Font awtFont = new java.awt.Font(fontFamily, fontStyle, fontSize == 0 ? 12 : fontSize);
		
		return new XawtNativeFontDetail(fontInfo, awtFont);
	}

	@Override
	public void close(final FontDetail fontDetail) {
		// Do nothing
	}
}
