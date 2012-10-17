package com.liaquay.tinyx.model;

public abstract class ColorMap extends AbstractResource {

	public ColorMap(final int id) {
		super(id);
	}
	
	/**
	 * Return the value of a black pixel.
	 *
	 * @return	The value of a black pixel.
	 */
	public abstract int getBlackPixel ();

	/**
	 * Return the value of a white pixel.
	 *
	 * @return	The value of a white pixel.
	 */
	public abstract int getWhitePixel ();
}
