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
	
	public void free() {
		
	}
}
