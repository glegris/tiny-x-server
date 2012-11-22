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
 * Represents the state of 32 LEDs 
 */
public class Leds {
	
	private int _mask = 0;
	
	public int getMask() {
		return _mask;
	}
	
	/**
	 * @param index LED index 1 to 32
	 */
	public void turnOn(final int index) {
		_mask |= 1 << (index-1);
	}
	
	/**
	 * @param index LED index 1 to 32
	 */
	public void turnOff(final int index) {
		_mask &= ~(1 << (index-1));		
	}
	
	public void turnOn() {
		_mask = ~0;
	}
	
	public void turnOff() {
		_mask = 0;
	}
}
