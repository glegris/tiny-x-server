package com.liaquay.tinyx.model;

import com.liaquay.tinyx.model.properties.PropertyValue;



public class Property {
	
	public enum Format {
		NoFormat(0),
		ByteFormat(1),
		ShortFormat(2),
		IntFormat(4);
		
		private final int _noOfBytes;
		
		private Format(final int noOfBytes) {
			_noOfBytes = noOfBytes;
		}
		
		public int getNoOfBytes() {
			return _noOfBytes;
		}		
		
		public int getNoOfBits() {
			return _noOfBytes << 3;
		}
		
		public int intsToUnits(final int noOfInts) {
			return noOfInts << (3 -ordinal());
		}
		
		public static Format getFromNoOfBits(final int noOfBits) {
			switch(noOfBits) {
			case 8:return ByteFormat;
			case 16:return ShortFormat;
			case 32:return IntFormat;
			default:
				return null;	
			}			
		}
	}
	
	public enum Mode {
		PropModeReplace,
		PropModePrepend,
		PropModeAppend;
		
		public static Mode getFromIndex(final int index) {
			final Mode[] modes = values();
			if (index<modes.length && index>=0) return modes[index];
			return null;
		}		
	}
	
	private final Atom _propertyAtom;
	private PropertyValue _value = null;
	
	public Property(final Atom propertyAtom) {
		_propertyAtom = propertyAtom;
	}
	
	public Atom getPropertyAtom() {
		return _propertyAtom;
	}
	
	public PropertyValue getValue() {
		return _value;
	}

	public void setValue(final PropertyValue value) {
		// TODO need to issue and event
		_value = value;
	}
}
