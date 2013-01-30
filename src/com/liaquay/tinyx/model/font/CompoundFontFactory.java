package com.liaquay.tinyx.model.font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
