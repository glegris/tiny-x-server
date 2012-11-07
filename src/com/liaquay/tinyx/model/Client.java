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
	private final int _clientId;
	private final IntMap<ClientWindowAssociation> _clientWindowAssociations = new IntMap<ClientWindowAssociation>();	
	private final PostBox _postBox;

	public Client(final int clientId, final PostBox postBox) {
		_clientId = clientId;
		_postBox = postBox;
	}
	
	public int getClientId() {
		return _clientId;
	}
	
	public PostBox getPostBox() {
		return _postBox;
	}
	
	public void free() {
		final List<ClientWindowAssociation> assocs = new ArrayList<ClientWindowAssociation>(_clientWindowAssociations.values());
		for(final ClientWindowAssociation assoc : assocs) {
			assoc.free();
		}
	}
	
	public IntMap<ClientWindowAssociation> getClientWindowAssociations() {
		return _clientWindowAssociations;
	}
}
