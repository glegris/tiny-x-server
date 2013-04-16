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

import java.util.Collection;

import com.liaquay.tinyx.model.Image.ImageType;


public abstract class Drawable implements Resource {

	public interface Listener {
		public void drawString(GraphicsContext graphicsContext, String str, int x, int y, int bx, int by, int bw, int bh);
		public void drawString(GraphicsContext graphicsContext, String str, int x, int y);
		public void copyArea(Drawable d, GraphicsContext graphicsContext, int srcX, int srcY, int width, int height, int dstX, int dstY);
		public void putImage(GraphicsContext graphicsContext, ImageType imageType, byte[] buffer, int width, int height,int destinationX, int destinationY, int leftPad, int depth);
		public void createImage(Drawable drawable);
		public void copyPlane(Drawable s, GraphicsContext graphicsContext, int bitplane, int srcX, int srcY,int width, int height, int dstX, int dstY);
		public void polyRectangle(GraphicsContext graphicsContext, Collection<Rectangle> rectangles, boolean b);
		public void polyFill(GraphicsContext graphicsContext, int x[], int y[]);
		public void polyLine(GraphicsContext graphicsContext, int[] xCoords, int[] yCoords);
		public void drawLine(GraphicsContext graphicsContext, int[] xCoords1, int[] yCoords1, int[] xCoords2, int[] yCoords2);
		public void drawLine(GraphicsContext graphicsContext, int x1, int y1, int x2, int y2);
		public void polyPoint(GraphicsContext graphicsContext, int[] xCoords, int[] yCoords);
		public byte[] getImageData(int x, int y, int width, int height, ImageType imageType, int planeMask);
		public void polyArc(GraphicsContext graphicsContext, Collection<Arc> arcs, boolean fill);
		public void free();
		public void drawSegments(GraphicsContext graphicsContext,
				Collection<Segment> segments);
	}
	
	protected static class NullListener implements Listener {
		@Override
		public void copyArea(Drawable s, GraphicsContext graphicsContext, int srcX, int srcY, int width, int height, int dstX, int dstY) {}
		@Override
		public void putImage(GraphicsContext graphicsContext, ImageType imageType, byte[] buffer, int width, int height, int destinationX, int destinationY, int leftPad, int depth) {}
		@Override
		public void drawString(GraphicsContext graphicsContext, String str, int x, int y, int bx, int by, int bw, int bh) {}
		@Override
		public void createImage(Drawable drawable) {}
		@Override
		public void copyPlane(Drawable s, GraphicsContext graphicsContext, int bitplane, int srcX, int srcY, int width, int height, int dstX, int dstY) {}
		@Override
		public void polyRectangle(GraphicsContext graphicsContext, Collection<Rectangle> rectangles, boolean b) {}
		@Override
		public void polyLine(GraphicsContext graphicsContext, int[] xCoords, int[] yCoords) {}
		@Override
		public void polyPoint(GraphicsContext graphicsContext, int[] xCoords, int[] yCoords) {}
		@Override
		public byte[] getImageData(int x, int y, int width, int height, ImageType imageType, int planeMask) { return null; }
		@Override
		public void drawLine(GraphicsContext graphicsContext, int[] xCoords1, int[] yCoords1, int[] xCoords2, int[] yCoords2) {}
		@Override
		public void drawLine(GraphicsContext graphicsContext, int x1, int y1, int x2, int y2) {}
		@Override
		public void drawString(GraphicsContext graphicsContext, String str, int x, int y) {}
		@Override
		public void polyFill(GraphicsContext graphicsContext, int[] x, int[] y) {}
		@Override
		public void free() {}
		@Override
		public void polyArc(GraphicsContext graphicsContext, Collection<Arc> arcs, boolean fill) {}
		@Override
		public void drawSegments(GraphicsContext graphicsContext,
				Collection<Segment> segments) {
			// TODO Auto-generated method stub
			
		}
	}

	public abstract Listener getDrawableListener();

	public abstract Screen getScreen();

	public abstract Visual getVisual();

	public abstract int getDepth();

	public abstract int getX();

	public abstract int getY();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract int getBorderWidth();

	public abstract ColorMap getColorMap();
	
	public void drawString(
			final GraphicsContext graphicsContext, 
			final String str, 
			final int x, 
			final int y, 
			final int bx, 
			final int by, 
			final int bw, 
			final int bh){
		
		final Font font = graphicsContext.getFont();
		final FontDetail fontDetail = font.getFontDetail();
		fontDetail.drawString(this, str, x, y, graphicsContext.getForegroundColour(), bx, by, bw, bh, graphicsContext.getBackgroundColour());
	}
	
	public void polyArc(
			final GraphicsContext graphicsContext, 
			final Collection<Arc> arcs,
			final boolean fill) {
		
		getDrawableListener().polyArc(graphicsContext, arcs, fill);
	}
	
	public void drawLine(
			final GraphicsContext graphicsContext, 
			final int x1, 
			final int y1,
			final int x2,
			final int y2) {
		
		getDrawableListener().drawLine(graphicsContext, x1, y1, x2, y2);
	}
	
	public void putImage(
			final GraphicsContext graphicsContext, 
			final ImageType imageType,
			final byte[] data,
			final int width, 
			final int height, 
			final int destinationX, 
			final int destinationY,
			final int leftPad, 
			final int depth) {

		getDrawableListener().putImage(graphicsContext, imageType, data, width, height, destinationX, destinationY, leftPad, depth);
	}	
	
	public void drawString(
			final GraphicsContext graphicsContext, 
			final String str, 
			final int x,
			final int y) {

		final Font font = graphicsContext.getFont();
		final FontDetail fontDetail = font.getFontDetail();
		fontDetail.drawString(this, str, x, y, graphicsContext.getForegroundColour());
	}
	
	public void polyRectangle(
			final GraphicsContext graphicsContext, 
			final Collection<Rectangle> rectangles, 
			final boolean fill) {
		
		getDrawableListener().polyRectangle(graphicsContext, rectangles, fill);
	}

	public void polyLine(
			final GraphicsContext graphicsContext,
			final int x[], 
			final int y[]) {
		
		getDrawableListener().polyLine(graphicsContext, x, y);
	}

	public void copyArea(
			final Drawable source,
			final GraphicsContext graphicsContext, 
			final int srcX,
			final int srcY,
			final int width, 
			final int height, 
			final int dstX, 
			final int dstY) {
		
		getDrawableListener().copyArea(source, graphicsContext, srcX, srcY, width, height, dstX, dstY);
	}

	public void polyPoint(
			final GraphicsContext graphicsContext, 
			final int[] xCoords,
			final int[] yCoords) {
		
		getDrawableListener().polyPoint(graphicsContext, xCoords, yCoords);
	}

	public void copyPlane(
			final Drawable s, 
			final GraphicsContext graphicsContext,
			final int bitplane,
			final int srcX, 
			final int srcY, 
			final int width, 
			final int height, 
			final int dstX,
			final int dstY) {
		
		getDrawableListener().copyPlane(s, graphicsContext, bitplane, srcX, srcY, width, height, dstX, dstY);
	}

	public void polyFill(
			final GraphicsContext graphicsContext, 
			final int[] xCoords,
			final int[] yCoords) {
		
		getDrawableListener().polyFill(graphicsContext, xCoords, yCoords);
	}

	public void drawSegments(GraphicsContext graphicsContext,
			Collection<Segment> segments) {
		
		getDrawableListener().drawSegments(graphicsContext, segments);
	}

}
