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

public class Keyboard {

	/**
	 * Mapping of physical keys to key-codes
	 */
	private KeyboardMapping _keyboardMapping;
	
	/**
	 * Modifier mappings
	 */
	// TODO should be configurable
	private ModifierMapping _modifierMapping = new ModifierMapping(
			2,
			new byte[] {
				    (byte)16, (byte)151,     // shift
				    (byte)20, (byte)0x00,    // lock
				    (byte)17, (byte)11,      // control
				    (byte)18, (byte)0,       // mod1
				    (byte)148, (byte)0x00,   // mod2,
				    (byte)0x00, (byte)0x00,  // mod3,
				    (byte)0x00, (byte)0x00,  // mod4
				    (byte)145, (byte)0x00    // mod5
				  });
	
	/**
	 * This is a bit-array that keeps track of depressed keys
	 */
	private byte[] _keymap = new byte[32];

	/**
	 * This is a bit-array that keeps track of auto-repeat keys
	 */
	private byte[] _repeats = new byte[32];
	
	/**
	 * The keyboards bell
	 */
	private Bell _bell = new Bell();
	
	/**
	 * The keyboards click
	 */
	private KeyClick _click = new KeyClick();
	
	/**
	 * The LEDs
	 */
	private Leds _leds = new Leds();
	
	public Keyboard(final KeyboardMapping keyboardMapping) {
		_keyboardMapping = keyboardMapping;
	}
	
	public KeyboardMapping getKeyboardMapping() {
		return _keyboardMapping;
	}
	
	public byte[] getKeymap() {
		return _keymap;
	}
	
	public byte[] getRepeats() {
		return _repeats;
	}
	
	public void setAutoRepeatOn(final int keycode) {
		if (keycode < 0 || keycode > 255) return;
		_repeats[keycode >> 3] |= (1 << (keycode & 7));
	}
	
	public void setAutoRepeatOff(final int keycode) {
		if (keycode < 0 || keycode > 255) return;
		_repeats[keycode >> 3] &= ~(1 << (keycode & 7));		
	}
	
	private boolean _globalAutoRepeatEnabled = true;
	
	public boolean isGlobalAutoRepeatEnabled() {
		return _globalAutoRepeatEnabled;
	}
	
	public void setGlobalAutoRepeatEnabled(final boolean enabled) {
		_globalAutoRepeatEnabled = enabled;
	}
	
	public Bell getBell() {
		return _bell;
	}
	
	public KeyClick getKeyClick() {
		return _click;
	}
	
	public Leds getLeds() {
		return _leds;
	}

	public void setGlobalAutoRepeatDefault() {
		// TODO Not correct
		_globalAutoRepeatEnabled =  true;	
	}

	public void setAutoRepeatDefault(final int keycode) {
		// TODO Not correct
		setAutoRepeatOn(keycode);
	}
	
	public ModifierMapping getModifierMapping() {
		return _modifierMapping;
	}
}
