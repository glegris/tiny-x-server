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
	private final PcfAccelerators _accelerators;
	private final PcfMetrics[] _metrics;
	private final PcfBitmaps _bitmaps;
	private final PcfEncodings _encodings;

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
		
		int x = xs;
		for(int i = 0; i < text.length(); ++i) {
			final char c = text.charAt(i);
			final int glyphIndex = _encodings.getGlyphIndex(c);
			final PcfMetrics metrics = _metrics[glyphIndex];
			if(!leftToRight) x-= metrics.getWidth();
			_bitmaps.getBitmap(glyphIndex, metrics, x, ys, renderer);
			if(leftToRight) x += metrics.getWidth();
		}	
	}
	
	public PcfMetrics stringMetrics(final String s) {
		
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
	
	public String toString(final String text) {
		final PcfMetrics stringMetrics = stringMetrics(text);
		final PcfMetrics maxBounds =  getMaxBounds();
		final PcfStringBitmapRenderer renderer = new PcfStringBitmapRenderer(stringMetrics.getWidth(), maxBounds.getHeight());
		drawString(renderer, text, 0, maxBounds.getAscent(), true);
		return renderer.toString();
	}
	
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for(int i = _encodings.getMinCharacter(); i< _encodings.getMaxCharacter(); ++i) {
			sb.append(i + "\n");
			sb.append(toString("" + (char)i));
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
