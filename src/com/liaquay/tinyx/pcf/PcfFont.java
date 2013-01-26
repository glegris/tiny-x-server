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

public class PcfFont {
	final PcfAccelerators _accelerators;
	final PcfMetrics[] _metrics;
	final PcfBitmaps _bitmaps;
	final PcfEncodings _encodings;

	public PcfFont(
			final PcfAccelerators accelerators, 
			final PcfMetrics[] metrics,
			final PcfBitmaps bitmaps, 
			final PcfEncodings encodings) {

		_accelerators = accelerators;
		_metrics = metrics;
		_bitmaps = bitmaps;
		_encodings = encodings;
	}

	/**
	 * Test if the font is a 2-byte font.
	 * @return      True if the font is 2-byte (e.g. JISX0208).
	 */
	private boolean is2Byte() {
		return _encodings.getFirstRow() > 0;
	}

	/**
	 * Convert a string to Unicode.
	 * That is, "\ u x x x x" substrings are made into Unicode characters.
	 * @param	s	The string
	 * @return      The converted string.
	 */
	private static String toUnicode(String s) {
		final StringBuilder outstr = new StringBuilder(s.length());
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\' && s.length()-i >= 6 &&
					s.charAt(i+1) == 'u') {
				c = (char)Integer.parseInt(
						s.substring(i+2,i+6),16);
				i += 5;
			}
			outstr.append(c);
		}
		return outstr.toString();
	}

	private static String toJISX(final String s) {
		final StringBuilder sb = new StringBuilder(s.length());
		int mode = 0;
		for (int i=0; i<s.length(); i++) {
			int c = (int)s.charAt(i);
			if (c == 0x1b && i<s.length()-2) {
				if (s.charAt(i+1) == '(' &&
						(s.charAt(i+2)=='B' ||
						s.charAt(i+2)=='J')) {
					mode = 0;
					i += 2;
					continue;
				} else if (s.charAt(i+1) == '$' &&
						(s.charAt(i+2)=='@' ||
						s.charAt(i+2)=='B')) {
					mode = 1;
					i += 2;
					continue;
				}
			}
			if (mode == 1 && i<s.length()-1) {
				int c2 = (int)(s.charAt(i+1));
				c = (char)((c<<8)|c2);
				i++;
			} else if (c > 0x80 && i<s.length()-1) {
				c -= 0x80;
				int c2 = (int)(s.charAt(i+1))-0x80;
				c = (char)((c<<8)|c2);
				i++;
			}
			int cj = c-JISX_OFFSET;
			if (cj >= 0 && cj < JISX.length && JISX[cj]>0) {
				c = JISX[cj];
			}
			sb.append((char)c);
		}
		return sb.toString();
	}

	private final static int JISX_OFFSET = 32; // Offset of first character

	// Mapping from ASCII to JISX-0208
	final static char JISX[] = {
		'\u2121', '\u212a', '\u216d', '\u2174', '\u2170', '\u2173', '\u2175',
		'\u216c', '\u214a', '\u214b', '\u2176', '\u215c', '\u2124', '\u215d',
		'\u2125', '\u213f', '\u2330', '\u2331', '\u2332', '\u2333', '\u2334',
		'\u2335', '\u2336', '\u2337', '\u2338', '\u2339', '\u2127', '\u2128',
		'\u2163', '\u2161', '\u2164', '\u2129', '\u2177', '\u2341', '\u2342',
		'\u2343', '\u2344', '\u2345', '\u2346', '\u2347', '\u2348', '\u2349',
		'\u234a', '\u234b', '\u234c', '\u234d', '\u234e', '\u234f', '\u2350',
		'\u2351', '\u2352', '\u2353', '\u2354', '\u2355', '\u2356', '\u2357',
		'\u2358', '\u2359', '\u235a', '\u214e', '\u2140', '\u214f', '\u2130',
		'\u2132', '\u2129', '\u2361', '\u2362', '\u2363', '\u2364', '\u2365',
		'\u2366', '\u2367', '\u2368', '\u2369', '\u236a', '\u236b', '\u236c',
		'\u236d', '\u236e', '\u236f', '\u2370', '\u2371', '\u2372', '\u2373',
		'\u2374', '\u2375', '\u2376', '\u2377', '\u2378', '\u2379', '\u237a',
		'\u2150', '\u2143', '\u2151', '\u2141', '\u2121'};

	private String encodeString(final String text) {
		if (is2Byte()) {
			return toJISX(text);
		}
		else {
			return text;
		}
	}
	
	public void drawString(
			final PcfBitmaps.Renderer renderer, 
			final String text, 
			final int xs, 
			final int ys) {
		drawString(renderer, text, xs, ys, isDrawLeftToRight());
	}

	public void drawString(
			final PcfBitmaps.Renderer renderer, 
			final String text, 
			final int xs, 
			final int ys, 
			final boolean leftToRight) {
		
		final String encodedText = encodeString(text);
		int x = xs;
		for(int i = 0; i < encodedText.length(); ++i) {
			final char c = encodedText.charAt(i);
			final int glyphIndex = _encodings.getGlyphIndex(c);
			final PcfMetrics metrics = _metrics[glyphIndex];
			if(!leftToRight) x-= metrics.getWidth();
			_bitmaps.getBitmap(glyphIndex, metrics, x, ys, renderer);
			if(leftToRight) x += metrics.getWidth();
		}	
	}
	
	public PcfMetrics stringMetrics(final String text) {
		return stringMetricsForEncodedText(encodeString(text));
	}
	
	private PcfMetrics stringMetricsForEncodedText(final String encodedText) {
		final String s = encodedText;
		
		int ascent = 0;
		int descent = 0;
		int leftSideBearing = 0;
		int rightSideBearing = 0;
		int characterWidth = 0;

		for (int i=0; i<s.length(); i++) {

			final int glyphIndex = _encodings.getGlyphIndex(s.charAt(i));
			final PcfMetrics metrics = _metrics[glyphIndex];
			
			if (metrics.getAscent() > ascent) {
				ascent = metrics.getAscent();
			}
			
			if (metrics.getDescent() > descent) {
				descent = metrics.getDescent();
			}
			if (characterWidth + metrics.getLeftSideBearing() < leftSideBearing) {
				leftSideBearing = characterWidth + metrics.getLeftSideBearing();
			}
			
			if (characterWidth + metrics.getRightSideBearing() > rightSideBearing) {
				rightSideBearing = characterWidth + metrics.getRightSideBearing();
			}
			
			characterWidth += metrics.getWidth();
		}
		
		return new PcfMetrics(leftSideBearing, rightSideBearing, characterWidth, ascent, descent, 0);
	}
	
	public boolean getNoOverlap() {
		return _accelerators.getNoOverlap();
	}

	public boolean getConstantMetrics() {
		return _accelerators.getConstantMetrics();
	}

	public boolean getTerminalFont() {
		return _accelerators.getTerminalFont();
	}

	public boolean getConstantWidth() {
		return _accelerators.getConstantWidth();
	}

	public boolean getInkInside() {
		return _accelerators.getInkInside();
	}

	public boolean getInkMetrics() {
		return _accelerators.getInkMetrics();
	}

	public boolean isDrawLeftToRight() {
		return !_accelerators.getDrawDirection();
	}

	public boolean getNaturalAlignment() {
		return _accelerators.getNaturalAlignment();
	}

	public int getAscent() {
		return _accelerators.getAscent();
	}

	public int getDescent() {
		return _accelerators.getDescent();
	}

	public int getMaxOverlap() {
		return _accelerators.getMaxOverlap();
	}

	public PcfMetrics getMinBounds() {
		return _accelerators.getMinBounds();
	}

	public PcfMetrics getMaxBounds() {
		return _accelerators.getMaxBounds();
	}

	public PcfMetrics getMinInkBounds() {
		return _accelerators.getMinInkBounds();
	}

	public PcfMetrics getMaxInkBounds() {
		return _accelerators.getMaxInkBounds();
	}

	public int getDefaultCharacter() {
		return _encodings.getDefaultCharacter();
	}	
	
	public int getMinCharacter() {
		return _encodings.getMinCharacter();
	}
	
	public int getMaxCharacter() {
		return _encodings.getMaxCharacter();
	}
}
