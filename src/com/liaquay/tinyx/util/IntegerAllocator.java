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

/**
 * Allocate a number in the range 0 <= n < count 
 */
public class IntegerAllocator {
	
	private final int[] _bits;
	private final int _max;
	
	public IntegerAllocator(final int count) {
		_max = count;
		_bits = new int[(count + 31) >> 5];
	}
	
	/**
	 * Allocate a number in the range 0 <= n < count 
	 */
	public int allocate() {
		for(int i = 0 ; i < _bits.length; ++i ) {
			if(_bits[i] !=-1) {
				for(int k = 0; k < 32; ++k) {
					final int b = 1 << k;
					if((_bits[i] & b) == 0) {
						_bits[i] |= b;
						int v = (i << 5) + k;
						return v < _max ? v : -1; 
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * Free a value so it can be allocated again
	 * @param value the value to free
	 */
	public void free(final int value) {
		_bits[value >> 5] &= ~(1<<value&31);
	}
	
	public void free() {
		for(int i = 0; i < _bits.length; ++i) {
			_bits[i] = 0;
		}
	}
	
	public int nextAllocated(final int start) {
		int i = start + 1;
		while(i < _max) {
		  final int wi = i >> 5;
		  final int w = _bits[wi];
		  if(w == 0) {
		    i = (wi + 1) << 5;
		  }
		  else {
		    final int bi = i & 31;
		    final int bm = 1 << bi;
		    if((bm & w) != 0) {
		      return i;
		    }
		    ++i;
		  }
		}
		return -2;
	}
}
