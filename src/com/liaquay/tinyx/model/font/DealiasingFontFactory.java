package com.liaquay.tinyx.model.font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.FontAlias;
import com.liaquay.tinyx.model.FontMatch;

public class DealiasingFontFactory implements FontFactory {
	
	private final List<FontAlias> _fontAliases = new ArrayList<FontAlias>();
	private final FontFactory _delegate;
	
	public DealiasingFontFactory(final FontFactory delegate) {
		_delegate = delegate;
	}

	public final void addFontAlias(final String alias, final String pattern) {
		_fontAliases.add(new FontAlias(alias, pattern));
	}
	
	@Override
	public final FontMatch getFirstMatchingFont(final String pattern) {
		final StringBuilder scratch = new StringBuilder();
		
		for(final FontAlias alias : _fontAliases) {
			final FontMatch match = alias.getFontMatch(pattern, scratch);
			if(match != null) return match;
		}

		return _delegate.getFirstMatchingFont(pattern);
	}
	
	@Override
	public final List<FontMatch> getMatchingFonts(final String pattern) {
		final List<FontMatch> matchingFonts = new ArrayList<FontMatch>();
		final StringBuilder scratch = new StringBuilder();
		
		for(final FontAlias alias : _fontAliases) {
			final FontMatch match = alias.getFontMatch(pattern, scratch);
			if(match != null) matchingFonts.add(match);
		}		
		matchingFonts.addAll(_delegate.getMatchingFonts(pattern));
		return matchingFonts;
	}
	
	@Override
	public final FontDetail open(final FontMatch fontMatch) throws IOException {
		
		final StringBuilder scratch = new StringBuilder();
		for(final FontAlias alias : _fontAliases) {
			final FontMatch match = alias.getFontMatch(fontMatch.getMergedFontName(), scratch);
			if(match != null) {
				final String pattern = alias.getPattern();
				final FontMatch fm = _delegate.getFirstMatchingFont(pattern);
				if(fm != null) return _delegate.open(fm);
			}
		}
		
		return _delegate.open(fontMatch);
	}

	@Override
	public void close(final FontDetail fontDetail) {
		_delegate.close(fontDetail);
	}
}
