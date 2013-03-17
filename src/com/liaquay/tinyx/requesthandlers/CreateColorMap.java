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
import java.util.logging.Logger;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.AbstractXInputStream;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.ColorMap;
import com.liaquay.tinyx.model.PseudoColorMap;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.TrueColorMap;
import com.liaquay.tinyx.model.Visual;
import com.liaquay.tinyx.model.Visual.VisualClass;
import com.liaquay.tinyx.model.Window;

public class CreateColorMap implements RequestHandler {
	private final static Logger LOGGER = Logger.getLogger(CreateColorMap.class.getName());

	@Override
	public void handleRequest(final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int allocationTypeIndex = request.getData();
		final ColorMap.AllocType allocType = ColorMap.AllocType.getFromIndex(allocationTypeIndex);
		if(allocType==null) {
			response.error(Response.ErrorCode.Value, allocationTypeIndex);
			return;
		}
		final int colorMapResourceId = inputStream.readInt();
		final int windowResourceId = inputStream.readInt(); 		
		final Window window = server.getResources().get(windowResourceId, Window.class);
		if(window == null) {
			response.error(Response.ErrorCode.Window, windowResourceId);	
			return;
		}
		final int visualResourceId = inputStream.readInt();
		final Visual visual = server.getResources().get(visualResourceId, Visual.class);
		if(visual == null) {
			response.error(Response.ErrorCode.Value, windowResourceId);	
			return;			
		}

		ColorMap map = null;

		if (visual.getVisualClass().equals(VisualClass.TrueColor)) {
			map = new TrueColorMap(colorMapResourceId);
		} else if (visual.getVisualClass().equals(VisualClass.PseudoColor)) {
			map = new PseudoColorMap(colorMapResourceId);
		} else {
			LOGGER.warning("Color map type: " + visual.getVisualClass() + " not yet supported by create");
		}
		if (map != null) {
			server.getResources().add(map);
		}
	}
}
