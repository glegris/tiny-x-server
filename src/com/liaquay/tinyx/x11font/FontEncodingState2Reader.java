package com.liaquay.tinyx.x11font;

import java.io.IOException;

public class FontEncodingState2Reader {

	private static class FontEncodingListener implements FontEncodingStage1Reader.Listener {

		private int _size = 255;
		private int _first = 0;
		private char[] _map = null;

		@Override
		public void startEncoding() {
		}

		@Override
		public void encoding(final String symbol, final String name) {
		}

		@Override
		public void startMappingUnicode() {
			_map = new char[_size-_first+1];
			for(int i = 0; i < _map.length; ++i) _map[i] = (char)(i+_first);
		}

		@Override
		public void mapping(char from, char to) {
			final int index = (from&0xffff)-_first;
			if(index < 0 || index >= _map.length) return;
			_map[index] = to;
		}

		@Override
		public void undefine(char c) {
			final int index = (c&0xffff)-_first;
			if(index < 0 || index >= _map.length) return;
			_map[index] = 0xffff;
		}

		@Override
		public void size(int size) {
			_size = size;
		}

		@Override
		public void firstIndex(int index) {
			_first = index;
		}

		public FontEncoding getMapping() {
			return new ArrayFontMapping(0, _size, _first, _map);
		}
	}

	public static FontEncoding read(final String file) throws IOException {
		final FontEncodingListener listener = new FontEncodingListener();
		FontEncodingStage1Reader.read(file,  listener);
		return listener.getMapping();
	}
	
	public static FontEncoding readFromResource(final String file) throws IOException {
		final FontEncodingListener listener = new FontEncodingListener();
		FontEncodingStage1Reader.readFromResource(file,  listener);
		return listener.getMapping();
	}
	
	public static void main(final String[] args) throws IOException {
		final FontEncoding mapping = readFromResource("com/liaquay/tinyx/x11font/iso8859-10.enc");
		System.out.println(mapping);
	}
}
