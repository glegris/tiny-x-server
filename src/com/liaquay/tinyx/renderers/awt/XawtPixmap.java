package com.liaquay.tinyx.renderers.awt;

import com.liaquay.tinyx.model.Pixmap;

public class XawtPixmap extends XawtDrawableListener implements Pixmap.Listener {

	public XawtPixmap(final Pixmap pixmap) {
		super(pixmap);
	}
}
