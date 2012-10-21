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
import java.util.Map;

import com.liaquay.tinyx.util.IntMap;
import com.liaquay.tinyx.util.IntMap.Key;

/**
 * Store for resources. 
 * 
 * The identifiers used with this class contain both a client and resource ID...
 * Resources used by the server have the client bits set to 0.  
 * 
 *  Very much not thread safe!
 */
public class Resources {
	
	private IntMap<Resource> _idToResourceMap = new IntMap<Resource>();
	
	public void add(final Resource resource){
		_idToResourceMap.put(resource.getId(), resource);
	}
	
	public Resource remove(final int resourceId) {
		return _idToResourceMap.remove(resourceId);
	}
	
	public Resource get(final int resourceId) {
		return _idToResourceMap.get(resourceId);
	}
	
	/**
	 * Get a resource specified by the resourceId, but only
	 * if assignable to the supplied class.
	 * 
	 * @param resourceId A resource identifier
	 * @param clazz result will be assignable to this class (or null)
	 * @return a resource
	 */
	@SuppressWarnings("unchecked")
	public <T extends Resource> T get(final int resourceId, final Class<T> clazz) {
		final Resource resource = get(resourceId);
		if(resource == null) return null;
		if(!clazz.isAssignableFrom(resource.getClass())) return null;
		return (T)resource;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Resource> T remove(final int resourceId, final Class<T> clazz) {
		final Resource resource = remove(resourceId);
		if(resource == null) return null;
		if(!clazz.isAssignableFrom(resource.getClass())) return null;
		return (T)resource;
	}
	
	public void free() {
		for(final Map.Entry<Key, Resource> entry : _idToResourceMap.entrySet()) {
			entry.getValue().free();
		}
	}

	public void free(final Client client) {
		final int maskedClientId = client.getClientId() << Resource.CLIENTOFFSET; 
		final List<Key> keysToRemove = new ArrayList<Key>();
		for(final Map.Entry<Key, Resource> entry : _idToResourceMap.entrySet()) {
			if((entry.getKey().getValue() & Resource.CLIENT_ID_MASK) == maskedClientId) {
				keysToRemove.add(entry.getKey());
				entry.getValue().free();
			}
		}
		for(final Key resourceKey : keysToRemove) {
			_idToResourceMap.remove(resourceKey);
		}
	}
}
