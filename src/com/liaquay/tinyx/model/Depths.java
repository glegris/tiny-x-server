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

import java.util.Collection;

import com.liaquay.tinyx.util.IntMap;

public class Depths {
	private IntMap<Depth> _depthToDepthMap = new IntMap<Depth>();
	
	public void add(final Depth depth) {
		_depthToDepthMap.put(depth.getDepth(), depth);
	}
	
	public Collection<Depth> values() {
		return _depthToDepthMap.values();
	}

	public Object get(final int depth) {
		return _depthToDepthMap.get(depth);
	}
}
