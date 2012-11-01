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

public class ListExtensions implements RequestHandler {

	private final Map<String, Extension> _extensionMap;

	public ListExtensions(final Map<String, Extension> extensionMap) {
		this._extensionMap = extensionMap;
	}

	//	     1     1                               Reply
	//	     1     CARD8                           number of STRs in names
	//	     2     CARD16                          sequence number
	//	     4     (n+p)/4                         reply length
	//	     24                                    unused
	//	     n     LISTofSTR                       names
	//	     p                                     unused, p=pad(n)
	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final XOutputStream outputStream = response.respond(_extensionMap.size());
		response.padHeader();
		for (final String extensionName : _extensionMap.keySet()) {
			final byte[] b = extensionName.getBytes();
			outputStream.writeByte(b.length);
			outputStream.write(b, 0, b.length);
		}
	}
}
