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
import com.liaquay.tinyx.model.Keyboard;
import com.liaquay.tinyx.model.KeyboardGrab;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class GrabKeyboard implements RequestHandler {
	
	enum GrabResponse {
		Success,
		AlreadyGrabbed,
		InvalidTime,
		NotViewable,
		Frozen
	}
	
	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {
		
		final XInputStream inputStream = request.getInputStream();
		final boolean ownerEvents = request.getData() != 0;
		final int windowId = inputStream.readInt();
		final Window grabWindow = server.getResources().get(windowId, Window.class);
		if(grabWindow == null) {
			response.error(Response.ErrorCode.Window, windowId);
			return;
		}
		if(!grabWindow.isViewable()) {
			response.respond(GrabResponse.NotViewable.ordinal());
			return;
		}
		final int timestamp = inputStream.readInt();
		final int servertime = server.getTimestamp();
		// Time of 0 means use current server time.
		final int time = timestamp == 0 ?servertime : timestamp;
		final boolean pointerSynchronous = inputStream.readUnsignedByte() == 0;
		final boolean keyboardSynchronous = inputStream.readUnsignedByte() == 0;
		final Keyboard keyboard = server.getKeyboard();
		final KeyboardGrab currnetKeyboardGrab = keyboard.getKeyboardGrab();
		
		if(currnetKeyboardGrab != null) {
			if(currnetKeyboardGrab.getClient() == client) {
				if(time - currnetKeyboardGrab.getTimestamp() < 0 || servertime - time < 0) {
					response.respond(GrabResponse.InvalidTime.ordinal());
					return;					
				}
			}
			else {
				response.respond(
						currnetKeyboardGrab.isKeyboardSynchronous() ?
								GrabResponse.Frozen.ordinal() :
								GrabResponse.AlreadyGrabbed.ordinal()
						);
			}
			return;
		}
				
		final KeyboardGrab keyboardGrab = new KeyboardGrab(
				client,
				ownerEvents,
				grabWindow,
				pointerSynchronous,
				keyboardSynchronous,
				time);
		
		server.setKeyboardGrab(keyboardGrab);

		response.respond(GrabResponse.Success.ordinal());
	}
}
