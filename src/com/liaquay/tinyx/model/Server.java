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

import com.liaquay.tinyx.io.ByteOrder;
import com.liaquay.tinyx.model.Visual.BackingStoreSupport;
import com.liaquay.tinyx.model.Visual.VisualClass;
import com.liaquay.tinyx.util.IntegerAllocator;


/**
 * 
 */
public class Server extends Client {
	
	private static final byte[] VENDOR = "Liaquay".getBytes();

	private static Format[] FORMATS = new Format[] {
		new Format (32, 24, 8)
	};
	
	private final IntegerAllocator _clientIdAllocator = new IntegerAllocator(Resource.MAXCLIENTS);
	private final Clients _clients = new Clients();
	private final Keyboard _keyboard = new Keyboard(); // TODO configurable
	private final Screen[] _screens = new Screen[1]; // TODO configure screens
	private final Resources _resources = new Resources();
	private final Atoms _atoms = new Atoms();
	private final Focus _focus;
	private final ByteOrder _imageByteOrder = ByteOrder.LSB;
	private final ByteOrder _bitmapBitOrder = ByteOrder.MSB;
	private final int _bitmapScanLineUnit = 8; 
	private final int _bitmapScanLinePad = 8; 

	public Server() {
		// Create the server as a client with ID of 0
		super(0, Resource.SERVER_MINID);
	    
	    // Ensure the first allocation is for the server (which has a client ID of 0)
	    _clientIdAllocator.allocate();
	    
	    // TODO not sure we should do this!
	    // TODO How accessible should the server client be?
	    _clients.add(this);
	    
	    // TODO Need to be able to make multiple screens!!!
	    final int visualId = allocateFakeId();
	    final int rootWindowResourceId = allocateFakeId();
	    final int colorMapResourceId = allocateFakeId();
	    final int fontResourceId = allocateFakeId();
	    final int rootCursorResourceId = allocateFakeId();
	    
	    // TODO all the stuff below needs to be configurable
	    
	    final Visual visual = new Visual(
	    		visualId, 
	    		32,
	    		BackingStoreSupport.BackingStoreAlways,
	    		VisualClass.TrueColor, 
	    		8,  // Bits Per RGB
	    		256, // TODO How do colour maps relate to visuals
	    		0x000000ff, // Red mask
	    		0x0000ff00, // Green mask
	    		0x00ff0000  // Blue mask
	    		);
	    
	    _resources.add(visual);
	    
	    final Depths depths = new Depths();
	    depths.add(visual);   
	    
	    final ColorMap defaultColorMap = new TrueColorMap(colorMapResourceId);
	    _resources.add(defaultColorMap);
	    
	    _screens[0] = new Screen(rootWindowResourceId, 
	    		                 defaultColorMap,
	    		                 visual,
	    		                 32,
	    		                 1280,
	    		                 800,
	    		                 1280,
	    		                 800,
	    		                 depths); // Save unders
	    
	    _resources.add(_screens[0].getRootWindow());
	    
	    _focus = new Focus(_screens[0].getRootWindow(), Focus.RevertTo.None);
	}
	
	public Client allocateClient() {
		final int clientId = _clientIdAllocator.allocate();
		if(clientId < 0) {
			return null;
		}
		else {
			// TODO Should I really | Resource.SERVER_BIT here??
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
		_resources.free(client);
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
	
	public Resources getResources() {
		return _resources;
	}
	
	public Atoms getAtoms() {
		return _atoms;
	}
	
	public Focus getFocus() {
		return _focus;
	}

	public ByteOrder getImageByteOrder() {
		return _imageByteOrder;
	}

	public ByteOrder getBitmapBitOrder() {
		return _bitmapBitOrder;
	}

	public int getBitmapScanLineUnit() {
		return _bitmapScanLineUnit;
	}

	public int getBitmapScanLinePad() {
		return _bitmapScanLinePad;
	}
}
