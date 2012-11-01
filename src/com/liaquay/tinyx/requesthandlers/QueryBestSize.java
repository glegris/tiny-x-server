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
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Server;

public class QueryBestSize implements RequestHandler {

	private enum BestSizeClass {
		Cursor,
		Tile,
		Stipple;
		
		public static BestSizeClass getFromIndex(final int index) {
			final BestSizeClass[] values = values();
			if(index >= 0 && index < values.length) return values[index];
			return null;
		}
	}
	
	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {
		
		final int bestClassIndex = request.getData();
		final BestSizeClass bestSizeClass = BestSizeClass.getFromIndex(bestClassIndex);
		if(bestSizeClass == null) {
			response.error(Response.ErrorCode.Match, bestClassIndex);
			return;			
		}
		final XInputStream inputStream = request.getInputStream();
		final int drawableResourceId = inputStream.readInt();
		final Drawable drawable = server.getResources().get(drawableResourceId, Drawable.class);
		if(drawable == null) {
			response.error(Response.ErrorCode.Drawable, drawableResourceId);
			return;
		}
		final int width = inputStream.readUnsignedShort();
		final int height = inputStream.readUnsignedShort();
		
		
		// TODO read best size from appropriate classes
		
		
		final int bestWidth = 32;
		final int bestHeight = 32;
		
		final XOutputStream outputStream = response.respond(1, 0);

		outputStream.writeShort(bestWidth);
		outputStream.writeShort(bestHeight);
		
	}
}
