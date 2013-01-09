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



public abstract class Drawable implements Resource {

	public interface Listener {
		public void copyArea(Drawable srcDrawable, Drawable d, GraphicsContext graphicsContext, int srcX,
				int srcY, int width, int height, int dstX, int dstY);
		
		public void putImage(GraphicsContext graphicsContext,
				byte[] buffer, int width, int height,
				int destinationX, int destinationY, int leftPad, int depth);
	}

	public abstract Listener getListener();

	public abstract Screen getScreen();

	public abstract Visual getVisual();

	public abstract int getDepth();

	public abstract int getX();

	public abstract int getY();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract int getBorderWidth();

//	abstract Graphics getGraphics();
	//	  abstract void restoreClip();

	//	  abstract Graphics getGraphics(GC gc, int mask);   
	//	  abstract Colormap getColormap();
//	abstract Image getImage(GraphicsContext gc, int x, int y, int width, int height);

}
