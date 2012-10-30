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
package com.liaquay.tinyx.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** 
*  Very much not thread safe!
*/
public class IntMap<T> {
	
	private static final Comparator<Key> COMPARATOR = new Comparator<Key>() {
		@Override
		public int compare(Key o1, Key o2) {
			return o1._id - o2._id;
		}
	};
	
	public static class Key {
		private int _id;
		
		public Key(final int id) {
			_id = id;
		}
		
		public int getValue() {
			return _id;
		}
	}
	
	private Key _scratchKey = new Key(0);
	
	private Map<Key, T> _map = new TreeMap<Key, T>(COMPARATOR);
	
	public void put(final int id, final T value){
		_map.put(new Key(id), value);
	}
	
	public T get(final int id) {
		_scratchKey._id = id;
		return _map.get(_scratchKey);
	}
	
	public T remove(final int id) {
		_scratchKey._id = id;
		return _map.remove(_scratchKey);
	}
	
	public T remove(final Key key) {
		return _map.remove(key);
	}	 
	
	public Set<Map.Entry<Key, T>> entrySet() {
		return _map.entrySet();
	}
	
	public Collection<T> values() {
		return _map.values();
	}
	
	public int size() {
		return _map.size();
	}
}