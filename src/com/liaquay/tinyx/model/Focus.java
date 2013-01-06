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

public class Focus {

	public enum RevertTo {
		None,
		PointerRoot,
		Parent;
		
		public static RevertTo getFromIndex(final int index) {
			final RevertTo[] revertTo = values();
			if (index<revertTo.length && index>=0) return revertTo[index];
			return null;
		}
	}
	
	public enum Mode {
		None,
		PointerRoot,
		Window;
	}
	
	private Window _window;
	private RevertTo _revertTo;
	private Mode _mode = Mode.PointerRoot;
	private int _timestamp = 0;
	
	public Focus(final Window window, final RevertTo revertTo) {
		_window = window;
		_revertTo = revertTo;
	}
	
	public Window getWindow() {
		return _window;
	}
	
	public Mode getMode() {
		return _mode;
	}
	
	public RevertTo getRevertTo() {
		return _revertTo;
	}
	
	public int getTimestamp() {
		return _timestamp;
	}
	
	public void set(final Mode mode, final Window window, final RevertTo revertTo, final int timestamp) {
		if(timestamp > _timestamp) {
			
			// TODO Issue focus events
			
			_window = window;
			_revertTo = revertTo;
			_mode = mode;
		}
	}
}
