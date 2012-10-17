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

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Map of clients against client identifier.
 * 
 * Very much not thread safe!
 */
public class Clients {
	
	private static class ClientKey {
		private int _clientId;
		
		public ClientKey(final int clientId) {
			_clientId = clientId;
		}
	}
	
	private static final Comparator<ClientKey> CLIENT_KEY_COMPARATOR = new Comparator<ClientKey>() {
		@Override
		public int compare(final ClientKey arg0, final ClientKey arg1) {
			return arg0._clientId - arg1._clientId;
		}		
	};
	
	private final Map<ClientKey, Client> _idToClientMap = new TreeMap<ClientKey, Client>(Clients.CLIENT_KEY_COMPARATOR);
	
	private ClientKey _scratchClientKey = new ClientKey(0);
	
	public void add(final Client Client){
		_idToClientMap.put(new ClientKey(Client.getClientId()), Client);
	}
	
	public Client remove(final int clientId) {
		_scratchClientKey._clientId = clientId;
		return _idToClientMap.remove(_scratchClientKey);
	}
	
	public Client get(final int clientId) {
		_scratchClientKey._clientId = clientId;
		return _idToClientMap.get(_scratchClientKey);
	}
	
	public void free() {
		
	}
}
