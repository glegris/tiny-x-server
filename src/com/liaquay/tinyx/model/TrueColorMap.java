package com.liaquay.tinyx.model;

public class TrueColorMap extends ColorMap {

	public TrueColorMap(int id) {
		super(id);
	}

	@Override
	public int getBlackPixel() {
		return 0xff000000;
	}

	@Override
	public int getWhitePixel() {
		return 0xffffffff;
	}

}
