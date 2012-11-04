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

public class ClientWindowAssociation {
	private final Client _client;
	private final Window _window;
	private int _eventMask = 0;
	
	public ClientWindowAssociation(final Client client, final Window window) {
		_client = client;
		_window = window;
		
		client.getClientWindowAssociations().put(window.getId(), this);
		window.getClientWindowAssociations().put(client.getClientId(), this);
	}
	
	public void setEventMask(final int eventMask) {
		_eventMask = eventMask;
	}
	
	public int getEventMask() {
		return _eventMask;
	}
	
	public Client getClient() {
		return _client;
	}
	
	public Window getWindow() {
		return _window;
	}
	
	public void free() {
		_client.getClientWindowAssociations().remove(_window.getId());
		_window.getClientWindowAssociations().remove(_client.getClientId());
	}
}
