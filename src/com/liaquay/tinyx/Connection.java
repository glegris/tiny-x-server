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

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.TinyXServer.Executable;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Event;
import com.liaquay.tinyx.model.Server;

public class Connection implements Executable {
	
	private final static Logger LOGGER = Logger.getLogger(Connection.class.getName());

	private final Client _client;
	private final Server _server;
	private final RequestAdaptor _request;
	private final ResponseAdaptor _response;
	private final RequestHandler _requestHandler;
	private final ArrayBlockingQueue<Event> _outTray;
	private final XOutputStream _outputStream;
	private final XInputStream _inputStream;
	
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
		_inputStream = inputStream;
	}
	
	@Override
	public void stop() {
		_isAlive = false;
		try{ _outputStream.close(); } catch(final IOException e) {}
		try{ _inputStream.close(); } catch(final IOException e) {}
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
								LOGGER.log(Level.SEVERE, "Failed to deliver event", e);
								
								// Indicate that the connection is dead and needs closing down.
								Connection.this.stop();
							}
						}
					}
					catch(final InterruptedException e) {
						LOGGER.log(Level.INFO, "Parcel force exiting.");
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
			// Do not issue an error message on normal exit.
			// TODO this is a bit messy!
			final boolean eos = e.getMessage() != null && e.getMessage().equals("END OF STREAM!");
			if(!(eos && _inputStream.getCounter() ==0 && _outputStream.getCounter() == 0)) {
				LOGGER.log(Level.SEVERE, "Failure in client processing", e);
			}
		}
		finally {
			LOGGER.log(Level.INFO, "Client " + _client.getClientId() + " thread exiting.");
			_isAlive = false;
			parcelForce.interrupt();
			try { parcelForce.join(); } catch(final InterruptedException e) {};
			
			synchronized (_server) {
				_server.freeClient(_client);
			}
		}
	}
}
