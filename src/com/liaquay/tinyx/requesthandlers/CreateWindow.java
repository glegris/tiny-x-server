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
import com.liaquay.tinyx.model.Visual;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.Window.WindowClass;
import com.liaquay.tinyx.requesthandlers.winattribhandlers.WindowAttributeHandlers;

public class CreateWindow implements RequestHandler {

	private final WindowAttributeHandlers _attributeHandlers;
	
	public CreateWindow(final WindowAttributeHandlers attributeHandlers){
		_attributeHandlers = attributeHandlers;
	}
	
	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int depth = request.getData();
		final int windowResourceId = inputStream.readInt();
		final int parentWindowResourceId = inputStream.readInt();
		final int x = inputStream.readSignedShort();
		final int y = inputStream.readSignedShort();
		final int width = inputStream.readUnsignedShort();
		final int height = inputStream.readUnsignedShort();
		final int borderWidth = inputStream.readUnsignedShort();
		final int windowClassIndex = inputStream.readUnsignedShort();
		final int visualResourceId = inputStream.readInt();
		final int attributeMask = inputStream.readInt();
		
		final WindowClass windowClass = WindowClass.getFromIndex(windowClassIndex);
		if(windowClass == null) {
			response.error(Response.ErrorCode.Value, windowClassIndex);
			return;
		}
		
		final Window parentWindow = server.getResources().get(parentWindowResourceId, Window.class);
		if(parentWindow == null) {
			response.error(Response.ErrorCode.Window, parentWindowResourceId);	
			return;
		}
		
		final Visual visual = visualResourceId == 0 ? 
				parentWindow.getVisual() : 
				server.getResources().get(visualResourceId, Visual.class);
		
		final Window window = new Window(
				windowResourceId,
				parentWindow,
				visual,
				depth,
				width,
				height,
				x,
				y,
				borderWidth,
				windowClass);
		
		_attributeHandlers.read(inputStream, window, attributeMask);
		
		server.getResources().add(window);
	}

}
