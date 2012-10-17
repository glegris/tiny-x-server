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
 * 
 */
public class Server extends Client {
	
	private static final byte[] VENDOR = "Liaquay".getBytes();

	private static Format[] FORMATS = new Format[] {
		new Format (32, 24, 8)
	};
	
	private IntegerAllocator _clientIdAllocator = new IntegerAllocator(Resource.MAXCLIENTS);
	private Clients _clients = new Clients();
	private Keyboard _keyboard = new Keyboard(); // TODO configurable
	private Screen[] _screens = new Screen[1]; // TODO configure screens

	public Server() {
		// Create the server as a client with ID of 0
		super(0, Resource.SERVER_MINID);
	    
	    // Ensure the first allocation is for the server (which has a client ID of 0)
	    _clientIdAllocator.allocate();
	    
	    // TODO not sure we should do this!
	    // TODO How accessible should the server client be?
	    _clients.add(this);
	    
	    final int visualId = allocateFakeId();
	    final int rootWindowResourceId = allocateFakeId();
	    final int colorMapResourceId = allocateFakeId();
	    final int fontResourceId = allocateFakeId();
	    final int rootCursorResourceId = allocateFakeId();
	    
	    // TODO all the stuff below needs to be configurable
	    
	    final Visual visual = new Visual(visualId);
	    _clientResources.add(visual);
	    
	    final Depth[] depths = new Depth[] {
	    	new Depth(32, new Visual[] {visual})
	    };
	    
	    final RootWindow rootWindow = new RootWindow(rootWindowResourceId);
	    _clientResources.add(rootWindow);
	    
	    final ColorMap defaultColorMap = new TrueColorMap(colorMapResourceId);
	    _clientResources.add(defaultColorMap);
	    
	    _screens[0] = new Screen(rootWindow, 
	    		                 defaultColorMap,
	    		                 visual,
	    		                 32,
	    		                 1280,
	    		                 800,
	    		                 1280,
	    		                 800,
	    		                 depths); // Save unders
	}
	
	public Client allocateClient() {
		final int clientId = _clientIdAllocator.allocate();
		if(clientId < 0) {
			return null;
		}
		else {
			final Client client = new Client(clientId, (clientId<<Resource.CLIENTOFFSET) | Resource.SERVER_BIT);
			_clients.add(client);
			return client;
		}
	}
	
	public void freeClient(final Client client) {
		final int clientId = client.getClientId();
		if(clientId == 0) {
			throw new RuntimeException("Don't try to free the server silly");
		}
		client.free();
		_clientIdAllocator.free(clientId);
	}
	
	public byte[] getVendor() {
		return VENDOR;
	}
	
	public Format[] getFormats() {
		return FORMATS;
	}
	
	public Screen[] getScreens() {
		return _screens;
	}
	
	public Keyboard getKeyboard() {
		return _keyboard;
	}
}
