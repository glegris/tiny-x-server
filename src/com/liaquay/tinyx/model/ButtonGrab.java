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


public class ButtonGrab {
	
	private static final int AnyModifier = 0x8000;
	private static final int AnyButton = 0;
	
	public static final class Trigger {
		private final int _button;
		private final int _keyMask;

		public Trigger(final int button, final int keyMask) {
			_button = button;
			_keyMask = keyMask;			
		}
		
		public int getButton() {
			return _button;
		}
		
		public int getKeyMask() {
			return _keyMask;
		}

		@Override
		public boolean equals(final Object o) {
			return equals((Trigger)o);
		}

		public boolean equals(final Trigger trigger) {

			final boolean sameButton =
					_button == AnyButton || 
					trigger._button == AnyButton ||
					_button == trigger._button;

			final boolean sameKey =
					_keyMask == AnyModifier ||
					trigger._keyMask == AnyModifier ||
					_keyMask == trigger._keyMask;

			return sameButton && sameKey;
		}
		
		public int hashCode() {
			return _keyMask * _button;
		}
	}
	
	private final boolean _ownerEvents;
	private final Window _grabWindow;
	private final int _eventMask;
	private final boolean _pointerSynchronous;
	private final boolean _keyboardSynchronous;
	private final Window _confineToWindow;
	private final Cursor _cursor;
	private final Trigger _trigger;
	private final Client _client;
	
	public ButtonGrab(
			final Client client, 
			final boolean ownerEvents,
			final Window grabWindow,
			final int eventMask,
			final boolean pointerSynchronous,
			final boolean keyboardSynchronous,
			final Window confineToWindow,
			final Cursor cursor,
			final Trigger trigger) {
		
		_ownerEvents = ownerEvents;
		_grabWindow = grabWindow;
		_eventMask = eventMask;
		_pointerSynchronous = pointerSynchronous;
		_keyboardSynchronous = keyboardSynchronous;
		_confineToWindow = confineToWindow;
		_cursor = cursor;
		_client = client;
		_trigger = trigger;
	}
	
	public Client getClient() {
		return _client;
	}
	
	public Trigger getTrigger() {
		return _trigger;
	}	
	
	/**
	 * Check if this passive grab should be activated.
	 * Note: key and pointer state must be updated prior to calling this method.
	 * 
	 * @param keyboard
	 * @param pointer
	 * @return
	 */
	public boolean checkActivation(
			final Keyboard keyboard, 
			final Pointer pointer) {
		
		// Check that only the required button is pressed
		if(_trigger._button == AnyButton) {
			// Check for any button pressed
			if(pointer.getButtonMask() == 0) return false;
		}
		else {
			// Check for the specific button (and only the specific button).
			if(!pointer.isOnlyButtonPressed(_trigger._button - 1)) return false;
		}
		// Check the pointer is not grabbed
		if(pointer.getPointerGrab() != null) return false;
		// Check the required modifiers are in place
		if(_trigger._keyMask != AnyModifier) {
			if(_trigger._keyMask != keyboard.getModifierMask()) return false;
		}
		// Check the grab window contains the pointer
		if(!_grabWindow.containsPixel(pointer.getX(), pointer.getY())) return false;
		
		for(Window ancestor = _grabWindow.getParent(); ancestor != null; ancestor = ancestor.getParent()) {
			final ButtonGrab existingGrab = ancestor.getButtonGrab(_trigger);
			if(existingGrab != null) return false;
		}
		
		// Check the confine-to window is viewable
		if(_confineToWindow != null) {
			if(!_confineToWindow.isViewable()) return false;
		}
		
		return true;
	}

}
