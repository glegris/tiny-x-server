package com.liaquay.tinyx.model;

public class ModifierMapping {

	private final int _keycodesPerModifier;
	private final byte[] _mappings;
	
	public ModifierMapping(final int keycodesPerModifier, final byte[] mappings) {
		_keycodesPerModifier = keycodesPerModifier;
		_mappings = mappings;
	}
	
	public int getKeycodesPerModifier() {
		return _keycodesPerModifier;
	}
	
	public byte[] getMappings() {
		return _mappings;
	}
	
	public int getKeycode(final int modifierIndex, final int keycodeIndex) {
		return _mappings[(modifierIndex * _keycodesPerModifier) + keycodeIndex] & 0xff;
	}
}
