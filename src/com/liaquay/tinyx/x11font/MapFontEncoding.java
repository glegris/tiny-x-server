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
package com.liaquay.tinyx.x11font;

import java.util.Map;

import com.liaquay.tinyx.util.IntMap;

public class MapFontEncoding extends FontEncodingAdaptor {

	private final int _first;
	private final int _last;
	
	public MapFontEncoding(
			final int first,
			final int last
			) {
		_first = first;
		_last = last;
	}

	@Override
	public char encode(final char c) {
		final Character t = _map.get(c);
		if(t == null) return c;
		return t;
	}

	@Override
	public char decode(final char c) {
		for(final Map.Entry<IntMap.Key, Character> entry : _map.entrySet()) {
			if(entry.getValue().charValue() == c) return (char)entry.getKey().getValue();
		}
		return c;
	}

	private IntMap<Character> _map = new IntMap<Character>();
	
	public void addMapping(final char from, final char to) {
		_map.put(from, to);
	}
	
	@Override
	public char getFirstCharacter() {
		return (char)_first;
	}

	@Override
	public char getLastCharacter() {
		return (char)_last;
	}
}

