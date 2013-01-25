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

public class PcfAccelerators {
	private final boolean _noOverlap;
	private final boolean _constantMetrics;
	private final boolean _terminalFont;
	private final boolean _constantWidth;
	private final boolean _inkInside;
	private final boolean _inkMetrics;
	private final boolean _drawDirection;
	private final boolean _naturalAlignment;
	private final int _ascent;
	private final int _descent;
	private final int _maxOverlap;
	
	private final PcfMetrics _minBounds;
	private final PcfMetrics _maxBounds;
	private final PcfMetrics _minInkBounds;
	private final PcfMetrics _maxInkBounds;
	
	public PcfAccelerators(
			final boolean noOverlap, 
			final boolean constantMetrics,
			final boolean terminalFont, 
			final boolean constantWidth, 
			final boolean inkInside, 
			final boolean inkMetrics,
			final boolean drawDirection, 
			final boolean naturalAlignment, 
			final int ascent, 
			final int descent,
			final int maxOverlap, 
			final PcfMetrics minBounds, 
			final PcfMetrics maxBounds,
			final PcfMetrics minInkBounds, 
			final PcfMetrics maxInkBounds) {
		
		_noOverlap = noOverlap;
		_constantMetrics = constantMetrics;
		_terminalFont = terminalFont;
		_constantWidth = constantWidth;
		_inkInside = inkInside;
		_inkMetrics = inkMetrics;
		_drawDirection = drawDirection;
		_naturalAlignment = naturalAlignment;
		_ascent = ascent;
		_descent = descent;
		_maxOverlap = maxOverlap;
		_minBounds = minBounds;
		_maxBounds = maxBounds;
		_minInkBounds = minInkBounds;
		_maxInkBounds = maxInkBounds;
	}

	public boolean getNoOverlap() {
		return _noOverlap;
	}

	public boolean getConstantMetrics() {
		return _constantMetrics;
	}

	public boolean getTerminalFont() {
		return _terminalFont;
	}

	public boolean getConstantWidth() {
		return _constantWidth;
	}

	public boolean getInkInside() {
		return _inkInside;
	}

	public boolean getInkMetrics() {
		return _inkMetrics;
	}

	public boolean getDrawDirection() {
		return _drawDirection;
	}

	public boolean getNaturalAlignment() {
		return _naturalAlignment;
	}

	public int getAscent() {
		return _ascent;
	}

	public int getDescent() {
		return _descent;
	}

	public int getMaxOverlap() {
		return _maxOverlap;
	}

	public PcfMetrics getMinBounds() {
		return _minBounds;
	}

	public PcfMetrics getMaxBounds() {
		return _maxBounds;
	}

	public PcfMetrics getMinInkBounds() {
		return _minInkBounds;
	}

	public PcfMetrics getMaxInkBounds() {
		return _maxInkBounds;
	}
}
