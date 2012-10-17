package com.liaquay.tinyx.model;

// TODO Make an abstract Visual and extend it with a true colour visual
public class Visual extends AbstractResource {
	public final static byte	BackingStoreNever = 0;
	public final static byte	BackingStoreWhenMapped = 1;
	public final static byte	BackingStoreAlways = 2;

	public final static byte	StaticGray = 0;
	public final static byte	GrayScale = 1;
	public final static byte	StaticColor = 2;
	public final static byte	PseudoColor = 3;
	public final static byte	TrueColor = 4;
	public final static byte	DirectColor = 5;

	/**
	 * Constructor.
	 *
	 * @param id	The visual ID.
	 */
	public Visual (	int	id) {
		super(id);
	}

	public int getType() {
		return TrueColor;
	}

	/**
	 * Return whether the visual supports a backing store.
	 *
	 * @return	Whether a backing store is supported.
	 */
	public byte
	getBackingStoreInfo () {
		return BackingStoreAlways;
	}

	/**
	 * Return whether the visual supports save-under.
	 *
	 * @return	Whether save-under is supported.
	 */
	public boolean
	getSaveUnder () {
		return false;
	}

	/**
	 * Return the depth of the visual.
	 * Under Android this is always 32.
	 *
	 * @return	The depth of the visual, in bits.
	 */
	public byte
	getDepth () {
		return 32;
	}
}
