/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
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
package com.liaquay.tinyx.font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.FontFactory;
import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.FontString;

public abstract class FontFactoryAdaptor implements FontFactory {
	
	private final List<FontString> _fontNames = new ArrayList<FontString>();

	protected final void addFontName(final String name) {
		_fontNames.add(new FontString(name));
	}
	
	@Override
	public final FontMatch getFirstMatchingFont(final String pattern) {
		final StringBuilder scratch = new StringBuilder();
		
		for (int i = 0; i < _fontNames.size(); i++) {
			final FontMatch fontMatch = _fontNames.get(i).getFontMatch(pattern, scratch);
			if(fontMatch != null) return fontMatch;
		}

		return null;
	}
	
	@Override
	public final List<FontMatch> getMatchingFonts(final String pattern) {
		final List<FontMatch> matchingFonts = new ArrayList<FontMatch>();
		final StringBuilder scratch = new StringBuilder();
		
		for (int i = 0; i < _fontNames.size(); i++) {
			final FontMatch fontMatch = _fontNames.get(i).getFontMatch(pattern, scratch);
			if(fontMatch != null) matchingFonts.add(fontMatch);
		}
		
		return matchingFonts;
	}
	
	@Override
	public abstract FontDetail open(final FontMatch fontMatch) throws IOException;
}
