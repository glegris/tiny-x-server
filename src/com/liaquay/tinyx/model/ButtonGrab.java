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

	public PointerGrab getPointerGrab(final int timestamp) {
		return new PointerGrab(
				_client, 
				_ownerEvents,
				_grabWindow,
				_eventMask,
				_pointerSynchronous,
				_keyboardSynchronous,
				_confineToWindow,
				_cursor,
				timestamp);
	}

	public Window getConfineToWindow() {
		return _confineToWindow;
	}
}
