package com.liaquay.tinyx.util;

/**
 * Allocate a number in the range 0 <= n < count 
 */
public class IntegerAllocator {
	
	private final int[] _bits;
	private final int _max;
	
	public IntegerAllocator(final int count) {
		_max = count;
		_bits = new int[(count + 31) >> 5];
	}
	
	/**
	 * Allocate a number in the range 0 <= n < count 
	 */
	public int allocate() {
		for(int i = 0 ; i < _bits.length; ++i ) {
			if(_bits[i] !=-1) {
				for(int k = 0; k < 32; ++k) {
					final int b = 1 << k;
					if((_bits[i] & b) == 0) {
						_bits[i] |= b;
						int v = (i << 5) + k;
						return v < _max ? v : -1; 
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * Free a value so it can be allocated again
	 * @param value the value to free
	 */
	public void free(final int value) {
		_bits[value >> 5] &= ~(1<<value&31);
	}
}
