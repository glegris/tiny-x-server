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

public class Visual extends AbstractResource {

	public enum BackingStoreSupport {
		BackingStoreNever,
		BackingStoreWhenMapped,
		BackingStoreAlways,
	}

	public enum VisualClass {
		StaticGray,
		GrayScale,
		StaticColor,
		PseudoColor,
		TrueColor,
		DirectColor,
	}

	private final BackingStoreSupport _backingStoreSupport;
	private final VisualClass _visualClass;
	private final int _bitsPerRGB;
	private final int _colormapEntries;
	private final int _redMask;
	private final int _greenMask;
	private final int _blueMask;
	private final int _depth;
	/**
	 * Constructor.
	 */
	public Visual (
			final int resourceId,
			final int depth,
			final BackingStoreSupport backingStoreSupport,
			final VisualClass visualClass,
			final int bitsPerRGB,
			final int colormapEntries) {

		super(resourceId);

		_backingStoreSupport = backingStoreSupport;
		_depth = depth;
		_visualClass = visualClass;
		_bitsPerRGB = bitsPerRGB;
		_colormapEntries = colormapEntries;
		_redMask = 0;
		_greenMask = 0;
		_blueMask = 0;
	}
	/**
	 * Constructor.
	 *
	 * @param id    The visual ID.
	 */
	public Visual (
			final int resourceId,
			final int depth,
			final BackingStoreSupport backingStoreSupport,
			final VisualClass visualClass,
			final int bitsPerRGB,
			final int colormapEntries,
			final int redMask,
			final int greenMask,
			final int blueMask) {

		super(resourceId);

		_backingStoreSupport = backingStoreSupport;
		_depth = depth;
		_visualClass = visualClass;
		_bitsPerRGB = bitsPerRGB;
		_colormapEntries = colormapEntries;
		_redMask = redMask;
		_greenMask = greenMask;
		_blueMask = blueMask;
	}

	public VisualClass getVisualClass() {
		return _visualClass;
	}

	public BackingStoreSupport getBackingStoreSupport () {
		return _backingStoreSupport;
	}

	/**
	 * Return whether the visual supports save-under.
	 *
	 * @return    Whether save-under is supported.
	 */
	public boolean getSaveUnder () {
		return false;
	}

	public int getBitsPerRGB() {
		return _bitsPerRGB;
	}

	public int getColormapEntries() {
		return _colormapEntries;
	}

	public int getRedMask() {
		return _redMask;
	}

	public int getGreenMask() {
		return _greenMask;
	}

	public int getBlueMask() {
		return _blueMask;
	}
	
	public int getDepth() {
		return _depth;
	}
}
