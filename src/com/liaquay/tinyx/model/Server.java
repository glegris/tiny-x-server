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

import com.liaquay.tinyx.io.ByteOrder;
import com.liaquay.tinyx.model.eventfactories.EventFactories;
import com.liaquay.tinyx.model.eventfactories.MappingNotifyFactory;
import com.liaquay.tinyx.model.font.FontFactory;

/**
 * 
 */
public class Server extends Client {
	
	private static final byte[] VENDOR = "Liaquay".getBytes();

	private static Format[] FORMATS = new Format[] {
		new Format (32, 24, 8)
	};
	
	private final Extensions _extensions = new Extensions();
	private final Clients _clients = new Clients();
	private final Keyboard _keyboard;
	private final List<Screen> _screens = new ArrayList<Screen>(2);
	private final Resources _resources = new Resources();
	private final Atoms _atoms = new Atoms();
	private final ByteOrder _imageByteOrder = ByteOrder.LSB;
	private final ByteOrder _bitmapBitOrder = ByteOrder.MSB;
	private final int _bitmapScanLineUnit = 8; 
	private final int _bitmapScanLinePad = 8;
	
	private final boolean _handlesBigRequests = true;
	private final int _maximumRequestLength = 32000;
	
	private Focus _focus = null;
	
	// Used for generating server side resources such as the Root Window, ColorMap, etc.
	private final int _endServerResourceId;
	private int _serverResourceId;
	private final EventFactories _eventFactories;
	private final FontFactory _fontFactory;
	
	public Server(final EventFactories eventFactories, final Keyboard keyboard, final FontFactory fontFactory) {		
		// Create the server as a client with ID of 0
		super(	0, 
				new PostBox() {
					@Override
					public void send(final Event event) {
						// Do nothing. No messages should be sent to the server anyhow.
					}
				});
		
		_eventFactories = eventFactories;
		_fontFactory = fontFactory;
		_keyboard = keyboard;
		
		keyboard.setListener(new Keyboard.Listener() {
			@Override
			public void mappingNotify(final int firstKeyCode, final int count) {
				_clients.send(_eventFactories.getMappingNotifyFactory().create(MappingNotifyFactory.Request.Keyboard, firstKeyCode, count));
			}
		});
	    
		_serverResourceId = (getClientId() << Resource.CLIENTOFFSET) | Resource.SERVER_BIT;
	    _endServerResourceId = (_serverResourceId | Resource.RESOURCE_ID_MASK)+1;
	    
	    // TODO not sure we should do this!
	    // TODO How accessible should the server client be?
	    //_clients.add(this);
	}
	
	public Extensions getExtensions() {
		return _extensions;
	}
	
	public EventFactories getEventFactories() {
		return _eventFactories;
	}
	
	public FontFactory getFontFactory() {
		return _fontFactory;
	}
	
	private int allocateResourceId(){
		final int id =_serverResourceId++;
		if (id !=_endServerResourceId){
			return id;
		}
		throw new RuntimeException("Error allocating fake ID");
	}	
	
	public interface ResourceFactory<T> {
		public T create(final int resourceId);
	}
	
	public ColorMap createColorMap(final ResourceFactory<ColorMap> factory) {
		final int resourceId = allocateResourceId();
		final ColorMap colorMap = factory.create(resourceId);
		_resources.add(colorMap);
		return colorMap;
	}
	
	public Visual createVisual(final ResourceFactory<Visual> factory) {
		final int resourceId = allocateResourceId();
		final Visual visual = factory.create(resourceId);
		_resources.add(visual);
		return visual;
	}
	
	public Screen addScreen(final ResourceFactory<Screen> factory) {
		final int resourceId = allocateResourceId();
		final Screen screen = factory.create(resourceId);
		// TODO not very graceful
		if(_screens.size() == 0) {
		    _focus = new Focus(screen, Focus.RevertTo.None);
		}
		
		_screens.add(screen);
		_resources.add(screen);
		
		return screen;
	}
	
	public Client allocateClient(final PostBox postBox) {
		return _clients.allocate(postBox);
	}
	
	public void freeClient(final Client client) {
		final int clientId = client.getClientId();
		if(clientId == 0) {
			throw new RuntimeException("Don't try to free the server silly");
		}
		_clients.free(client);
		_resources.free(client);
	}
	
	public byte[] getVendor() {
		return VENDOR;
	}
	
	public Format[] getFormats() {
		return FORMATS;
	}
	
	public List<Screen> getScreens() {
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
	
	public boolean getHandlesBigRequests() {
		return _handlesBigRequests;
	}

	public int getMaximumRequestLength() {
		return _maximumRequestLength;
	}
	
	
	// TODO I need to be called
	public void free() {
		_clients.freeAllClients();
		// TODO free all resources
	}

}
