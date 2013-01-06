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
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Image.ImageType;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class GetImage implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {

		ImageType type = ImageType.getFromIndex(request.getData());

		final XInputStream inputStream = request.getInputStream();

		final int drawable = inputStream.readInt();
		final Drawable drawableRes = server.getResources().get(drawable, Drawable.class);
		if (drawableRes == null) {
			response.error(Response.ErrorCode.Drawable, drawable);
			return;
		}
		final int x = inputStream.readSignedShort();
		final int y = inputStream.readSignedShort();

		final int width = inputStream.readUnsignedShort();
		final int height = inputStream.readUnsignedShort();

		final int planeMask = inputStream.readInt();

		final XOutputStream outputStream = response.respond(1);

		// Depth of the drawable for which we are going to send back data.
		outputStream.writeByte(drawableRes.getDepth());

		outputStream.writeInt(drawableRes.getVisual().getId());
		outputStream.writePad(20);
		
		if (drawableRes instanceof Pixmap) {
			Pixmap p = (Pixmap) drawableRes;
			
			// Check that the specified region is wholly contained within the pixmap
			if (x+width > p.getWidth() || y+height > p.getHeight()) {
				response.error(ErrorCode.Match, p.getId());
			}
			
			// Null image
			if (p.getData() != null) {
				PixmapUtils.writeZPixmap(p, planeMask, x, y, width, height);
			} else {
				PixmapUtils.writeNullZPixmap(outputStream, p, planeMask, x, y, width, height);
			}
		} else if (drawableRes instanceof Window) {
			Window w = (Window) drawableRes;

			int size = (width * height * drawableRes.getDepth()) / 8;

			for (int i = 0; i < size; i++)
				outputStream.writeByte(0);
			
		}

		//TODO: Output the data!!!!!
//	     n     LISTofBYTE                      data
//	     p                                     unused, p=pad(n)
	}
}
