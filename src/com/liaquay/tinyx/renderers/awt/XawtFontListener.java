package com.liaquay.tinyx.renderers.awt;

import com.liaquay.tinyx.model.*;

public class XawtFontListener implements Font.Listener {
	
	final java.awt.Font _awtFont;
	
	@Override
	public void fontClosed(final Font font) {
		// TODO Close the font?
	}

	public XawtFontListener(final java.awt.Font awtFont) {
		_awtFont = awtFont;
	}
	
	public java.awt.Font getAwtFont() {
		return _awtFont;
	}
}
