/*
 *  PCF font reader
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
package com.liaquay.tinyx.pcf;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class PcfDemoForCursor {
	
	public static void main (String[] ar) throws IOException {
		final FileInputStream fileInputStream = new FileInputStream("/usr/share/fonts/X11/misc/cursor.pcf.gz");
		final GZIPInputStream zipInputStream = new GZIPInputStream(fileInputStream);
		try {
			final PcfFont font = PcfFontFactory.read(zipInputStream);
			
			System.out.println("read font");
			
			test2(font);
		}
		finally {
			fileInputStream.close();
		}
	}
	
	public static void test2(final PcfFont font) {
		final String text = "Hello World !\"£$%^&*()-=+_[]# #]['; ;',./ /.,\\|]";
		final PcfMetrics stringMetrics = font.stringMetrics(text);
		final PcfMetrics maxBounds =  font.getMaxBounds();
		final PcfStringBitmapRenderer renderer = new PcfStringBitmapRenderer(stringMetrics.getWidth(), maxBounds.getHeight());
		font.drawString(renderer, text, 0, maxBounds.getAscent(), true);
		System.out.println(text + "\n" + renderer);
	}	

}
