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
import com.liaquay.tinyx.model.Atom;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class ConvertSelection implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {
		// TODO logging
		System.out.println(String.format("ERROR: unimplemented request request code %d, data %d, length %d, seq %d", 
				request.getMajorOpCode(), 
				request.getData(),
				request.getLength(),
				request.getSequenceNumber()));		
		
		final XInputStream inputStream = request.getInputStream();

		final int windowId = inputStream.readInt(); 		
		final Window window = server.getResources().get(windowId, Window.class);
		if(window == null) {
			response.error(Response.ErrorCode.Window, windowId);			
			return;
		}

		final int selectionId = inputStream.readInt(); 		
		final Atom selection = server.getAtoms().get(selectionId);
		if(selection == null) {
			response.error(Response.ErrorCode.Atom, windowId);			
			return;
		}
		
		final int targetId = inputStream.readInt(); 		
		final Atom target = server.getAtoms().get(targetId);
		if(target == null) {
			response.error(Response.ErrorCode.Atom, windowId);			
			return;
		}

		final int propertyId = inputStream.readInt(); 		
		final Atom property = server.getAtoms().get(propertyId);
		if(target == null) {
			response.error(Response.ErrorCode.Atom, windowId);			
			return;
		}

		final int timestamp = inputStream.readInt();
//	     4     TIMESTAMP                       time
//	          0     CurrentTime
	}
}
