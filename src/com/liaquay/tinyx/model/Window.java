package com.liaquay.tinyx.model;

import com.liaquay.tinyx.util.Tree;

public class Window extends Tree<Window> implements Drawable {

	private final int _resourceId;
	
	public Window(final int resourceId, final Window parent) {
		super(parent);
		_resourceId = resourceId;
	}

	@Override
	public int getId() {
		return _resourceId;
	}

}
