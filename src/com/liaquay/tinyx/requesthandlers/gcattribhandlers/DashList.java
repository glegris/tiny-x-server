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
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Server;

public class DashList implements GraphicsAttributeHandler {

	@Override
	public void read(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response, 
			final GraphicsContext graphicsContext) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int dashes = inputStream.readSignedByte();
		final List<Integer> dashList = new ArrayList<Integer>();
		dashList.add(dashes);
		dashList.add(dashes);
		graphicsContext.setDashes(dashList);
	}

	@Override
	public void write(final XOutputStream outputStream, final GraphicsContext graphicsContext) throws IOException {
		final List<Integer> dashList = graphicsContext.getDashes();

		for (int i=0; i < dashList.size(); i++) {
			outputStream.writeByte(dashList.get(i));
		}
	}

	@Override
	public void copy(final GraphicsContext source, final GraphicsContext destination) {
		destination.setDashes(source.getDashes());
	}
}
