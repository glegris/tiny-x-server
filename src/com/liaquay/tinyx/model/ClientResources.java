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
 * Store for resources. 
 * 
 * The identifiers used with this class contain both a client and resource ID...
 * Resources used by the server have the client bits set to 0.  
 * 
 *  Very much not thread safe!
 */
public class ClientResources {
	
	private static class ResourceKey {
		private int _resourceId;
		
		public ResourceKey(final int resourceId) {
			_resourceId = resourceId;
		}
	}
	
	private static final Comparator<ResourceKey> RESOURCE_KEY_COMPARATOR = new Comparator<ResourceKey>() {
		@Override
		public int compare(final ResourceKey arg0, final ResourceKey arg1) {
			return arg0._resourceId - arg1._resourceId;
		}		
	};
	
	private final Map<ResourceKey, Resource> _idToResourceMap = new TreeMap<ResourceKey, Resource>(RESOURCE_KEY_COMPARATOR);
	
	private ResourceKey _scratchResourceKey = new ResourceKey(0);
	
	public void add(final Resource resource){
		_idToResourceMap.put(new ResourceKey(resource.getId()), resource);
	}
	
	public Resource remove(final int resourceId) {
		_scratchResourceKey._resourceId = resourceId;
		return _idToResourceMap.remove(_scratchResourceKey);
	}
	
	public Resource get(final int resourceId) {
		_scratchResourceKey._resourceId = resourceId;
		return _idToResourceMap.get(_scratchResourceKey);
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
	
	public void free() {
		
	}
}
