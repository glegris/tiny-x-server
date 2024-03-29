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
	
	public enum Modifier {
		Shift,
		Lock,
		Control,
		Mod1,
		Mod2,
		Mod3,
		Mod4,
		Mod5
	};
	
	public interface Listener {
		public void mappingNotify(final int firstKeyCode, final int count);
		public void modiferNotify();
	}

	private Listener _listener = new Listener() {
		@Override
		public void mappingNotify(int firstKeyCode, int count) {
		}		
		
		@Override
		public void modiferNotify() {
		}
	};
	
	void setListener(final Listener listener) {
		_listener = listener;
	}

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
	private KeyFlags _keymap = new KeyFlags();

	/**
	 * This is a bit-array that keeps track of auto-repeat keys
	 */
	private KeyFlags _repeats = new KeyFlags();
	
	/**
	 * The keyboards bell
	 */
	private Bell _bell = new Bell();
	
	/**
	 * The keyboards click
	 */
	private KeyClick _click = new KeyClick();
	
	/**
	 * A grab
	 */
	private KeyboardGrab _keyboardGrab = null;
	
	/**
	 * The LEDs
	 */
	private Leds _leds = new Leds();
	
	public Keyboard(final KeyboardMapping keyboardMapping) {
		_keyboardMapping = keyboardMapping;
		
		keyboardMapping.setListener(new KeyboardMapping.Listener() {
			@Override
			public void mappingNotify(final int firstKeyCode, final int count) {
				_listener.mappingNotify(firstKeyCode, count);
			}});
	}
	
	public KeyboardMapping getKeyboardMapping() {
		return _keyboardMapping;
	}
	
	public byte[] getKeymap() {
		return _keymap.getBytes();
	}
	
	public byte[] getRepeats() {
		return _repeats.getBytes();
	}
	
	public void setAutoRepeatOn(final int keycode) {
		_repeats.set(keycode);
	}
	
	public void setAutoRepeatOff(final int keycode) {
		_repeats.clear(keycode);
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

	public void setModifierMapping(final ModifierMapping modifierMapping) {
		_modifierMapping = modifierMapping;
		_listener.modiferNotify();
	}
	
	/**
	 * Get the state of the current modifiers.
	 * 
	 * @param Modifier the modifier
	 * @return true if the modifier is set
	 */
	public boolean isModified(final Modifier Modifier) {
		final int keycodesPerModifier = _modifierMapping.getKeycodesPerModifier();
		for(int keycodeIndex = 0; keycodeIndex < keycodesPerModifier; ++keycodeIndex) {
			final int keycode = _modifierMapping.getKeycode(Modifier.ordinal(), keycodeIndex);
			if(keycode == 0) return false;
			if(_keymap.isSet(keycode)) return true;
		}
		return false;
	}
	
	public int getModifierMask() {
		return 
				(isModified(Keyboard.Modifier.Shift) ? 1 : 0 ) |
				(isModified(Keyboard.Modifier.Lock) ? 1<<1 : 0 ) |
				(isModified(Keyboard.Modifier.Control) ? 1<<2 : 0 ) |
				(isModified(Keyboard.Modifier.Mod1) ? 1<<3 : 0 ) |
				(isModified(Keyboard.Modifier.Mod2) ? 1<<4 : 0 ) |
				(isModified(Keyboard.Modifier.Mod3) ? 1<<5 : 0 ) |
				(isModified(Keyboard.Modifier.Mod4) ? 1<<6 : 0 ) |
				(isModified(Keyboard.Modifier.Mod5) ? 1<<7 : 0 );
	}
	
	/**
	 * Called by server
	 * 
	 * @param keycode code representing physical key in the range 0-255
	 * @param when time in milliseconds
	 */
	void keyPressed(final int keycode, final long when) {
		_keymap.set(keycode);
		
		//TODO ...
		
	}
	
	/**
	 * Called by server
	 * 
	 * @param keycode code representing physical key in the range 0-255
	 * @param when time in milliseconds
	 */
	void keyReleased(final int keycode, final long when){
		_keymap.clear(keycode);
		
		//TODO ...
		
	}	
	
	public void setKeyboardGrab(final KeyboardGrab keyboardGrab) {
		_keyboardGrab = keyboardGrab;
	}
	
	public KeyboardGrab getKeyboardGrab() {
		return _keyboardGrab;
	}
}
