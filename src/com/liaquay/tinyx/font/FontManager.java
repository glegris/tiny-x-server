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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.FontFactory;
import com.liaquay.tinyx.model.FontMatch;

public class FontManager implements FontFactory {

	private final int _openFontCapacity;
	
	private static class FontDetailHolder {
		private final FontDetail _fontDetail;
		private int _count = 1;
		
		public FontDetailHolder(final FontDetail fontDetail) {
			_fontDetail = fontDetail;
		}
	}
	
	private final FontFactory _delegate;
	private final Map<String, FontDetailHolder> _openFonts = new TreeMap<String, FontDetailHolder>();
	private final Map<String, FontDetailHolder> _closedFonts = new TreeMap<String, FontDetailHolder>();
	
	public FontManager(
			final FontFactory delegate,
			final int openFontCapacity) {
		_delegate = delegate;
		_openFontCapacity = openFontCapacity;
	}
	
	@Override
	public FontMatch getFirstMatchingFont(final String pattern) {
		return _delegate.getFirstMatchingFont(pattern);
	}

	@Override
	public List<FontMatch> getMatchingFonts(final String pattern) {
		return _delegate.getMatchingFonts(pattern);
	}

	@Override
	public FontDetail open(final FontMatch fontMatch) throws IOException {
		final FontDetailHolder fdh1 = _openFonts.get(fontMatch.getMergedFontName());
		if(fdh1 != null) {
			fdh1._count++;
			_closedFonts.remove(fontMatch.getMergedFontName());
			return fdh1._fontDetail;
		}
		final FontDetail fontDetail = _delegate.open(fontMatch);
		if(fontDetail == null) return null;

		final FontDetailHolder fdh2 = new FontDetailHolder(fontDetail);
		_openFonts.put(fontMatch.getMergedFontName(), fdh2);
		int overCapacity = _openFonts.size() - _openFontCapacity;
		while(overCapacity > 0 && _closedFonts.size() > 0) {
			final FontDetailHolder fdh3 = _closedFonts.values().iterator().next();
			remove(fdh3);
		}
		return fontDetail;
	}
	
	private final void remove(final FontDetailHolder fdh) {
		final String fontName= fdh._fontDetail.getName();
		_delegate.close(fdh._fontDetail);
		_openFonts.remove(fontName);
		_closedFonts.remove(fontName);
	}
	
	@Override
	public void close(final FontDetail fontDetail) {
		final FontDetailHolder fdh1 = _openFonts.get(fontDetail.getName());
		if(fdh1 == null) return;
		if(fdh1._count <= 0) throw new RuntimeException("Closing already closed font");
		fdh1._count--;
		if(fdh1._count == 0) {
			if(_openFonts.size() > _openFontCapacity) {
				remove(fdh1);
			}
			else {
				_closedFonts.put(fontDetail.getName(), fdh1);
			}
		}
	}
}
