/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.x11font;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileFontEncodingFactory implements FontEncodingFactory {
	
	private final static Logger LOGGER = Logger.getLogger(FileFontEncodingFactory.class.getName());

	private Map<String, String> _fileNameMap = new TreeMap<String, String>();
	private Map<String, FontEncoding> _fontEncodingMap = new TreeMap<String, FontEncoding>();
	
	public FileFontEncodingFactory() {}
	
	public void loadBuiltIns() throws IOException {
		loadBuiltIn("iso8859-1");
		loadBuiltIn("iso8859-2");
		loadBuiltIn("iso8859-3");
		loadBuiltIn("iso8859-4");
		loadBuiltIn("iso8859-5");
		loadBuiltIn("iso8859-6");
		loadBuiltIn("iso8859-7");
		loadBuiltIn("iso8859-8");
		loadBuiltIn("iso8859-9");
		loadBuiltIn("iso8859-10");
		loadBuiltIn("iso8859-15");
		loadBuiltIn("iso10646-1");
	}
	
	private void loadBuiltIn(final String encoding) throws IOException {
		final FontEncoding mapping = FontEncodingState2Reader.readFromResource("com/liaquay/tinyx/x11font/"+ encoding + ".enc");
		_fontEncodingMap.put(encoding, mapping);
	}
	
	public void load(final String folderName) throws IOException {
		FontEncodingsReader.read(folderName, new FontEncodingsReader.Listener() {
			@Override
			public void encoding(final String encoding, final String filename) {
				_fileNameMap.put(encoding, filename);
			}
		});
	}

	@Override
	public FontEncoding open(final String encoding) throws IOException {
		
		final FontEncoding openEncoding = _fontEncodingMap.get(encoding);
		if(openEncoding != null) return openEncoding;
		
		final String fileName = _fileNameMap.get(encoding);
		if(fileName == null) {
			LOGGER.log(Level.SEVERE, "Failed to load font encoding " + encoding + " as no file is associated with this name");
			
			return NullFontEncoding.INSTANCE;
		}
		
		final FontEncoding readEncoding = FontEncodingState2Reader.read(fileName);
		_fontEncodingMap.put(encoding, readEncoding);
		return readEncoding;
	}

	@Override
	public void close(final FontEncoding encoding) {
	}
	
	public static void main(final String[] args) throws IOException {
		final FileFontEncodingFactory f = new FileFontEncodingFactory();
		f.loadBuiltIns();
		f.load("/usr/share/fonts/X11/misc");
		final FontEncoding encoding1 = f.open("iso8859-11");
		System.out.println(encoding1);
		final FontEncoding encoding2 = f.open("iso8859-11");
		System.out.println(encoding2);
	}
}
