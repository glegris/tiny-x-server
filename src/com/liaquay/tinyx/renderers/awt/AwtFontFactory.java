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
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.FontName;
import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.model.font.FontFactory;

public class AwtFontFactory implements FontFactory {

	private final List<FontName> _fontNames;

	public AwtFontFactory() {
		_fontNames = initFontNames();
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
		}catch(final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public FontDetail getFontDetail(final FontInfo fontInfo) {

		final String name = fontInfo.getFamilyName();
		final Font f = new Font(name, Font.PLAIN, fontInfo.getPixelSize());
		final FontMetrics fm = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(f);

		//TODO: Try and get the first and last character for this font from somewhere more meaningful
		final int firstChar = 32;
		final int lastChar = 255;

		//		final Rectangle2D maxBounds = f.getMaxCharBounds(new FontRenderContext(null, true, false));

		final FontDetail fd = new FontDetail(
				fm.getMaxAscent(),
				fm.getMaxDescent(),
				0, // Minimum width
				fm.getMaxAdvance(), // Maximum width
				f.getMissingGlyphCode(),
				firstChar, // First char
				lastChar, // Last char
				fm.getHeight(),
				fm.getLeading());

		return fd;
	}
}
