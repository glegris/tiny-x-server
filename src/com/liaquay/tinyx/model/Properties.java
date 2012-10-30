package com.liaquay.tinyx.model;

import com.liaquay.tinyx.util.IntMap;

// Not sure this should inherit! 
public class Properties extends IntMap<Property> {


	public void add(final Property property) {
		put(property.getPropertyAtom().getId(), property);
	}

}
