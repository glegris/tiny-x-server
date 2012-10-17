package com.liaquay.tinyx.model;

public abstract class AbstractResource implements Resource {

	private final int _id;
	
	public AbstractResource(final int id) {
		_id = id;
	}
	
	@Override
	public int getId() {
		return _id;
	}
}
