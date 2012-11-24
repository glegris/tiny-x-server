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
import com.liaquay.tinyx.model.PointerMapping;
import com.liaquay.tinyx.model.Server;

public class SetPointerMapping implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int lengthOfMap = request.getData();
		final Pointer pointer = server.getPointer();
		final PointerMapping mapping = pointer.getPointerMapping();
		
		if(lengthOfMap != mapping.getNumberOfButtons()) {
			response.error(Response.ErrorCode.Value, lengthOfMap);
			return;
		}
		
		final byte[] map = new byte[lengthOfMap];
		inputStream.read(map, 0, lengthOfMap);
		pointer.setPointerMapping(new PointerMapping(map));
		
		// Reply 0 = success, 1 = busy, 2 = failed
		// TODO implement busy behaviour (I think this means don't map depressed buttons).
		response.respond(0, 0);
	}
}
