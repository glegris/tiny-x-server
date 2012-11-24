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
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class TranslateCoordinates implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {
		
		final XInputStream inputStream = request.getInputStream();
		final int srcWindowResourceId = inputStream.readInt();
		final int destWindowResourceId = inputStream.readInt();
		final int srcX = inputStream.readSignedShort();
		final int srcY = inputStream.readSignedShort();
		
		final Window srcWindow = server.getResources().get(srcWindowResourceId, Window.class);
		final Window destWindow = server.getResources().get(destWindowResourceId, Window.class);
		
		if(srcWindow == null) {
			response.error(Response.ErrorCode.Window, srcWindowResourceId);
			return;
		}
		
		if(destWindow == null) {
			response.error(Response.ErrorCode.Window, destWindowResourceId);
			return;
		}
		
		final boolean sameScreen = srcWindow.getScreen() == destWindow.getScreen();
		
		if(sameScreen) {
			final int absX = srcX + srcWindow.getAbsX();
			final int absY = srcY + srcWindow.getAbsY();
			final int destX = absX - destWindow.getAbsX();
			final int destY = absY - destWindow.getAbsY();
			final Window child = destWindow.childWindowAt(absX, absY);
			final XOutputStream outputStream = response.respond(1, 0);
			outputStream.writeInt(child == null ? 0 : child.getId());
			outputStream.writeShort(destX);
			outputStream.writeShort(destY);
		}
		else {
			final XOutputStream outputStream = response.respond(0, 0);
			outputStream.writeInt(0);
			outputStream.writeShort(0);
			outputStream.writeShort(0);
		}
	}
}
