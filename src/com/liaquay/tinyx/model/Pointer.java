package com.liaquay.tinyx.model;

public class Pointer {
	int _x, _y;
	int _state;
	
	public Pointer() {
		_x = 0;
		_y = 0;
		_state = 0;
	}
	public int getX() {
		return _x;
	}
	public void setX(int _x) {
		this._x = _x;
	}
	public int getY() {
		return _y;
	}
	public void setY(int _y) {
		this._y = _y;
	}
	public int getState() {
		return _state;
	}
	public void setState(int _state) {
		this._state = _state;
	}
}
