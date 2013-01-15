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
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.GraphicsContextAttributeHandlers;

public class CreateGraphicsContext implements RequestHandler {

	private final GraphicsContextAttributeHandlers _attributeHandlers; 
	
	public CreateGraphicsContext(final GraphicsContextAttributeHandlers attributeHandlers) {
		_attributeHandlers = attributeHandlers;
	}

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int graphicsContextId = inputStream.readInt();
		final int drawableResourceId = inputStream.readInt();
		final Drawable drawable = server.getResources().get(drawableResourceId, Drawable.class);
		if(drawable == null) {
			response.error(Response.ErrorCode.Drawable, drawableResourceId);
			return;
		}
		final GraphicsContext graphicsContext = new GraphicsContext(graphicsContextId, drawable, server.getFixedFont());
		final int attributeMask = inputStream.readInt();
		_attributeHandlers.read(server, client, request, response, graphicsContext, attributeMask);

		if(!ErrorCode.None.equals(response.getResponseCode())) {
			graphicsContext.free();
			return;
		}
		server.getResources().add(graphicsContext);
	}
}
