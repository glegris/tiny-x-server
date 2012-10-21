package com.liaquay.tinyx.model;

public class Focus {

	public enum RevertTo {
		None,
		PointerRoot,
		Parent
	}
	
	private Window _window;
	private RevertTo _revertTo;
	private int _timestamp = 0;
	
	public Focus(final Window window, final RevertTo revertTo) {
		_window = window;
		_revertTo = revertTo;
	}
	
	public Window getWindow() {
		return _window;
	}
	
	public RevertTo getRevertTo() {
		return _revertTo;
	}
	
	public int getTimestamp() {
		return _timestamp;
	}
	
	public void set(final Window window, final RevertTo revertTo, final int timestamp) {
		if(timestamp > _timestamp) {
			
			// TODO Issue focus events
			
			_window = window;
			_revertTo = revertTo;
		}
	}
}
