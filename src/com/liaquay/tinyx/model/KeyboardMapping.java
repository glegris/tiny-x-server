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

public class KeyboardMapping {
	
	public interface Listener {
		public void mappingNotify(final int firstKeyCode, final int count);
	}
	
	private final int _firstKeyCode;
	private final int _keysymsPerKeycode;
	private final int _keycodeCount;
	private final int[] _keyboardMapping;
	private Listener _listener = new Listener() {
		@Override
		public void mappingNotify(int firstKeyCode, int count) {
		}		
	};
	
	void setListener(final Listener listener) {
		_listener = listener;
	}
	
	public KeyboardMapping(
			final int firstKeyCode, 
			final int keycodeCount,
			final int keysymsPerKeycode,
			final int[] keyboardMapping) {
		
		_firstKeyCode = firstKeyCode;
		_keyboardMapping = keyboardMapping;
		_keysymsPerKeycode = keysymsPerKeycode;
		_keycodeCount = keycodeCount;
	}
	
	public int getFirstKeyCode() {
		return _firstKeyCode;
	}
	
	public int getKeysymsPerKeycode() {
		return _keysymsPerKeycode;
	}
	
	public int getKeycodeCount() {
		return _keycodeCount;
	}
	
	public int[] getMappingArray() {
		return _keyboardMapping;
	}
	
	public void changeMapping(
			final int keycodeCount,
			final int firstKeycode,
			final int keysymsPerKeycode,
			final int[] m) {
		
		for(int i = 0; i < keycodeCount; ++i) {
			final int k = firstKeycode + i - _firstKeyCode;
			if(k >= 0 && k < _keycodeCount) {
				for(int j = 0; j < _keysymsPerKeycode; ++j) {
					_keyboardMapping[(k*_keysymsPerKeycode) + j] = j < keysymsPerKeycode ? m[(i*keysymsPerKeycode) + j] : 0;
				}
			}
		}
		_listener.mappingNotify(firstKeycode, keycodeCount);
	}
}
