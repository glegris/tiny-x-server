package com.liaquay.tinyx.font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.FontAlias;
import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.FontFactory;
import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.FontString;

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
			if(match != null){
				return match;
			}
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
				final String pattern;
				
				if( alias.getPattern().startsWith("-") && match.getMergedFontName().startsWith("-")) {
					final FontInfo fi1 = match.getFontInfo();
					if(fi1 != null) {
					final FontString fs = new FontString(alias.getPattern());
					final FontMatch fm = fs.getFontMatch("-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
					final FontInfo fi2 = fm.getFontInfo();
					pattern =  
							"-" + fi2.getFoundry() + 
							"-" + fi2.getFamilyName() + 
							"-" + fi2.getWeightName() +
							"-" + fi2.getSlant() +
							"-" + fi2.getWidthName() + 
							"-" + fi2.getAddStyleName() + 
							"-" + fi1.getPixelSize() + 
							"-" + fi1.getPointSize() + 
							"-" + fi1.getResolutionX() + 
							"-" + fi1.getResolutionY() + 
							"-" + fi2.getSpacing() + 
							"-" + fi1.getAverageWidth() + 
							"-" + fi2.getCharsetRegistry() + 
							"-" + fi2.getCharsetEncoding();
					}
					else {
						pattern = alias.getPattern();
					}
				}
				else {
					pattern = alias.getPattern();
				}
				
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
	
	public static void main(final String []args) {
		final FontString fontString = new FontString("-adobe-symbol-b-i-normal--0-0-0-0-p-0-adobe-fontspecific");
		final FontMatch match = fontString.getFontMatch("-adobe-symbol-b-i-normal--12-13-14-15-p-16-adobe-fontspecific");
		final FontInfo fi1 = match.getFontInfo();
		final String pattern = "-adobe-symbol-b-b-normal--0-0-0-0-p-0-adobe-symbol";
		final FontString fs = new FontString(pattern);
		final FontMatch fm = fs.getFontMatch("-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		final FontInfo fi2 = fm.getFontInfo();
		final String p2 =  
				"-" + fi2.getFoundry() + 
				"-" + fi2.getFamilyName() + 
				"-" + fi2.getWeightName() +
				"-" + fi2.getSlant() +
				"-" + fi2.getWidthName() + 
				"-" + fi2.getAddStyleName() + 
				"-" + fi1.getPixelSize() + 
				"-" + fi1.getPointSize() + 
				"-" + fi1.getResolutionX() + 
				"-" + fi1.getResolutionY() + 
				"-" + fi2.getSpacing() + 
				"-" + fi1.getAverageWidth() + 
				"-" + fi2.getCharsetRegistry() + 
				"-" + fi2.getCharsetEncoding();

		System.out.println(p2);
	}
}
