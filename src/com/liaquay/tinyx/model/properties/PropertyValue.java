package com.liaquay.tinyx.model.properties;

import com.liaquay.tinyx.model.Atom;
import com.liaquay.tinyx.model.Property.Format;

public abstract class PropertyValue {
	
	private final Atom _typeAtom;
	
	public PropertyValue(final Atom typeAtom) {
		_typeAtom = typeAtom;
	}
	public int getLengthInBytes() {
		return getLength() * getFormat().getNoOfBytes();
	}
	
	public Atom getTypeAtom() {
		return _typeAtom;
	}
	
	public abstract int getLength();
	
	public abstract Format getFormat();
}
