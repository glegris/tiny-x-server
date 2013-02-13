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

public class CompoundFontFactory implements FontFactory {

	private final FontFactory[] _delegates;
	
	public CompoundFontFactory(final FontFactory[] delegates) {
		_delegates = delegates;
	}
	
	@Override
	public FontMatch getFirstMatchingFont(final String pattern) {
		for(final FontFactory delegate : _delegates) {
			final FontMatch fontMatch = delegate.getFirstMatchingFont(pattern);
			if(fontMatch != null) return fontMatch;
		}
		return null;
	}

	@Override
	public List<FontMatch> getMatchingFonts(final String pattern) {
		final List<FontMatch> matches = new ArrayList<FontMatch>();
		for(final FontFactory delegate : _delegates) {
			matches.addAll(delegate.getMatchingFonts(pattern));
		}
		return matches;
	}

	@Override
	public FontDetail open(final FontMatch fontInfo) throws IOException {
		for(final FontFactory delegate : _delegates) {
			final FontDetail fontDetail = delegate.open(fontInfo);
			if(fontDetail != null) return fontDetail;
		}
		return null;
	}

	@Override
	public void close(final FontDetail fontDetail) {
		for(final FontFactory delegate : _delegates) {
			delegate.close(fontDetail);
		}
	}
}
