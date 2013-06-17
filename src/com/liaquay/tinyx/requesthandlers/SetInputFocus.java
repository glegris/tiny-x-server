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
import com.liaquay.tinyx.model.Focus;
import com.liaquay.tinyx.model.Focus.RevertTo;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class SetInputFocus implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {
		
		final int revertToIndex = request.getData();
		final XInputStream inputStream = request.getInputStream();		
		final int windowId = inputStream.readInt(); 		
		
		final RevertTo revertTo = RevertTo.getFromIndex(revertToIndex);
		if(revertTo == null) {
			response.error(Response.ErrorCode.Value, windowId);		
			return;			
		}
		
		final int timestamp = inputStream.readInt();
		final Focus.Mode mode;
		final Window window; 
		switch (windowId) {
		case 0: {
			window = null;
			mode = Focus.Mode.None;
			break;
		}
		case 1: {
			window = server.getPointer().getScreen().getRootWindow();
			mode = Focus.Mode.PointerRoot;
			break;
		}
		default:{ 
			mode = Focus.Mode.Window; 
			window = server.getResources().get(windowId, Window.class);
			if(window == null) {
				response.error(Response.ErrorCode.Window, windowId);		
				return;
			}
			
			break;
		}
		}
		// The specified focus window must be viewable at the time XSetInputFocus() is called, or a BadMatch error results. 
		if(window != null && !window.isViewable()) {
			response.error(Response.ErrorCode.Match, windowId);		
			return;
		}
		final Focus focus = server.getFocus();
		focus.set(mode, window, revertTo, timestamp);
	}
}
