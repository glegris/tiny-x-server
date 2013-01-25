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

public class PcfStringBitmapRenderer implements PcfBitmaps.Renderer {

	private final int _width;
	private final int _height;
	private final char[][] _chars;
	
	public PcfStringBitmapRenderer(final int width,  final int height) {
		_width = width;
		_height = height;
		_chars = new char[height][width];
		for(int x = 0; x < width; ++x ){
			for(int y = 0; y < height; ++y ){
				_chars[y][x] = '.';
			}
		}
	}
	
	@Override
	public void render(int x, int y) {
		if(x < 0 || x>=_width || y <0 || y>=_height) return;
		_chars[y][x] = '@';
	}
	
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for(int y = 0; y < _height; ++y ){
			sb.append(_chars[y]);
			sb.append('\n');
		}		
		return sb.toString();
	}
}
