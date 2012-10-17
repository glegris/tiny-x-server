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

public class Screen {
	private final Visual _rootVisual;
	private final int _rootDepth;
	private final ColorMap _defaultColorMap;
	private final RootWindow _rootWindow;
	private final int _widthPixels;
	private final int _heightPixels;
	private final int _widthMM;
	private final int _heightMM;
	private final int _minInstalledMaps = 1; // TODO What are these?
	private final int _maxInstalledMaps = 1; // TODO What are these?
	private final Depth[] _depths;           // TODO What are these?

	public Screen(final RootWindow rootWindow, 
			       final ColorMap defaultColorMap,
			       final Visual rootVisual,
			       final int rootDepth,
			       final int widthPixels,
			       final int heightPixels,
			       final int widthMM,
			       final int heightMM,
			       final Depth[] depths) {
		
		_rootWindow = rootWindow;
		_defaultColorMap = defaultColorMap;
		_rootVisual = rootVisual;
		_rootDepth = rootDepth;
		_widthPixels = widthPixels;
		_heightPixels = heightPixels;
		_widthMM = widthMM;
		_heightMM = heightMM;
		_depths = depths;
	}
	
	public Visual getRootVisual() {
		return _rootVisual;
	}
	
	public RootWindow getRootWindow() {
		return _rootWindow;
	}
	
	public ColorMap getDefaultColorMap() {
		return _defaultColorMap;
	}
	
	public int getWidthPixels() {
		return _widthPixels;
	}	
	
	public int getHeightPixels() {
		return _heightPixels;
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

	public Depth[] getDepths() {
		return _depths;
	}
	
	public int getRootDepth(){
		return _rootDepth;
	}
}
