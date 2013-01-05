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
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Screen;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class PolySegment implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int drawableResourceId = inputStream.readInt();
		final Drawable drawable = server.getResources().get(drawableResourceId, Drawable.class);
		if(drawable == null) {
			response.error(Response.ErrorCode.Drawable, drawableResourceId);
			return;
		}
		final int graphicsContextResourceId = inputStream.readInt();
		final GraphicsContext graphicsContext = server.getResources().get(graphicsContextResourceId, GraphicsContext.class);
		if(graphicsContext == null) {
			response.error(Response.ErrorCode.GContext, graphicsContextResourceId);
			return;
		}

		int len = request.getLength();

		int numSegments = ((len-12)/8);

		int xCoords1[] = new int[numSegments];
		int yCoords1[] = new int[numSegments];
		int xCoords2[] = new int[numSegments];
		int yCoords2[] = new int[numSegments];

		for (int i=0; i < numSegments; i++) {
			xCoords1[i] = inputStream.readSignedShort();
			yCoords1[i] = inputStream.readSignedShort();
			xCoords2[i] = inputStream.readSignedShort();
			yCoords2[i] = inputStream.readSignedShort();
		}

		for (int i=0; i < numSegments; i++) {
			if (drawable instanceof Window) {
				((Window) drawable).drawLine(graphicsContext, xCoords1[i], yCoords1[i], xCoords2[i], yCoords2[i]);
			} else {
				((Screen) drawable).drawLine(graphicsContext, xCoords1[i], yCoords1[i], xCoords2[i], yCoords2[i]);
			}
		}
	}
}
