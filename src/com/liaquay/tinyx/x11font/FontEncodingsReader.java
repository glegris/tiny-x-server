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

public class FontEncodingsReader {
	public interface Listener {
		public void encoding(final String encoding, final String filename);
	}
	
	public static void read(final String dirName, final Listener listener) throws IOException {
		
		final String encodingsDir = dirName +
				(dirName.endsWith("/") ? "" : "/") +
				"encodings.dir";
		
		DiadicFileReader.read(encodingsDir, new DiadicFileReader.Listener()  {
			@Override
			public void parameter(final String encoding, final String filename) {
				final String prefixedFilename = filename.startsWith("/") ? filename :						
						dirName + (dirName.endsWith("/") ? "" : "/") + filename;
				listener.encoding(encoding, prefixedFilename);
			}
		});
	}
	
	public static void main(final String[] args) throws IOException {
		read("/usr/share/fonts/X11/misc", new Listener(){
			@Override
			public void encoding(final String encoding, final String filename) {
				System.out.println("Encoding: " + encoding + " " + filename);
			}
		});
	}
}
