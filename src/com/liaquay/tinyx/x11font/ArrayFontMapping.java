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

public class ArrayFontMapping extends FontEncodingAdaptor {

	private final int _first;
	private final int _last;
	private final int _start;
	private final char[] _map;
	
	public ArrayFontMapping(
			final int first,
			final int last,
			final int mapStart, 
			final char[] map) {
		
		_first = first;
		_last = last;
		_start = mapStart;
		_map = map;
	}
	 
	@Override
	public char encode(final char c) {
		final int index = (c & 0xffff) - _start;
		if(index < 0 || index >= _map.length) return c;
		return _map[index];
	}

	@Override
	public char decode(final char c) {
		for(int i = 0; i < _map.length; ++i) {
			if(_map[i] == c) return (char)(_start+i);
		}
		return c;
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
