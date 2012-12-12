/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.model;

public class Cursor extends AbstractResource {

	int _x;
	int _y;

	Pixmap _sourcePixmapRes;
	Pixmap _maskPixmapRes;
	
	int foregroundColorRed;
	int foregroundColorGreen;
	int foregroundColorBlue;
	

	int backgroundColorRed;
	int backgroundColorGreen;
	int backgroundColorBlue;

	
	public Cursor(final int id) {
		super(id);
	}

	public void setX(int x) {
		this._x = x;
	}
	
	public int getX() {
		return this._x;
	}

	public void setY(int y) {
		this._y = y;
	}

	public int getY() {
		return this._y;
	}

	public void setSourcePixmap(Pixmap sourcePixmapRes) {
		_sourcePixmapRes = sourcePixmapRes;
	}

	public void setMaskPixmap(Pixmap maskPixmapRes) {
		_maskPixmapRes = maskPixmapRes;
	}

	public Pixmap getSourcePixmap() {
		return _sourcePixmapRes;
	}
	
	public Pixmap getMaskPixmap() {
		return _maskPixmapRes;
	}

	public int getForegroundColorRed() {
		return foregroundColorRed;
	}

	public void setForegroundColorRed(int foregroundColorRed) {
		this.foregroundColorRed = foregroundColorRed;
	}

	public int getForegroundColorGreen() {
		return foregroundColorGreen;
	}

	public void setForegroundColorGreen(int foregroundColorGreen) {
		this.foregroundColorGreen = foregroundColorGreen;
	}

	public int getForegroundColorBlue() {
		return foregroundColorBlue;
	}

	public void setForegroundColorBlue(int foregroundColorBlue) {
		this.foregroundColorBlue = foregroundColorBlue;
	}

	public int getBackgroundColorRed() {
		return backgroundColorRed;
	}

	public void setBackgroundColorRed(int backgroundColorRed) {
		this.backgroundColorRed = backgroundColorRed;
	}

	public int getBackgroundColorGreen() {
		return backgroundColorGreen;
	}

	public void setBackgroundColorGreen(int backgroundColorGreen) {
		this.backgroundColorGreen = backgroundColorGreen;
	}

	public int getBackgroundColorBlue() {
		return backgroundColorBlue;
	}

	public void setBackgroundColorBlue(int backgroundColorBlue) {
		this.backgroundColorBlue = backgroundColorBlue;
	}

	
}
