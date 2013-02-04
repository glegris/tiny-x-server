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

import com.liaquay.tinyx.util.IntMap;

public class PcfEncodings {
	private final int _firstCol;
	private final int _lastCol;
	private final int _firstRow;
	private final int _lastRow;
	private final int _defaultCharacter;
	private final IntMap<Integer> _characterMap;
	private final int _minChar;
	private final int _maxChar;

	public PcfEncodings(
			final int firstCol,
			final int lastCol, 
			final int firstRow, 
			final int lastRow,
			final int defaultCharacter, 
			final IntMap<Integer> characterMap,
			final int minChar,
			final int maxChar) {

		_firstCol = firstCol;
		_lastCol = lastCol;
		_firstRow = firstRow;
		_lastRow = lastRow;
		if(characterMap == null) {
			System.out.println("Oh dear");
		}
		_defaultCharacter = defaultCharacter;
		_characterMap = characterMap;
		_minChar = minChar;
		_maxChar = maxChar;
	}

	public int getFirstCol() {
		return _firstCol;
	}

	public int getLastCol() {
		return _lastCol;
	}

	public int getFirstRow() {
		return _firstRow;
	}

	public int getLastRow() {
		return _lastRow;
	}

	public int getDefaultCharacter() {
		return _defaultCharacter;
	}

	public IntMap<Integer> getCharacterMap() {
		return _characterMap;
	}
	
	public int getGlyphIndex(final char c) {
		final int i = c & 0xffff;
		final Integer m = _characterMap.get(i);
		if(m == null) {
			final Integer d = _characterMap.get(_defaultCharacter);
			if(d ==  null) {
				return 32;
			}
			else {
				return d;
			}
		}
		return m;
	}
	
	public int getMinCharacter() {
		return _minChar;
	}
	
	public int getMaxCharacter() {
		return _maxChar;
	}
}
