package com.liaquay.tinyx.requesthandlers.extensions.shape.handlers;


public enum ShapeKind {
	Bounding,
	Clip;

	public static ShapeKind getFromIndex(final int index) {
		final ShapeKind[] values = values();
		if (index<values.length && index>=0) return values[index];
		return null;
	}
}