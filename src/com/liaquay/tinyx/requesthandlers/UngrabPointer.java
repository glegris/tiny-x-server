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
package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.PointerGrab;
import com.liaquay.tinyx.model.Server;

public class UngrabPointer implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int time = inputStream.readInt();
		final Pointer pointer = server.getPointer();
		final PointerGrab pointerGrab = pointer.getPointerGrab();
		if(pointerGrab == null) return;
		if((time - pointerGrab.getTimestamp()) < 0) return;
		if(pointer.getButtonMask() == 0) {
			server.releasePointerGrab();
		}
		else {
			pointerGrab.setExitWhenAllReleased();
		}
	}
}
