package com.liaquay.tinyx.model;

public final class Atom implements Comparable<Atom>{
	
	private final int _id;
	private final String _text;
	
	public Atom(final int id, final String text) {
		_text = text;
		_id = id;
	}

	@Override
	public final int compareTo(final Atom arg0) {
		return _id - arg0._id;
	}
	
	public final String getText(){
		return _text;
	}
	
	public final int getId() {
		return _id;
	}
}
