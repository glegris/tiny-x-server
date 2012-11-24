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
import com.liaquay.tinyx.Response.ErrorCode;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class QueryPointer implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final XInputStream is = request.getInputStream();

		// Get the window that we are requesting the pointer for.
		final int windowId = is.readInt();
		final Window window = server.getResources().get(windowId, Window.class);
		
		// Send a bad window error
		if (window == null) {
			response.error(ErrorCode.Window, windowId);
			return;
		}
		
		final Window root = window.getScreen();
		final int rootWindowId = root.getId();
		
		// Create the response
		final XOutputStream os = response.respond(1, 24);
		final Pointer p = server.getPointer();

		os.writeInt(rootWindowId);
		os.writeInt(rootWindowId == windowId ? 0 : windowId);
		os.writeShort(p.getX());
		os.writeShort(p.getY());
		// TODO the following should be window relative coords
		// TODO i.e. p.x - w.x (where w.x is the absolute coordinate of the window).
		os.writeShort(window.getScreen().getWidthPixels() - p.getX());
		os.writeShort(window.getScreen().getHeightPixels() - p.getY());
		os.writeShort(p.getState());
	}

}
