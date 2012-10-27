package com.liaquay.tinyx.model.properties;

import com.liaquay.tinyx.model.Property.Format;

public abstract class PropertyValue {
	
	private final int _typeAtomId;
	
	public PropertyValue(final int typeAtomId) {
		_typeAtomId = typeAtomId;
	}
	public int getLengthInBytes() {
		return getLength() * getFormat().getNoOfBytes();
	}
	
	public int getTypeAtomId() {
		return _typeAtomId;
	}
	
	public abstract int getLength();
	
	public abstract Format getFormat();
}
