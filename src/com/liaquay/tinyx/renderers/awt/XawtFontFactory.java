package com.liaquay.tinyx.renderers.awt;

import java.io.IOException;

import com.liaquay.tinyx.model.font.CompoundFontFactory;
import com.liaquay.tinyx.model.font.FontFactory;
import com.liaquay.tinyx.model.font.FontManager;

public class XawtFontFactory extends CompoundFontFactory{

	public XawtFontFactory() throws IOException {
		super(new FontFactory[]{
				new FontManager(new XawtPcfFontFactory("/usr/share/fonts/X11/misc/"), 50),
				new XawtNativeFontFactory()
		});
	}
}
