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

import java.util.Iterator;

import com.liaquay.tinyx.util.IntegerAllocator;

/**
 * Map of clients against client identifier.
 */
public class Clients {
	
	private final IntegerAllocator _clientIdAllocator = new IntegerAllocator(Resource.MAXCLIENTS);
	private final Client[] _clients = new  Client[Resource.MAXCLIENTS];

	public Clients() {
	    // Ensure the first allocation is for the server (which has a client ID of 0)
	    _clientIdAllocator.allocate();
	}
	
	public Client allocate(final PostBox postBox, final Host host){
		final int clientId = _clientIdAllocator.allocate();
		if(clientId < 0) {
			return null;
		}
		else {
			final Client client = new Client(clientId, postBox, host);
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

	public Iterator<Client> iterator() {
		return new Iterator<Client>() {
			private int i = _clientIdAllocator.nextAllocated(0);
			private int j = -1;
			
			@Override
			public boolean hasNext() {
				return i >= 0;
			}

			@Override
			public Client next() {
				if(i < 0) return null;
				final Client c = _clients[i];
				j = i;
				i = _clientIdAllocator.nextAllocated(i);
				return c;
			}

			@Override
			public void remove() {
				if(j >= 0) {
					_clientIdAllocator.free(j);
					_clients[j] = null;
				}
			}
		};
	}
	
	/**
	 * Deliver an event to all clients
	 */
	public void send(final Event event) {
		for(int i = _clientIdAllocator.nextAllocated(0) ; i >=0 ; i = _clientIdAllocator.nextAllocated(i)){
			final Client client = _clients[i];
			client.getPostBox().send(event, null);
		}
	}
}
