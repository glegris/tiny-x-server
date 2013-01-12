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

public class CopyArea implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();		

		final int srcDrawable = inputStream.readInt();
		final Drawable s = server.getResources().get(srcDrawable, Drawable.class);
		if(s == null) {
			response.error(Response.ErrorCode.Drawable, srcDrawable);	
			return;			
		}

		final int dstDrawable = inputStream.readInt();
		final Drawable d = server.getResources().get(dstDrawable, Drawable.class);
		if(d == null) {
			response.error(Response.ErrorCode.Drawable, dstDrawable);	
			return;	
		}

		// Both drawables need to have the same depth
		if (s.getDepth() != d.getDepth()) {
			response.error(ErrorCode.Match, d.getId());
			return;	
		}

		// Both drawables need to be on the same root window
		if (!s.getScreen().getRootWindow().equals(d.getScreen().getRootWindow())) {
			response.error(ErrorCode.Match, d.getId());
			return;	
		}

		final int gcId = inputStream.readInt();
		final GraphicsContext graphicsContext = server.getResources().get(gcId, GraphicsContext.class);
		if(graphicsContext == null) {
			response.error(Response.ErrorCode.GContext, gcId);
			return;
		}

		final int srcX = inputStream.readUnsignedShort();
		final int srcY = inputStream.readUnsignedShort();

		final int dstX = inputStream.readUnsignedShort();
		final int dstY = inputStream.readUnsignedShort();

		final int width = inputStream.readSignedShort();
		final int height = inputStream.readSignedShort();

		d.getListener().copyArea(s, graphicsContext, srcX, srcY, width, height, dstX, dstY);
	}
}
