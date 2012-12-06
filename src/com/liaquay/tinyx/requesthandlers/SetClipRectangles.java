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
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Server;

public class SetClipRectangles implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {


		final XInputStream inputStream = request.getInputStream();		

		final int ordering = request.getData();
		//	     0     UnSorted
		//	     1     YSorted
		//	     2     YXSorted
		//	     3     YXBanded

		final int graphicsContextResourceId = inputStream.readInt();
		final GraphicsContext graphicsContext = server.getResources().get(graphicsContextResourceId, GraphicsContext.class);
		if(graphicsContext == null) {
			response.error(Response.ErrorCode.GContext, graphicsContextResourceId);
			return;
		}	       

		final int clipXOrigin = inputStream.readSignedShort();
		final int clipYOrigin = inputStream.readSignedShort();
		graphicsContext.setClipXOrigin(clipXOrigin);
		graphicsContext.setClipYOrigin(clipYOrigin);
		
		final int numRectangles = (request.getLength() - request.getInputStream().getCounter()) >> 3;
		for (int i = 0; i < numRectangles;i++) {
			final int x = inputStream.readSignedShort();
			final int y = inputStream.readSignedShort();

			final int width = inputStream.readUnsignedShort();
			final int height = inputStream.readUnsignedShort();

		}

	}
}