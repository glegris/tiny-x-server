package com.liaquay.tinyx.model.font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.FontAlias;
import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.FontString;

public abstract class FontFactoryAdaptor implements FontFactory {
	
	private final List<FontString> _fontNames = new ArrayList<FontString>();
	private final List<FontAlias> _fontAliases = new ArrayList<FontAlias>();

	protected final void addFontName(final String name) {
		_fontNames.add(new FontString(name));
	}
	
	protected final void addFontAlias(final String alias, final String pattern) {
		_fontAliases.add(new FontAlias(alias, pattern));
	}
	
	@Override
	public final FontMatch getFirstMatchingFont(final String pattern) {
		final StringBuilder scratch = new StringBuilder();
		
		for(final FontAlias alias : _fontAliases) {
			final FontMatch match = alias.getFontMatch(pattern, scratch);
			if(match != null) return match;
		}
		
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
		
		for(final FontAlias alias : _fontAliases) {
			final FontMatch match = alias.getFontMatch(pattern, scratch);
			if(match != null) matchingFonts.add(match);
		}		
		
		for (int i = 0; i < _fontNames.size(); i++) {
			final FontMatch fontMatch = _fontNames.get(i).getFontMatch(pattern, scratch);
			if(fontMatch != null) matchingFonts.add(fontMatch);
		}
		return matchingFonts;
	}
	
	@Override
	public final FontDetail open(final FontMatch fontMatch) throws IOException {
		
		final StringBuilder scratch = new StringBuilder();
		for(final FontAlias alias : _fontAliases) {
			final FontMatch match = alias.getFontMatch(fontMatch.getMergedFontName(), scratch);
			if(match != null) {
				final String pattern = alias.getPattern();
				for (int i = 0; i < _fontNames.size(); i++) {
					final FontMatch fm = _fontNames.get(i).getFontMatch(pattern, scratch);
					if(fm != null) {
						return deAliasedOpen(fm);
					}
				}				
			}
		}
		
		return deAliasedOpen(fontMatch);
	}
	
	public abstract FontDetail deAliasedOpen(final FontMatch fontMatch) throws IOException;
}
