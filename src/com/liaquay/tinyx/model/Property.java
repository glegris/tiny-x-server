package com.liaquay.tinyx.model;

public abstract class Property {
	
	public enum Format {
		NoFormat,
		ByteFormat,
		ShortFormat,
		IntFormat
	}
	
	public enum Mode {
		PropModeReplace,
		PropModePrepend,
		PropModeAppend
	}
	
	private final int _propertyAtomId;
	private final int _typeAtomId;
	
	public Property(final int propertyAtomId, final int typeAtomId) {
		_propertyAtomId = propertyAtomId;
		_typeAtomId = typeAtomId;
	}
	
	public int getTypeAtomId() {
		return _typeAtomId;
	}
	
	public int getPropertyAtomId() {
		return _propertyAtomId;
	}
	
	public abstract int getLengthInBytes();
	public abstract int getLength();
	public abstract Format getFormat();
}
