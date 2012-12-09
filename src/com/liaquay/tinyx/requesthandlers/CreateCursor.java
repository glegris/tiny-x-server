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
import com.liaquay.tinyx.model.Cursor;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Server;

public class CreateCursor implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();

		final int cursorId = inputStream.readInt();
		final Cursor cursor = new Cursor(cursorId);
		server.getResources().add(cursor);

		final int sourcePixmap = inputStream.readInt();
		final Pixmap sourcePixmapRes = server.getResources().get(sourcePixmap, Pixmap.class);
		if (sourcePixmapRes == null) {
			response.error(Response.ErrorCode.Pixmap, sourcePixmap);
			return;
		}

		final int maskPixmap = inputStream.readInt();
		final Pixmap maskPixmapRes = server.getResources().get(maskPixmap, Pixmap.class);
		if (maskPixmapRes == null) {
			response.error(Response.ErrorCode.Pixmap, maskPixmap);
			return;
		}

		// Both pixmaps must have a 
		if (maskPixmapRes.getDepth() != 1 || sourcePixmapRes.getDepth() != 1) {
			response.error(Response.ErrorCode.Match, maskPixmap);
			return;
		}

		final int foregroundRed = inputStream.readUnsignedShort();
		final int foregroundGreen = inputStream.readUnsignedShort();
		final int foregroundBlue = inputStream.readUnsignedShort();

		final int backgroundRed = inputStream.readUnsignedShort();
		final int backgroundGreen = inputStream.readUnsignedShort();
		final int backgroundBlue = inputStream.readUnsignedShort();

		final int x = inputStream.readUnsignedShort();
		final int y = inputStream.readUnsignedShort();

		cursor.setX(x);
		cursor.setY(y);
		//		cursor.setForegroundColor();
		//		cursor.setBackgroundColor();
	}
}
