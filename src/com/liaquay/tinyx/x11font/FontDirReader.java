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

public class FontDirReader {

	public interface Listener {
		public void alias(final String alias, final String fontPattern);
		public void font(final String fileName,  final String fontName);
	}
	
	public static void read(final String dirName, final Listener listener) throws IOException {
		
		final String fontsDir = dirName +
				(dirName.endsWith("/") ? "" : "/") +
				"fonts.dir";
		
		DiadicFileReader.read(fontsDir, new DiadicFileReader.Listener()  {
			@Override
			public void parameter(final String fileName, final String fontName) {
				final String fontFilename = dirName +
						(dirName.endsWith("/") ? "" : "/") +
						fileName;
				listener.font(fontFilename, fontName);
			}
		});
		
		final String fontsAlias = dirName +
				(dirName.endsWith("/") ? "" : "/") +
				"fonts.alias";
		
		DiadicFileReader.read(fontsAlias, new DiadicFileReader.Listener()  {
			@Override
			public void parameter(final String alias, final String fontPattern) {
				listener.alias(alias, fontPattern);
			}
		});
	}
	
	public static void main(final String[] args) throws IOException {
		read("/usr/share/fonts/X11/misc", new Listener(){
			@Override
			public void alias(final String alias, final String fontPattern) {
				System.out.println("ALIAS: " + alias + " " + fontPattern);
				
			}

			@Override
			public void font(final String fileName, final String fontName) {
				System.out.println("FONT: " + fileName + " " + fontName);
			}
		});
	}
}
