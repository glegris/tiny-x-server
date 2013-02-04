package com.liaquay.tinyx.font;

import java.io.IOException;
import java.util.List;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.model.font.FontFactory;
import com.liaquay.tinyx.x11font.FontEncoding;
import com.liaquay.tinyx.x11font.FontEncodingFactory;

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
		return new EncodingFontDetail(fontDetail, encoding);
	}

	public void close(final FontDetail fontDetail) {
		_delegate.close(fontDetail);
	}

	private FontEncoding getEncoding(final FontMatch fontMatch) throws IOException {
		final String encoding;
		if(fontMatch.isFielded()) {
			final FontInfo fontInfo = fontMatch.getFontInfo();
			encoding = fontInfo.getCharsetRegistry().toLowerCase() + "-" + fontInfo.getCharsetEncoding().toLowerCase();
		}
		else {
			encoding = "iso8859-1";
		}
				
		return _encodingFactory.open(encoding);
	}
}
