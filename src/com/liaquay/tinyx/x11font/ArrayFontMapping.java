package com.liaquay.tinyx.x11font;

public class ArrayFontMapping extends FontEncodingAdaptor {

	private final int _first;
	private final int _last;
	private final int _start;
	private final char[] _map;
	
	public ArrayFontMapping(
			final int first,
			final int last,
			final int mapStart, 
			final char[] map) {
		
		_first = first;
		_last = last;
		_start = mapStart;
		_map = map;
	}
	 
	@Override
	public char encode(final char c) {
		final int index = (c & 0xffff) - _start;
		if(index < 0 || index >= _map.length) return c;
		return _map[index];
	}

	@Override
	public char decode(final char c) {
		for(int i = 0; i < _map.length; ++i) {
			if(_map[i] == c) return (char)(_start+i);
		}
		return c;
	}

	@Override
	public char getFirstCharacter() {
		return (char)_first;
	}

	@Override
	public char getLastCharacter() {
		return (char)_last;
	}
}
