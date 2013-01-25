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

public class PcfToc {
	
	private PcfTocEntry[] _entries = new PcfTocEntry[9];
	
	public void add(final PcfTocEntry entry) {
		for(int i = 0; i < 8; ++i) {
			if((entry.getType() & 1<<i) != 0) {
				_entries[i] = entry;
			}
		}
	}
	
	public PcfTocEntry get(final int type) {
		if(type < 0 || type >= _entries.length) return null;
		return _entries[type];
	}
	
	public PcfTocEntry getProperties() {
		return _entries[0];
	}
	
	public PcfTocEntry getAccelerators() {
		return _entries[1];
	}
	
	public PcfTocEntry getMetrics() {
		return _entries[2];
	}
	
	public PcfTocEntry getBitmaps() {
		return _entries[3];
	}
	
	public PcfTocEntry getInkMetrics() {
		return _entries[4];
	}
	
	public PcfTocEntry getBdfEncodings() {
		return _entries[5];
	}
	
	public PcfTocEntry getSWidths() {
		return _entries[6];
	}
	
	public PcfTocEntry getGlyphNames() {
		return _entries[7];
	}	
	
	public PcfTocEntry getBdfAccelerators() {
		return _entries[8];
	}
}
