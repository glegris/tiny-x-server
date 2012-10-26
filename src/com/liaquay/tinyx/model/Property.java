package com.liaquay.tinyx.model;

import com.liaquay.tinyx.model.ColorMap.AllocType;


public abstract class Property {
	
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
			if (index<modes.length && index>=0)
				return modes[index];
			return null;
		}		
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
