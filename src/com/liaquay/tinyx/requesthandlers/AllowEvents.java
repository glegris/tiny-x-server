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

public class AllowEvents implements RequestHandler {
	
	enum Mode {
		AsyncPointer,
		SyncPointer,
		ReplayPointer,
		AsyncKeyboard,
		SyncKeyboard,
		ReplayKeyboard,
		AsyncBoth,
		SyncBoth;
		
		public static Mode getFromIndex(final int index) {
			final Mode[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}
	}
	
	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {
		
		final Mode mode = Mode.getFromIndex(request.getData());
		final XInputStream inputStream = request.getInputStream();
		final int timestamp = inputStream.readInt();
		final int servertime = server.getTimestamp();
		// Time of 0 means use current server time.
		final int time = timestamp == 0 ?servertime : timestamp;

		
		switch(mode) {
		case AsyncBoth: {
			
			break;
		}
		case AsyncKeyboard:{
			server.allowKeyboardEvents(client, false, time);
			break;
		}
		case AsyncPointer:{
			
			break;
		}
		case ReplayKeyboard:{
			
			break;
		}
		case ReplayPointer:{
			
			break;
		}
		case SyncBoth:{
			server.allowKeyboardEvents(client, true, time);
			
			break;
		}
		case SyncKeyboard:{
			server.allowKeyboardEvents(client, true, time);
			break;
		}
		case SyncPointer:{
			
			break;
		}
		}
		
		
		
		
		// TODO logging
		System.out.println(String.format("ERROR: unimplemented request request code %d, data %d, length %d, seq %d", 
				request.getMajorOpCode(), 
				request.getData(),
				request.getLength(),
				request.getSequenceNumber()));		
	}
}
