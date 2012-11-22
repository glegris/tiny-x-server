package com.liaquay.tinyx.model.font;

import java.util.List;

import com.liaquay.tinyx.model.FontString;

public interface FontFactory {
	List<FontString> getFontNames();

	FontString getFirstMatchingFont(String requestedFontName);

	List<FontString> getMatchingFonts(String pattern);
}
