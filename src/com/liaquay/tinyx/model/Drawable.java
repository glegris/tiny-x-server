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

import java.awt.Image;
import java.awt.image.BufferedImage;


public abstract class Drawable implements Resource {

	public interface Listener {
		public void drawString(GraphicsContext graphicsContext, String str, int x, int y, int bx, int by, int bw, int bh);

		public void copyArea(Drawable d, GraphicsContext graphicsContext, int srcX,
				int srcY, int width, int height, int dstX, int dstY);
		
		public void putImage(GraphicsContext graphicsContext,
				byte[] buffer, int width, int height,
				int destinationX, int destinationY, int leftPad, int depth);

		void createImage(Drawable drawable);

		BufferedImage getImage();

		public void copyPlane(Drawable s, int bitplane, int srcX, int srcY,
				int width, int height, int dstX, int dstY);
	}
	
	protected static class NullListener implements Listener {

		@Override
		public void copyArea(Drawable d, GraphicsContext graphicsContext,
				int srcX, int srcY, int width, int height, int dstX, int dstY) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void putImage(GraphicsContext graphicsContext, byte[] buffer,
				int width, int height, int destinationX, int destinationY,
				int leftPad, int depth) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawString(GraphicsContext graphicsContext, String str,
				int x, int y, int bx, int by, int bw, int bh) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void createImage(Drawable drawable) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public BufferedImage getImage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void copyPlane(Drawable s, int bitplane, int srcX, int srcY,
				int width, int height, int dstX, int dstY) {
			// TODO Auto-generated method stub
			
		}
	}


	public abstract com.liaquay.tinyx.model.Drawable.Listener getDrawableListener();

	public abstract Screen getScreen();

	public abstract Visual getVisual();

	public abstract int getDepth();

	public abstract int getX();

	public abstract int getY();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract int getBorderWidth();

	public abstract ColorMap getColorMap();
//	abstract Graphics getGraphics();
	//	  abstract void restoreClip();

	//	  abstract Graphics getGraphics(GC gc, int mask);   
	//	  abstract Colormap getColormap();
//	abstract Image getImage(GraphicsContext gc, int x, int y, int width, int height);

	
	
	public void drawString(
			final GraphicsContext graphicsContext, 
			final String str, 
			final int x, 
			final int y, 
			final int bx, 
			final int by, 
			final int bw, 
			final int bh){
		getDrawableListener().drawString(graphicsContext, str, x, y, bx, by, bw, bh);
	}
}
