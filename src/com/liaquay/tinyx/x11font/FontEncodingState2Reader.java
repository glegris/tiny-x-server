package com.liaquay.tinyx.x11font;

import java.io.IOException;

public class FontEncodingState2Reader {

	private static class MapFontEncodingListener implements FontEncodingStage1Reader.Listener {

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
			_map = new char[_size-_first];
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
		final MapFontEncodingListener listener = new MapFontEncodingListener();
		FontEncodingStage1Reader.read(file,  listener);
		return listener.getMapping();
	}

	public static void main(final String[] args) throws IOException {
		final FontEncoding mapping = read("/usr/share/fonts/X11/encodings/adobe-symbol.enc.gz");
		System.out.println(mapping);
	}
}
