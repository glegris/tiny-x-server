package com.liaquay.tinyx.model;

public class Grab {
	private final boolean _ownerEvents;
	private final Window _grabWindow;
	private final boolean _pointerSynchronous;
	private final boolean _keyboardSynchronous;
	private final int _timestamp;
	private final Client _client;
	private boolean _exitWhenAllReleased = false;
	
	public Grab(
			final Client client, 
			final boolean ownerEvents,
			final Window grabWindow,
			final boolean pointerSynchronous,
			final boolean keyboardSynchronous,
			final int timestamp) {
		
		_ownerEvents = ownerEvents;
		_grabWindow = grabWindow;
		_pointerSynchronous = pointerSynchronous;
		_keyboardSynchronous = keyboardSynchronous;
		_timestamp = timestamp;
		_client = client;
	}
	
	public Client getClient() {
		return _client;
	}
	
	public int getTimestamp() {
		return _timestamp;
	}
	
	public boolean isKeyboardSynchronous() {
		return _keyboardSynchronous;
	}
	
	public boolean isPointerSynchronous() {
		return _pointerSynchronous;	
	}

	public Window getGrabWindow() {
		return _grabWindow;
	}

	public boolean isOwnerEvents() {
		return _ownerEvents;
	}
	
	public boolean getExitWhenAllReleased() {
		return _exitWhenAllReleased;
	}
	
	public void setExitWhenAllReleased() {
		_exitWhenAllReleased = true;
	}
}
