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
package com.liaquay.tinyx.requesthandlers.winattribhandlers;

import java.io.IOException;

import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.requesthandlers.AttributeHandler;

public abstract class WindowAttributeHandler implements AttributeHandler<Window> {

	@Override
	public final void write(final XOutputStream outputStream, final Window window) throws IOException {
		throw new RuntimeException("Unimplemented");
	}
}
