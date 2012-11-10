package com.liaquay.tinyx.renderers.awt;

import java.awt.Frame;

import com.liaquay.tinyx.model.Screen;

public class XawtScreen extends Frame {

	private static final long serialVersionUID = 4774093786430120797L;

	public XawtScreen(final Screen screen) {
		setSize(screen.getWidthPixels(), screen.getHeightPixels());
	}
}
