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
package com.liaquay.tinyx.model;

import com.liaquay.tinyx.util.IntegerAllocator;

/**
 * Map of clients against client identifier.
 */
public class Clients implements PostBox {
	
	private final IntegerAllocator _clientIdAllocator = new IntegerAllocator(Resource.MAXCLIENTS);
	private final Client[] _clients = new  Client[Resource.MAXCLIENTS];

	public Clients() {
	    // Ensure the first allocation is for the server (which has a client ID of 0)
	    _clientIdAllocator.allocate();
	}
	
	public Client allocate(final PostBox postBox){
		final int clientId = _clientIdAllocator.allocate();
		if(clientId < 0) {
			return null;
		}
		else {
			final Client client = new Client(clientId, postBox);
			_clients[clientId] = client;
			return client;
		}
	}
	
	public void free(final Client client) {
		final int clientId = client.getClientId();
		_clientIdAllocator.free(clientId);
		_clients[clientId] = null;
		client.free();
	}
	
	public Client get(final int clientId) {
		return _clients[clientId];
	}
	
	public void freeAllClients() {
		for(int i = _clientIdAllocator.nextAllocated(-1) ; i >=0 ; i = _clientIdAllocator.nextAllocated(i)){
			final Client client = _clients[i];
			client.free();
			 _clients[i] = null;
		}
	}

	/**
	 * Deliver an event to all clients
	 */
	@Override
	public void send(final Event event) {
		for(int i = _clientIdAllocator.nextAllocated(-1) ; i >=0 ; i = _clientIdAllocator.nextAllocated(i)){
			final Client client = _clients[i];
			client.getPostBox().send(event);
		}
	}
}
