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

/**
 * A set of boolean flags, 1 for each key-code
 */
public class KeyFlags {
	private final byte[] _bytes;
	
	public KeyFlags(final byte[] bytes) {
		_bytes = bytes;
	}
	
	public KeyFlags() {
		_bytes = new byte[32];
	}
	
	public void set(final int keycode) {
		if (keycode < 0 || keycode > 255) return;
		_bytes[keycode >> 3] |= (1 << (keycode & 7));
	}
	
	public void clear(final int keycode) {
		if (keycode < 0 || keycode > 255) return;
		_bytes[keycode >> 3] &= ~(1 << (keycode & 7));		
	}
	
	public boolean isSet(final int keycode) {
		if (keycode < 0 || keycode > 255) return false;
		return (_bytes[keycode >> 3] & (1 << (keycode & 7))) != 0;
	}
	
	public byte[] getBytes() {
		return _bytes;
	}
}
