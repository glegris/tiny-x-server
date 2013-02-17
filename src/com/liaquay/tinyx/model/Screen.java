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

import com.liaquay.tinyx.model.eventfactories.EventFactories;

public class Screen extends Window {

	private final ColorMap _defaultColorMap;
	private final int _widthMM;
	private final int _heightMM;
	private final int _minInstalledMaps = 1; // TODO What are these?
	private final int _maxInstalledMaps = 1; // TODO What are these?
	private final Depths _depths;           

	public Screen(
			final int rootWindowResourceId, 
			final ColorMap defaultColorMap,
			final Visual rootVisual,
			final int rootDepth,
			final int widthPixels,
			final int heightPixels,
			final int widthMM,
			final int heightMM,
			final Depths depths,
			final EventFactories eventFactories) {

		super(rootWindowResourceId,
				null,
				rootVisual,
				rootDepth,
				widthPixels,
				heightPixels,
				0, //x 
				0, // y
				0, // border width
				WindowClass.InputOutput,
				eventFactories,
				defaultColorMap);

		_defaultColorMap = defaultColorMap;
		_widthMM = widthMM;
		_heightMM = heightMM;
		_depths = depths;
		
		setBackgroundPixel(defaultColorMap.getBlackPixel());
	}

	public ColorMap getDefaultColorMap() {
		return _defaultColorMap;
	}

	public int getWidthMM() {
		return _widthMM;
	}	

	public int getHeightMM() {
		return _heightMM;
	}

	public int getMinInstalledMaps() {
		return _minInstalledMaps;
	}

	public int getMaxInstalledMaps() {
		return _maxInstalledMaps;
	}

	public Depths getDepths() {
		return _depths;
	}

	public Screen getScreen() {
		return this;
	}
}
