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

public class Pixmap extends Drawable {


	public interface Listener extends Drawable.Listener {
	}

	private Listener _listener = null;

	public void setListener(final Listener listener) {
		_listener = listener;
	}

	public Listener getListener() {
		return _listener;
	}

	private final int _resourceId;
	private final Drawable _drawable;
	private final int _depth;
	private final int _width;
	private final int _height;

	public Pixmap(
			final int resourceId,
			final Drawable drawable,
			final int depth,
			final int width,
			final int height) {
		_depth = depth;
		_width = width;
		_height = height;
		_drawable = drawable;
		_resourceId = resourceId;
	}

	@Override
	public int getId() {
		return _resourceId;
	}

	@Override
	public void free() {
		_listener.free();
	}

	@Override
	public Screen getScreen() {
		// TODO Not sure this is correct
		return _drawable.getScreen();
	}

	@Override
	public Visual getVisual() {
		// TODO Not sure this is correct
		return _drawable.getVisual();
	}

	public int getDepth() {
		return _depth;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {		
		return 0;
	}

	@Override
	public int getWidth() {
		return _width;
	}

	@Override
	public int getHeight() {
		return _height;
	}

	@Override
	public int getBorderWidth() {
		return 0;
	}

	@Override
	public Listener getDrawableListener() {
		return _listener;
	}

	@Override
	public ColorMap getColorMap() {
		return _drawable.getColorMap();
	}

	@Override
	public int getAbsX() {
		return 0;
	}

	@Override
	public int getAbsY() {
		return 0;
	}

	public void fill(int _foregroundColour) {
		// TODO Auto-generated method stub

	}
}
