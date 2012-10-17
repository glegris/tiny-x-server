package com.liaquay.tinyx.model;

public class Format {

	private final int _depth;
	private final int _bpp;
	private final int _scanLinePad;

	public Format(final int depth, final int bpp, final int scanLinePad) {
		_depth = depth; 
		_bpp = bpp; 
		_scanLinePad = scanLinePad;
	}

	public int getDepth() {
		return _depth;
	}

	public int getBpp() {
		return _bpp;
	}

	public int getScanLinePad() {
		return _scanLinePad;
	}
}