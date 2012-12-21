package com.liaquay.tinyx.requesthandlers;


public enum CoordMode {
	Origin,
	Previous;

	public static CoordMode getFromIndex(final int index) {
		final CoordMode[] values = values();
		if (index<values.length && index>=0) return values[index];
		return null;
	}
}