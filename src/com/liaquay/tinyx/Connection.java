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
package com.liaquay.tinyx;

import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class Connection implements Runnable {

	private final Client _client;
	private final Server _server;
	private final RequestAdaptor _request;
	private final ResponseAdaptor _response;
	private final RequestHandler _requestHandler;
	
	public Connection(final XInputStream inputStream,
			           final XOutputStream outputStream,
			           final Server server,
			           final Client client,
			           final RequestHandler requestHandler) {
		
		_server = server;
		_client = client;
		_request = new RequestAdaptor(inputStream);
		_response = new ResponseAdaptor(outputStream);
		_requestHandler = requestHandler;
	}
	
	public void run() {
		
		try {
			while(true) {
				_request.readRequest();
				_response.setRequest(_request);
				_requestHandler.handleRequest(_server, _client, _request, _response);
				_request.skipRemaining();
				_response.send();
			}
		}
		catch(final Exception e) {
			
		}
		finally {
			_server.freeClient(_client);
		}
	}
}
