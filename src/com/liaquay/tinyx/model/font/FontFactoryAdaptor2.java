package com.liaquay.tinyx.model.font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.FontString;

public abstract class FontFactoryAdaptor2 implements FontFactory {
	
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
