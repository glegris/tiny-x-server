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
import java.util.Map;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.extensions.Extension;

public class QueryExtension implements RequestHandler {

	Map<String, Extension> _extensionMap;

	public QueryExtension(Map<String, Extension> extensionMap) {
		this._extensionMap = extensionMap;
	}

	@Override
	public void handleRequest(final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final String requestedExtensionName = request.getInputStream().readString();

		System.out.print("Extension query for " + requestedExtensionName);

		for (String extensionName : _extensionMap.keySet()) {
			if	(extensionName.equals(requestedExtensionName)){
				final Extension ext = _extensionMap.get(extensionName);

				// Extension present
				writeResponse(response, ext);
				
				return;
			}
		}

		// Extension not found
		writeResponse(response, null);
	}

	private void writeResponse(Response response, Extension ext) throws IOException {
		if (ext == null) {
			System.out.println(".... Not found");
		} else {
			System.out.println(".... Found");
		}
		final XOutputStream outputStream = response.respond(1, 0);
		outputStream.writeByte(ext == null ? 0 : 1);  
		outputStream.writeByte(ext == null ? 0 : ext.getMajorOpCode());
		outputStream.writeByte(ext == null ? 0 : ext.getFirstEvent());
		outputStream.writeByte(ext == null ? 0 : ext.getFirstError());
		outputStream.writePad(20);
	}
}
