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
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class FillPoly implements RequestHandler {

	public enum Shape {
		Complex,
		Nonconvex,
		Convex;

		public static Shape getFromIndex(final int index) {
			final Shape[] values = values();
			if (index<values.length && index>=0) return values[index];
			return null;
		}
	}

	@Override
	public void handleRequest(
			final Server server, 
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

		//TODO Implement
		final Shape shape = Shape.getFromIndex(inputStream.readUnsignedByte());
		final CoordMode coordMode = CoordMode.getFromIndex(inputStream.readUnsignedByte());

		inputStream.readUnsignedShort();

		final int len = request.getLength();

		final int numCoords = ((len-16)/4);
		
		final int xCoords[] = new int[numCoords];
		final int yCoords[] = new int[numCoords];
		
		for (int i=0; i < numCoords; i++) {
			xCoords[i] = inputStream.readSignedShort();
			yCoords[i] = inputStream.readSignedShort();
		}
		
		if(CoordMode.Previous.equals(coordMode)) {
			for (int i=1; i < numCoords; i++) {
				xCoords[i] = xCoords[i] + xCoords[i-1];
				yCoords[i] = yCoords[i] + yCoords[i-1];
			}
		}
		
		drawable.getDrawableListener().polyFill(graphicsContext, xCoords, yCoords);
	}
}
