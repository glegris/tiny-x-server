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
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.TextExtents;
import com.liaquay.tinyx.model.Window;

public class PolyText16 implements RequestHandler {

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
		
		// TODO check drawable is not input only

		int x = inputStream.readSignedShort();
		final int y = inputStream.readSignedShort();

		while(true) {
			final int len = inputStream.readUnsignedByte();

			if(len == 0) {
				break;
			}
			if (len == 255) {
				final int font = inputStream.readInlineFontId();
				final Font f = server.getResources().get(font, Font.class);
				if(f != null) graphicsContext.setFont(f);
			}
			else {
				final int delta = inputStream.readUnsignedByte();
				final String text = inputStream.readString16(len);

				((Window) drawable).drawString(graphicsContext, text, x + delta, y);
				
				final TextExtents textExtents = graphicsContext.getFont().getTextExtents(text);
				x += delta + textExtents.getWidth();
			}
		}
	}
}
