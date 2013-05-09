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
import com.liaquay.tinyx.model.Cursor;
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.PointerGrab;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class GrabPointer implements RequestHandler {

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
		final int eventMask = inputStream.readUnsignedShort();
		final boolean pointerSynchronous = inputStream.readUnsignedByte() == 0;
		final boolean keyboardSynchronous = inputStream.readUnsignedByte() == 0;
		
		final int confineToWindowId = inputStream.readInt();
		final Window confineToWindow;
		if(confineToWindowId == 0) {
			confineToWindow = null;
		}
		else {
			confineToWindow = server.getResources().get(confineToWindowId, Window.class);
			if(confineToWindow == null) {
				response.error(Response.ErrorCode.Window, confineToWindowId);
				return;
			}
			if(!confineToWindow.isViewable()) {
				response.respond(GrabResponse.NotViewable.ordinal());
				return;
			}
		}

		final int cursorId = inputStream.readInt();
		final Cursor cursor;
		if(cursorId == 0) {
			cursor = null;
		}
		else {
			cursor = server.getResources().get(cursorId, Cursor.class);
			if(cursor == null) {
				response.error(Response.ErrorCode.Cursor, cursorId);
				return;
			}
		}

		final int timestamp = inputStream.readInt();
		final int servertime = server.getTimestamp();
		// Time of 0 means use current server time.
		final int time = timestamp == 0 ?servertime : timestamp;
		
		final Pointer pointer = server.getPointer();
		final PointerGrab currentPointerGrab = pointer.getPointerGrab();
		if(currentPointerGrab != null) {
			if(currentPointerGrab.getClient() == client) {
				if(time - currentPointerGrab.getTimestamp() < 0 || servertime - time < 0) {
					response.respond(GrabResponse.InvalidTime.ordinal());
					return;					
				}
			}
			else {
				response.respond(
						currentPointerGrab.isPointerSynchronous() ?
								GrabResponse.Frozen.ordinal() :
								GrabResponse.AlreadyGrabbed.ordinal()
						);
			}
			return;
		}
				
		final PointerGrab pointerGrab = new PointerGrab(
				client,
				ownerEvents,
				grabWindow,
				eventMask,
				pointerSynchronous,
				keyboardSynchronous,
				confineToWindow,
				cursor,
				time);
		
		server.setPointerGrab(pointerGrab);

		response.respond(GrabResponse.Success.ordinal());
	}
}
