/*
 *  Tiny X server - A Java X server*
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
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.ColorMap;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Visual;
import com.liaquay.tinyx.model.Window;

public class GetWindowAttributes implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {
		
		final XInputStream inputStream = request.getInputStream();		
		final int windowId = inputStream.readInt(); 		
		final Window window = server.getResources().get(windowId, Window.class);
		if(window == null) {
			response.error(Response.ErrorCode.Window, windowId);		
			return;
		}
		
		final Visual visual = window.getVisual();
		final XOutputStream outputStream = response.respond(window.getBackingStoreHint().ordinal(), 3 << 2);
		outputStream.writeInt(visual.getId());
		outputStream.writeShort(window.isInputOnly() ? 2 : 1);
		outputStream.writeByte(window.getBitGravity().ordinal());
		outputStream.writeByte(window.getWinGravity().ordinal());
		outputStream.writeInt(window.getBackingPlanes());
		outputStream.writeInt(window.getBackingPixel());
		outputStream.writeByte(window.getSaveUnders());
		outputStream.writeByte(1); // TODO Map is installed 
		outputStream.writeByte(window.getMappedState().ordinal());
		outputStream.writeByte(window.getOverrideRedirect() ? 1 : 0);
		final ColorMap colorMap = window.getColorMap();
		outputStream.writeInt(colorMap == null ? 0 : colorMap.getId());
		outputStream.writeInt(window.getEventMask());
		outputStream.writeInt(window.getEventMask());
		outputStream.writeShort(window.getDoNotPropagateMask());
	}
}
