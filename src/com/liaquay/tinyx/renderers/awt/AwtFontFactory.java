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
import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.model.font.FontFactory;

public class AwtFontFactory implements FontFactory {

	private final List<FontInfo> _fontNames;
	
	public AwtFontFactory() {
		_fontNames = initFontNames();
	}
	
	private List<FontInfo> initFontNames() {
		final List<FontInfo> fontList = new ArrayList<FontInfo>();
		
		final GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Font[] fonts = e.getAllFonts(); // Get the fonts
		for (final Font f : fonts) {
			final String familyName = f.getFamily();
			
			String foundryName = "*";
			String charSet = "ISO8859";
			String pointSize = "*";
			String weightName = "*";
			
			// TODO PS - Add new constructor
			final FontInfo fontString = new FontInfo("-" + foundryName + "-" + familyName + "-"+ weightName + "-" + "*" + "-*-*-*-" + pointSize + "-*-*-*-*-" + charSet + "-*");
			fontList.add(fontString);
			
			System.out.println(fontString);
		}
		return fontList;
	}
	
	@Override
	public List<FontInfo> getFontNames() {
		return _fontNames;
	}

	@Override
	public FontInfo getFirstMatchingFont(final FontInfo requestedFont) {
		
		// TODO is there a more efficient way to do this?
		for (int i = 0; i < _fontNames.size(); i++) {
			if (_fontNames.get(i).matches(requestedFont)) {
				return _fontNames.get(i);
			}
		}
		
		return null;
	}
	
	@Override
	public List<FontInfo> getMatchingFonts(final FontInfo requestedFont) {

		final List<FontInfo> matchingFonts = new ArrayList<FontInfo>();
		
		for (int i = 0; i < _fontNames.size(); i++) {
			matchingFonts.add(_fontNames.get(i));
		}
		
		return matchingFonts;
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
				32, // First char
				255, // Last char
				fm.getHeight(),
				fm.getLeading());
		
		return fd;
	}
}
