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
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.TextExtents;
import com.liaquay.tinyx.model.font.FontDetail;

public class QueryTextExtents implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int fid = inputStream.readInt();
		final Font font = (Font) server.getResources().get(fid);
		if(font == null) {
			response.error(Response.ErrorCode.Font, fid);
			return;
		}
		final boolean odd = request.getData() != 0;
		final String text = inputStream.readString16((request.getLength()>>1) - (odd ? 5 : 4));
		final FontDetail fontDetail = font.getFontDetail();
		final TextExtents textExtents = fontDetail.getTextExtents(text);
		final XOutputStream outputStream = response.respond(fontDetail.isLeftToRight() ? 0 : 1);
		outputStream.writeShort(fontDetail.getAscent());
		outputStream.writeShort(fontDetail.getDescent());
		outputStream.writeShort(textExtents.getAscent());
		outputStream.writeShort(textExtents.getDescent());
		outputStream.writeInt(textExtents.getWidth());
		outputStream.writeInt(textExtents.getLeft());
		outputStream.writeInt(textExtents.getRight());
	}
}
