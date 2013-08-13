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
package com.liaquay.tinyx.x11font;

import java.io.IOException;
import java.util.List;

import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.FontFactory;
import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.FontMatch;

public class EncodingFontFactory implements FontFactory {

	private final FontFactory _delegate;
	private final FontEncodingFactory _encodingFactory;
	
	public EncodingFontFactory(
			final FontFactory delegate,
			final FontEncodingFactory encodingFactory) {
		
		_delegate = delegate;
		_encodingFactory = encodingFactory;
	}

	public FontMatch getFirstMatchingFont(final String pattern) {
		return _delegate.getFirstMatchingFont(pattern);
	}

	public List<FontMatch> getMatchingFonts(final String pattern) {
		return _delegate.getMatchingFonts(pattern);
	}

	public FontDetail open(final FontMatch fontMatch) throws IOException {
		final FontDetail fontDetail = _delegate.open(fontMatch);
		if(fontDetail == null) return null;
		final FontEncoding encoding = getEncoding(fontMatch);
		if(encoding == null) return fontDetail;
		return new EncodingFontDetail(fontDetail, encoding);
	}

	public void close(final FontDetail fontDetail) {
		_delegate.close(fontDetail);
	}

	private FontEncoding getEncoding(final FontMatch fontMatch) throws IOException {
		final String encoding;
		if(fontMatch.isFielded()) {
			final FontInfo fontInfo = fontMatch.getFontInfo();
			final String charsetRegistry = fontInfo.getCharsetRegistry().toLowerCase();
			final String charsetEncoding = fontInfo.getCharsetEncoding().toLowerCase();
			if(charsetRegistry.equals("fontspecific") || charsetEncoding.equals("fontspecific")) return null;
			encoding = fontInfo.getCharsetRegistry().toLowerCase() + "-" + fontInfo.getCharsetEncoding().toLowerCase();
		}
		else {
			encoding = "iso8859-1";
		}
		return _encodingFactory.open(encoding);
	}
}
