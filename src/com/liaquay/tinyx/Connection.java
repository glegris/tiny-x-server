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

import java.util.concurrent.ArrayBlockingQueue;

import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Event;
import com.liaquay.tinyx.model.Server;

public class Connection implements Runnable {

	private final Client _client;
	private final Server _server;
	private final RequestAdaptor _request;
	private final ResponseAdaptor _response;
	private final RequestHandler _requestHandler;
	private final ArrayBlockingQueue<Event> _outTray;
	private final XOutputStream _outputStream;
	private boolean _isAlive = true;
	
	public Connection(final XInputStream inputStream,
			           final XOutputStream outputStream,
			           final Server server,
			           final Client client,
			           final RequestHandler requestHandler, 
			           final ArrayBlockingQueue<Event> outTray) {
		
		_server = server;
		_client = client;
		_request = new RequestAdaptor(inputStream);
		_response = new ResponseAdaptor(outputStream);
		_requestHandler = requestHandler;
		_outTray = outTray;
		_outputStream = outputStream;
	}
	
	public void run() {
		
		// This thread receives events for this client and delivers them to the output stream.
		final Thread parcelForce = new Thread() {
			@Override
			public void run() {
				while(_isAlive) {
					try {
						final Event event = _outTray.take();
						
						// Ensure output stream is protected from concurrent access
						// in particular from the request handler thread.
						synchronized (_outputStream) {
							try {
								event.write(_outputStream, _request.getSequenceNumber());
							}
							catch(final Exception e) {
								// TODO Log
								// TODO try to shut the client down
								e.printStackTrace();
								
								// Indicate that the connection is dead and needs closing down.
								_isAlive = false;
							}
						}
					}
					catch(final InterruptedException e) {
						// TODO Logger
						System.out.println("Parcel force exiting.");
					}
				}
			}
		};
		parcelForce.start();
		
		try {
			while(_isAlive) {
				_request.readRequest();
				
				// Ensure model is protected from concurrent access
				synchronized (_server) {
					
					// Ensure output stream is protected from concurrent access
					// in particular from the event delivery thread.
					synchronized (_outputStream) {
						_response.setRequest(_request);
						_requestHandler.handleRequest(_server, _client, _request, _response);
						_request.skipRemaining();
						_response.padAlign();
						_response.send();
					}
				}
			}
		}
		catch(final Exception e) {
			// TODO Logging
			e.printStackTrace();
		}
		finally {
			
			_isAlive = false;
			parcelForce.interrupt();
			try { parcelForce.join(); } catch(final InterruptedException e) {};
			
			synchronized (_server) {
				_server.freeClient(_client);
			}
		}
	}
}
