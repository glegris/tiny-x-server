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
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class UngrabKey implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {
		
		final XInputStream inputStream = request.getInputStream();
		final int keyCode = request.getData(); // 0 AnyKey
		final int windowId = inputStream.readInt();
		final Window grabWindow = server.getResources().get(windowId, Window.class);
		if(grabWindow == null) {
			response.error(Response.ErrorCode.Window, windowId);
			return;
		}
		final int keyMask = inputStream.readUnsignedShort(); // #x8000 AnyModifier

		// TODO logging
		System.out.println(String.format("ERROR: unimplemented request request code %d, data %d, length %d, seq %d", 
				request.getMajorOpCode(), 
				request.getData(),
				request.getLength(),
				request.getSequenceNumber()));		
	}
}
