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
package com.liaquay.tinyx.requesthandlers.gcattribhandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.Response.ErrorCode;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Server;

public class Stipple implements GraphicsAttributeHandler {

	@Override
	public void read(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response, 
			final GraphicsContext graphicsContext) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int stipplePixmap = inputStream.readInt();
		final Pixmap p = (Pixmap) server.getResources().get(stipplePixmap, Pixmap.class);
		if (p != null) {
			graphicsContext.setStipple(p);
		} else {
			response.error(ErrorCode.Pixmap, stipplePixmap);
		}
	}

	@Override
	public void write(final XOutputStream outputStream, final GraphicsContext graphicsContext) throws IOException {
		if (graphicsContext.getStipple() != null) {
			outputStream.writeInt(graphicsContext.getStipple().getId());
		}
	}

	@Override
	public void copy(final GraphicsContext source, final GraphicsContext destination) {
		destination.setStipple(source.getStipple());
	}
}
