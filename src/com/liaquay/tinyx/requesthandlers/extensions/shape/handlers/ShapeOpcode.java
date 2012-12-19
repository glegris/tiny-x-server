package com.liaquay.tinyx.requesthandlers.extensions.shape.handlers;


public enum ShapeOpcode {
	Set,
	Union,
	Intersect,
	Subtract,
	Invert;

	public static ShapeOpcode getFromIndex(final int index) {
		final ShapeOpcode[] values = values();
		if (index<values.length && index>=0) return values[index];
		return null;
	}
}