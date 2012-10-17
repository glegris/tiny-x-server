/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
