package com.liaquay.tinyx.model;

public class Pallette extends ColorMap {
	
	private int[] _red;
	private int[] _green;
	private int[] _blue;

	public Pallette(final int id) {
		super(id);
	}

	@Override
	public int getBlackPixel() {
		return 0; // TODO
	}

	@Override
	public int getWhitePixel() {
		return 0; // TODO
	}
	
	public boolean isValidColor(final int pixel) {
		return pixel >= 0 && pixel < _red.length;
	}
	
	public void getColor(final int pixel, final Color color) {
		color._red = _red[pixel];
		color._green = _green[pixel];
		color._blue = _blue[pixel];
	}
}
