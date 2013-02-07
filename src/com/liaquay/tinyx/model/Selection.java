package com.liaquay.tinyx.model;

public class Selection {
	private Window _window;
	private int _timestamp;
	
	public Window getWindow() {
		return _window;
	}

	public int getTimestamp() {
		return _timestamp;
	}
	
	public Selection(
			final Window window,
			final int timestamp){
		
		_window = window;
		_timestamp = timestamp;
	}
	
	public void update(
			final Window window,
			final int timestamp){
		
		_window = window;
		_timestamp = timestamp;
	}

}
