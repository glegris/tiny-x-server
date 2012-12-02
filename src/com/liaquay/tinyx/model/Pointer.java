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

public class Pointer {
	
	public interface Listener {
		public void mappingNotify(final int count);
	}

	private Listener _listener = new Listener() {
		@Override
		public void mappingNotify(int count) {
		}		
	};
	
	void setListener(final Listener listener) {
		_listener = listener;
	}
	
	private int _x = 0;
	private int _y = 0;
	private int _buttonState = 0;
	private int _accelerationNumerator = 1;
	private int _accelerationDemoninator = 1;
	private int _threshold = 1;
	private boolean _doAcceleration = false;
	private boolean _doThreshold = false;
	private PointerMapping _mapping = new PointerMapping(5);
	private Screen _screen = null;

	public Pointer() {
	}

	
	public Screen getScreen() {
		return _screen;
	}
	
	public PointerMapping getPointerMapping() {
		return _mapping;
	}
	
	public void setPointerMapping(final PointerMapping mapping) {
		_mapping = mapping;
		_listener.mappingNotify(_mapping.getNumberOfButtons());
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public void set(final Screen screen, final int x, final int y) {
		_screen = screen;
		_x = x;
		_y = y;
	}

	public void buttonPressed(final int buttonIndex) {
		_buttonState |= 1 << buttonIndex;
	}
	
	public void buttonReleased(final int buttonIndex) {
		_buttonState &= ~(1 << buttonIndex);
	}
	
	public boolean isButtonPressed(final int buttonIndex) {
		return (_buttonState & 1 << buttonIndex) != 0;
	}

	public void setAccelerationNumerator(final int accelerationNumerator) {
		_accelerationNumerator = accelerationNumerator;
	}

	public void setAccelerationDemoninato(final int accelerationDemoninator) {
		_accelerationDemoninator = accelerationDemoninator;
	}

	public void setThreshold(final int threshold) {
		_threshold = threshold;
	}

	public void setDoThreshold(final boolean doThreshold) {
		_doThreshold = doThreshold;
	}

	public void setDoAcceleration(final boolean doAcceleration) {
		_doAcceleration = doAcceleration;
	}

	public int getAccelerationNumerator() {
		return _accelerationNumerator;
	}

	public int getAccelerationDenominator() {
		return _accelerationDemoninator;
	}

	public int getThreshold() {
		return _threshold;
	}
	
	public boolean getDoThreshold() {
		return _doThreshold;
	}
	
	public boolean getDoAcceleration() {
		return _doAcceleration;
	}
	
	public Window childWindowAt() {
		if(_screen == null) return null;
		return _screen.childWindowAt(_x, _y);
	}
}
