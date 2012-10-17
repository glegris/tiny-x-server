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
