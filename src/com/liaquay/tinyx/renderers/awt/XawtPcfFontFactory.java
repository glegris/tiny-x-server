package com.liaquay.tinyx.renderers.awt;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.model.font.FontFactoryAdaptor;
import com.liaquay.tinyx.pcf.PcfFont;
import com.liaquay.tinyx.pcf.PcfFontFactory;
import com.liaquay.tinyx.x11font.FontDirReader;

public class XawtPcfFontFactory  extends FontFactoryAdaptor {
	
	private final static Logger LOGGER = Logger.getLogger(XawtPcfFontFactory.class.getName());

	private final Map<String, String> _fontNameToFileName = new TreeMap<String, String>();
	
	public XawtPcfFontFactory(final String fontDir) throws IOException {
		FontDirReader.read(fontDir, new FontDirReader.Listener() {
			
			@Override
			public void font(final String fileName, final String fontName) {
				_fontNameToFileName.put(fontName, fileName);
				addFontName(fontName);
			}
			
			@Override
			public void alias(final String alias, final String pattern) {
				addFontAlias(alias, pattern);
			}
		});
	}

	@Override
	public FontDetail deAliasedOpen(final FontMatch fontMatch) throws IOException {
		
		final String fileName = _fontNameToFileName.get(fontMatch.getMergedFontName());
		if(fileName == null) {
			return null;
		}
		final PcfFont font = PcfFontFactory.read(fileName);
		final FontInfo fontInfo;
		if(fontMatch.isFielded()) {
			fontInfo = fontMatch.getFontInfo();
		}
		else {
			
			final String foundry = "";
			final String familyName = ""; 
			final String weightName = "";
			final String slant = ""; 
			final String setWidthName = ""; 
			final String addStyleName = "";
			final int pixelSize = 0; 
			final int pointSize = 0; 
			final int resolutionX = 0; 
			final int resolutionY = 0;
			final String spacing = ""; 
			final int averageWidth = 0; 
			final String charsetRegistry = "";
			final String charsetEncoding = "";
			
			fontInfo  = new FontInfo(
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
		}
		
		return new XawtPcfFontDetail(
				fontMatch.getMergedFontName(), 
				font, 
				fontInfo);
	}

	@Override
	public void close(final FontDetail fontDetail) {
	}
}
