package com.liaquay.tinyx.model;

public class Focus {

	public enum RevertTo {
		None,
		PointerRoot,
		Parent;
		
		public static RevertTo getFromIndex(final int index) {
			final RevertTo[] revertTo = values();
			if (index<revertTo.length && index>=0) return revertTo[index];
			return null;
		}
	}
	
	public enum Mode {
		None,
		PointerRoot,
		Window;
	}
	
	private Window _window;
	private RevertTo _revertTo;
	private Mode _mode = Mode.None;
	private int _timestamp = 0;
	
	public Focus(final Window window, final RevertTo revertTo) {
		_window = window;
		_revertTo = revertTo;
	}
	
	public Window getWindow() {
		return _window;
	}
	
	public Mode getMode() {
		return _mode;
	}
	
	public RevertTo getRevertTo() {
		return _revertTo;
	}
	
	public int getTimestamp() {
		return _timestamp;
	}
	
	public void set(final Mode mode, final Window window, final RevertTo revertTo, final int timestamp) {
		if(timestamp > _timestamp) {
			
			// TODO Issue focus events
			
			_window = window;
			_revertTo = revertTo;
			_mode = mode;
		}
	}
}
