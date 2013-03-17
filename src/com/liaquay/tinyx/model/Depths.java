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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.liaquay.tinyx.util.IntMap;

public class Depths {
	
	public static class Depth {
		private final int _depth;
		private final Collection<Visual> _visuals = new ArrayList<Visual>(1);
		
		public Depth(final int depth) {
			_depth = depth;
		}
		
		public int getDepth() {
			return _depth;
		}
		
		public Collection<Visual> getVisuals() {
			return _visuals;
		}
		
		private void add(final Visual visual) {
			_visuals.add(visual);
		}
	}
	
	private Map<Integer, Depth> _depthToDepthMap = new TreeMap<Integer, Depth>();
	
	public void add(final Depth depth) {
		_depthToDepthMap.put(depth.getDepth(), depth);
	}
	
	public Collection<Depth> values() {
		return _depthToDepthMap.values();
	}

	public Depth get(final int depth) {
		return _depthToDepthMap.get(depth);
	}
	
	public void add(final Visual visual) {
		Depth depth = _depthToDepthMap.get(visual.getDepth());
		if(depth == null) {
			depth = new Depth(visual.getDepth());
			_depthToDepthMap.put(visual.getDepth(), depth);
		}
		depth.add(visual);
	}
}
