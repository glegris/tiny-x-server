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
	
	/**
	 * Used to form the pointer button state
	 */
	public static final int Button1Mask=(1<<8);
	public static final int Button2Mask=(1<<9);
	public static final int Button3Mask=(1<<10);
	public static final int Button4Mask=(1<<11);
	public static final int Button5Mask=(1<<12);
	
	private int _x, _y;
	private int _state;
	private int _accelerationNumerator = 1;
	private int _accelerationDemoninator = 1;
	private int _threshold = 1;
	private boolean _doAcceleration = false;
	private boolean _doThreshold = false;
	private PointerMapping _mapping = new PointerMapping(5);

	public Pointer() {
		_x = 0;
		_y = 0;
		_state = 0;
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
	
	public void set(final int x, final int y) {
		_x = x;
		_y = y;
	}

	public int getState() {
		return _state;
	}
	
	public void setState(final int state) {
		_state = state;
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
}
