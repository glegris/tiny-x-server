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

public class Client {
	private final int _clientId;
	protected final ClientResources _clientResources = new ClientResources();
	private final int _endFakeID;

	private int _fakeID;

	public Client(final int clientId, final int fakeID) {
		_clientId = clientId;    
		_fakeID = fakeID;
	    _endFakeID = (_fakeID | Resource.RESOURCE_ID_MASK)+1;
	}
	
	public int getClientId() {
		return _clientId;
	}
	
	protected int allocateFakeId(){
		final int id =_fakeID++;
		if (id !=_endFakeID){
			return id;
		}
		// TODO : Error handling!
		System.out.println("error: fakeClient "+id);
		throw new RuntimeException("Error allocating fake ID");
	}	
	
	public void free() {
		_clientResources.free();
	}
}
