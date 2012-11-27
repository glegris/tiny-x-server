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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Extension;
import com.liaquay.tinyx.model.Server;

public class QueryExtension implements RequestHandler {

	private final static Logger LOGGER = Logger.getLogger(QueryExtension.class.getName());
	
	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final String requestedExtensionName = request.getInputStream().readString(2);
		
		LOGGER.log(Level.INFO, "Extension query for " + requestedExtensionName);

		final Extension ext = server.getExtensions().getExtension(requestedExtensionName);
		
		final XOutputStream outputStream = response.respond(1, 0);
		if(ext == null) {
			outputStream.writeByte(0);
		}
		else {
			// Extension present
			outputStream.writeByte(1);
			outputStream.writeByte(ext.getMajorOpCode());
			outputStream.writeByte(ext.getFirstEventCode());
			outputStream.writeByte(ext.getFirstErrorCode());
		}
	}
}
