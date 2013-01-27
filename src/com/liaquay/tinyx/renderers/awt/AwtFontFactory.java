/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Nathan Ludkin
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.renderers.awt;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.FontName;
import com.liaquay.tinyx.model.TextExtents;
import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.model.font.FontFactory;
import com.liaquay.tinyx.pcf.PcfFont;
import com.liaquay.tinyx.pcf.PcfFontFactory;
import com.liaquay.tinyx.pcf.PcfMetrics;

public class AwtFontFactory implements FontFactory {

	private final List<FontName> _fontNames;

	public AwtFontFactory() {
		_fontNames = initFontNames();
		_fontNames.addAll(initPcfFontNames());
	}

	private enum Slant {
		r,i
	}

	private enum Weight {
		medium, bold
	}

	private List<FontName> initFontNames() {
		final List<FontName> fontList = new ArrayList<FontName>();

		final GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Font[] fonts = e.getAllFonts(); // Get the fonts
		for (final Font f : fonts) {
			for(final Slant slant : Slant.values()) {
				for(final Weight weight : Weight.values()) {
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

					final FontName fontString = new FontName(fontInfo.toString());
					fontList.add(fontString);

					System.out.println(fontString);
				}
			}
		}
		return fontList;
	}

	@Override
	public FontInfo getFirstMatchingFont(final String pattern) {
		final StringBuilder scratch = new StringBuilder();
		for (int i = 0; i < _fontNames.size(); i++) {
			final FontInfo fontInfo = _fontNames.get(i).getFontInfo(pattern, scratch);
			if(fontInfo != null) return fontInfo;
		}

		return null;
	}

	@Override
	public List<FontInfo> getMatchingFonts(final String pattern) {
		try {
			final List<FontInfo> matchingFonts = new ArrayList<FontInfo>();
			final StringBuilder scratch = new StringBuilder();
			for (int i = 0; i < _fontNames.size(); i++) {
				final FontInfo fontInfo = _fontNames.get(i).getFontInfo(pattern, scratch);
				if(fontInfo != null) matchingFonts.add(fontInfo);
			}
			return matchingFonts;
		}
		catch(final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public FontDetail getFontDetail(final FontInfo fontInfo) {
		FontDetail fd = getPcfFontDefail(fontInfo);  
		if(fd != null) return fd;
		fd = getAwtFontDefail(fontInfo); 
		return fd;
	}
	
	private FontDetail getAwtFontDefail(final FontInfo fontInfo) {
		final String name = fontInfo.getFamilyName();
		final Font f = new Font(name, Font.PLAIN, fontInfo.getPixelSize());
		final FontMetrics fm = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(f);

		final TextExtents minBounds = new TextExtents(
				0, // Ascent
				0, // Descent
				0, // width
				0, // left
				0, // right,
				0  // attributes
				);
		
		final TextExtents maxBounds = new TextExtents(
				fm.getMaxAscent(), // Ascent
				fm.getMaxDescent(), // Descent
				fm.getMaxAdvance(), // width
				0, // left
				fm.getMaxAdvance(), // right,
				0  // attributes
				);
		
		final int defaultChar = 42; // TODO extract from font
		//TODO: Try and get the first and last character for this font from somewhere more meaningful
		final int firstChar = 32;
		final int lastChar = 255;
		final boolean leftToRight = true; //  TODO extract from font
		final int ascent = fm.getAscent();
		final int descent = fm.getDescent();
		
		return new FontDetail(minBounds, maxBounds, defaultChar, firstChar, lastChar, leftToRight, ascent, descent);
	}
	
	
//	6x10.pcf.gz -misc-fixed-medium-r-normal--10-100-75-75-c-60-iso10646-1
	private Map<FontInfo, PcfFont> _pcfFonts = new TreeMap<FontInfo, PcfFont>();
	
	private FontDetail getPcfFontDefail(final FontInfo fontInfo) {
		final PcfFont font = _pcfFonts.get(fontInfo);
		if(font == null) return null;
		
		final TextExtents minBounds = pcfMetricsToTextExtents(font.getMinBounds());
		final TextExtents maxBounds = pcfMetricsToTextExtents(font.getMaxBounds());
		final int defaultChar = font.getDefaultCharacter();
		final int firstChar = font.getMinCharacter();
		final int lastChar = font.getMaxCharacter();
		final boolean leftToRight = font.isDrawLeftToRight();
		final int ascent = font.getAscent();
		final int descent = font.getDescent();
		
		return new FontDetail(minBounds, maxBounds, defaultChar, firstChar, lastChar, leftToRight, ascent, descent);
	}
	
	private final TextExtents pcfMetricsToTextExtents(final PcfMetrics pcfMetrics) {
		return new TextExtents(
				pcfMetrics.getAscent(), // Ascent
				pcfMetrics.getDescent(), // Descent
				pcfMetrics.getWidth(), // width
				pcfMetrics.getLeftSideBearing(), // left
				pcfMetrics.getRightSideBearing(), // right,
				pcfMetrics.getAttributes()  // attributes
				);
	}
	
	// -misc-fixed-medium-r-normal--10-100-75-75-c-60-iso10646-1
	private List<FontName> initPcfFontNames() {
		final List<FontName> fontList = new ArrayList<FontName>();
		try {
			final FileInputStream fileInputStream = new FileInputStream("/usr/share/fonts/X11/misc/fixed.pcf.gz");
			final GZIPInputStream zipInputStream = new GZIPInputStream(fileInputStream);
			try {
				final PcfFont font = PcfFontFactory.read(zipInputStream);
				final FontName fontName = new FontName("-misc-fixed-medium-r-normal--10-100-75-75-c-60-iso10646-1");
				fontList.add(fontName);
				_pcfFonts.put(fontName.getFontInfo("*"), font);
			}
			finally {
				fileInputStream.close();
			}
		}
		catch(final Exception e) {
			e.printStackTrace();
		}

		return fontList;
	}
	
	public com.liaquay.tinyx.model.Font.Listener fontOpened(final com.liaquay.tinyx.model.Font font) {
		final FontInfo fontInfo = font.getFontInfo();
		final PcfFont pcfFont = _pcfFonts.get(fontInfo);
		if(pcfFont != null) {
			return new XawtPcfFontListener(pcfFont);
		}
		return new XawtNativeFontListener(font.getFontInfo());
	}
}
