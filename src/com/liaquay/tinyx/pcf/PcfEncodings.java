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

public class PcfEncodings {
	private final int firstCol;
	private final int lastCol;
	private final int firstRow;
	private final int lastRow;
	private final int defaultCharacter;
	private final int[] characterMap;

	public PcfEncodings(
			int firstCol,
			int lastCol, 
			int firstRow, 
			int lastRow,
			int defaultCharacter, 
			int[] characterMap) {

		this.firstCol = firstCol;
		this.lastCol = lastCol;
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.defaultCharacter = defaultCharacter;
		this.characterMap = characterMap;
	}

	public int getFirstCol() {
		return firstCol;
	}

	public int getLastCol() {
		return lastCol;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public int getDefaultCharacter() {
		return defaultCharacter;
	}

	public int[] getCharacterMap() {
		return characterMap;
	}
	
	public int getGlyphIndex(final char c) {
		final int i = c & 0xffff;
		if(i < 0 || i >=characterMap.length) return defaultCharacter;
		return characterMap[i];
	}
}
