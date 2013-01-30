/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Nathan Ludkin
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
package com.liaquay.tinyx.model;

import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.model.font.FontFactory;

public class Font extends AbstractResource {

	private final FontDetail _fontDetail;
	private final FontFactory _fontFactory;
	
	public Font(
			final int id, 
			final FontDetail fontDetail,
			final FontFactory fontFactory) {
		super(id);
		_fontDetail = fontDetail;
		_fontFactory = fontFactory;
	}

	@Override
	public void free() {
		_fontFactory.close(_fontDetail);
	}
	
	public FontDetail getFontDetail() {
		return _fontDetail;
	}
}
