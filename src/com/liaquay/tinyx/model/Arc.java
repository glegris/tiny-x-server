package com.liaquay.tinyx.model;

public class Arc {
	int x;
	int y;
	int width;
	int height;
	int angle1;
	int angle2;
	
	public Arc(int x, int y, int width, int height, int angle1, int angle2) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle1 = angle1;
		this.angle2 = angle2;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getAngle1() {
		return angle1;
	}

	public void setAngle1(int angle1) {
		this.angle1 = angle1;
	}

	public int getAngle2() {
		return angle2;
	}

	public void setAngle2(int angle2) {
		this.angle2 = angle2;
	}
	
	
}
