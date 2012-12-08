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

import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.util.IntMap;

public class Client {
	
	public interface Listener {
		public void closed();
	}
	
	private final static class NullListener implements Listener {
		@Override
		public void closed() {}
	}
	
	private static final Listener NULL_LISTENER = new NullListener();
	
	private Listener _listener = NULL_LISTENER;
	
	public void setListener(final Listener listener) {
		_listener = listener;
	}
	
	private final int _clientId;
	// TODO this is probably not needed
	// Currently only used to remove a clients resources.
	// This could be done from the window which is a resource.
	// Currently performing a linear scan of resources on close of a client to remove unwanted ones.
	private final IntMap<ClientWindowAssociation> _clientWindowAssociations = new IntMap<ClientWindowAssociation>();	
	private final PostBox _postBox;
	private final Host _host;

	public Client(final int clientId, final PostBox postBox, final Host host) {
		_clientId = clientId;
		_postBox = postBox;
		_host = host;
	}
	
	public int getClientId() {
		return _clientId;
	}
	
	public PostBox getPostBox() {
		return _postBox;
	}
	
	public void free() {
		_listener.closed();
		final List<ClientWindowAssociation> assocs = new ArrayList<ClientWindowAssociation>(_clientWindowAssociations.values());
		for(final ClientWindowAssociation assoc : assocs) {
			assoc.free();
		}
	}
	
	public void add(final ClientWindowAssociation assoc) {
		_clientWindowAssociations.put(assoc.getWindow().getId(), assoc);
	}
	
	public void remove(final ClientWindowAssociation assoc) {
		_clientWindowAssociations.remove(assoc.getWindow().getId());
	}
}
