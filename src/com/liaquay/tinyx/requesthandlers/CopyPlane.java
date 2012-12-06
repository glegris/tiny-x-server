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
import com.liaquay.tinyx.model.Server;

public class CopyPlane implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {
		
		final XInputStream inputStream = request.getInputStream();		

		int srcDrawable = inputStream.readInt();
		int dstDrawable = inputStream.readInt();

		int gc = inputStream.readInt();

		int srcX = inputStream.readUnsignedByte();
		int srcY = inputStream.readUnsignedByte();

		int dstX = inputStream.readUnsignedByte();
		int dstY = inputStream.readUnsignedByte();

		int width = inputStream.readUnsignedByte();
		int height = inputStream.readUnsignedByte();

		int bitplane = inputStream.readInt();

		Drawable s = server.getResources().get(srcDrawable, Drawable.class);
		Drawable d = server.getResources().get(dstDrawable, Drawable.class);
	}
}
