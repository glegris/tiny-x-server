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
package com.liaquay.tinyx.renderers.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.logging.Logger;

import com.liaquay.tinyx.model.Cursor;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Rectangle;
import com.liaquay.tinyx.model.Segment;
import com.liaquay.tinyx.model.Image.ImageType;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.Window.BackgroundMode;
import com.liaquay.tinyx.renderers.awt.gc.GraphicsContextComposite;

/**
 *
 */
public class XawtWindow extends XawtDrawableListener implements Window.Listener {

	private final static Logger LOGGER = Logger.getLogger(XawtWindow.class.getName());

	private final Window _window;
	private final MyJPanel _jPanel;
	private BufferedImage _image = null;


	public XawtWindow(final Window window, MyJPanel jPanel) {
		super(window);

		_window = window;
		_jPanel = jPanel;

		//TODO: Map the depth
		//	window.getDepth();
		if (window.getRootWindow().equals(window)) {
			BufferedImage image = new BufferedImage(window.getWidth() + (window.getBorderWidth() * 2), window.getHeight() + (window.getBorderWidth() * 2), BufferedImage.TYPE_INT_ARGB);
			_image = image;
		}
	}

	@Override
	public void childCreated(final Window child) {
		final XawtWindow listener = new XawtWindow(child, _jPanel);
		child.setListener(listener);
	}

	@Override
	public void setCursor(final Cursor cursor) {
		//Get the default toolkit
		final Toolkit toolkit = Toolkit.getDefaultToolkit();

		if (cursor != null) {
			final Pixmap p = cursor.getSourcePixmap();
			final Pixmap m = cursor.getMaskPixmap();

			//TODO: Create graphicsContext just to aid in the creation of the cursor image correctly.

			//			if (image != null) {
			//				final Point hotSpot = new Point(cursor.getX(),cursor.getY());
			//				final java.awt.Cursor c = toolkit.createCustomCursor(image, hotSpot, cursor.getId() + "");
			//				_canvas.setCursor(c);
			//			}
		}
	}

	@Override
	public void drawString(
			final GraphicsContext graphicsContext, 
			final String str, 
			final int x,
			final int y) {

		super.drawString(graphicsContext, str, x, y);

		updateCanvas();
	}

//	@Override
//	public void polyArc(
//			final GraphicsContext graphicsContext, 
//			final Collection<Arc> arcs,
//			final boolean fill) {
//
//		super.polyArc(graphicsContext, arcs, fill);
//
////		updateCanvas(x, y, width, height);
//	}

	@Override
	public void polyRectangle(GraphicsContext graphicsContext, Collection<Rectangle> rectangles, boolean fill) {
		super.polyRectangle(graphicsContext, rectangles, fill);

		int x[] = new int[rectangles.size() * 2];
		int xpos = 0;
		
		int y[] = new int[rectangles.size() * 2];
		int ypos = 0;
		
		for (Rectangle r : rectangles) {
			x[xpos++] = r.getX();
			x[xpos++] = r.getX() + r.getWidth();
			
			y[ypos++] = r.getY();
			y[ypos++] = r.getY() + r.getHeight();
		}

		updateCanvas(x, y);
	}


	@Override
	public void polyFill(
			final GraphicsContext graphicsContext, 
			final int xCoords[], 
			final int yCoords[]) {

		super.polyFill(graphicsContext, xCoords, yCoords);

		updateCanvas(xCoords, yCoords);
	}


	@Override
	public void drawSegments(GraphicsContext graphicsContext,
			Collection<Segment> segments) {

		super.drawSegments(graphicsContext, segments);

		updateCanvas(segments);
	}
	
	@Override
	public void polyLine(
			final GraphicsContext graphicsContext, 
			final int xCoords[], 
			final int yCoords[]) {

		super.polyLine(graphicsContext, xCoords, yCoords);

		updateCanvas(xCoords, yCoords);
	}

	/** Seems ok */
	@Override
	public void drawLine(
			final GraphicsContext graphicsContext, 
			final int x1, 
			final int y1,
			final int x2, 
			final int y2) {

		super.drawLine(graphicsContext, x1, y1, x2, y2);

		int topLeftX = Math.min(x1, x2);
		int topLeftY = Math.min(y1, y2);

		int botRightX = Math.max(x1,  x2);
		int botRightY = Math.max(y1,  y2);

		updateCanvas(topLeftX, topLeftY, Math.max(1, botRightX - topLeftX) , Math.max(1, botRightY - topLeftY));
	}


	@Override
	public Graphics2D getGraphics(GraphicsContext graphicsContext) {
		return getGraphics(graphicsContext, 0);
	}

	@Override
	public Graphics2D getGraphics() {
		final Graphics2D graphics = getBorderGraphics();
		final int borderWidth = _window.getBorderWidth();

		graphics.setClip(
				borderWidth, 
				borderWidth,
				_window.getWidthPixels(), 
				_window.getHeightPixels());

		graphics.translate(borderWidth, borderWidth);
		return graphics;
	}

	private Graphics2D getBorderGraphics() {
		final Graphics2D graphics = (Graphics2D) getImage().getGraphics();
		final int borderWidth = _window.getBorderWidth();

		graphics.setClip(
				_window.getClipX(), 
				_window.getClipY(),
				_window.getClipWidth(), 
				_window.getClipHeight());

		graphics.translate(_window.getAbsX() - borderWidth, _window.getAbsY() - borderWidth);
		return graphics;
	}

	private BufferedImage _backgroundImage = null;

	@Override
	public void setBackgroundPixmap(final Pixmap pixmap) {
		if(pixmap == null) {
			_backgroundImage = null;
		}
		else {
			final XawtPixmap awtPixmap = (XawtPixmap)pixmap.getListener();
			_backgroundImage = awtPixmap.getImage();
		}
	}

	@Override
	public void clearArea(
			final int x, 
			final int y, 
			final int width, 
			final int height) {

		final Graphics2D graphics = getGraphics();
		final Window backgroundWindow = _window.getBackgroundWindow();
		final BackgroundMode mode = backgroundWindow.getBackgroundMode();
		if(mode.equals(BackgroundMode.Pixel)) {
			final int rgb = backgroundWindow.getColorMap().getRGB(_window.getBackgroundPixel());
			graphics.setBackground(new Color(rgb));
			graphics.clearRect(x, y, width, height);
			updateCanvas(x, y, width, height);
		}
		else if(mode.equals(BackgroundMode.Pixmap)) {
			final XawtWindow awtBackgroundWindow = (XawtWindow)backgroundWindow.getWindowListener();
			final Image image = awtBackgroundWindow._backgroundImage;
			final int tileOriginX = backgroundWindow.getAbsX() - _window.getAbsX();
			final int tileOriginY = backgroundWindow.getAbsY() - _window.getAbsY();
			tileArea(tileOriginX,tileOriginY,x,y,width,height, graphics, image);
			updateCanvas(x, y, width, height);
		}
	}

	@Override
	public void copyArea(Drawable destDrawable,
			GraphicsContext graphicsContext, int srcX, int srcY, int width,
			int height, int dstX, int dstY) {

		super.copyArea(destDrawable, graphicsContext, srcX, srcY, width, height, dstX,
				dstY);

		//		if (graphicsContext.getGraphicsExposures()) {
		updateCanvas(dstX, dstY, width, height);
		//		}
	}


//	@Override
//	public void putImage(GraphicsContext graphicsContext, ImageType imageType, byte[] buffer,
//			int width, int height, int destinationX, int destinationY,
//			int leftPad, int depth) {
//
//		super.putImage(graphicsContext, imageType, buffer, width, height, destinationX,
//				destinationY, leftPad, depth);
//
////		updateCanvas(destinationX, destinationY, width, height);
//	}


	@Override
	public void copyPlane(Drawable s, GraphicsContext graphicsContext,		
			int bitplane, int srcX, int srcY,
			int width, int height, int dstX, int dstY) {
		super.copyPlane(s, graphicsContext, bitplane, srcX, srcY, width, height, dstX, dstY);

		if (graphicsContext.getGraphicsExposures()) {
			updateCanvas(dstX, dstY, width, height);
		}
	}

	@Override
	public void polyPoint(GraphicsContext graphicsContext, int[] xCoords,
			int[] yCoords) {
		super.polyPoint(graphicsContext, xCoords, yCoords);

		updateCanvas(xCoords, yCoords);
	}

	@Override
	public int getPixel(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void createImage(Drawable drawable) {
		//		BufferedImage image = new BufferedImage(drawable.getWidth(), drawable.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		//		setImage(image);
	}

	public void updateCanvas() {
		//		_canvas.getGraphics().setClip(0, 0, _window.getRootWindow().getWidth(), _window.getRootWindow().getHeight());
		if(_window.isMapped()) _jPanel.repaint();//getGraphics().drawImage(getImage(), 0, 0, _window.getRootWindow().getWidth(), _window.getRootWindow().getHeight(), null);
	}

	private void updateCanvas(int x, int y, int width, int height) {
		LOGGER.info("Update Canvas: X:" + x + " Y: " + y + " Width: " + width + " Height: " + height);
		if(_window.isMapped()) {
			_jPanel.repaint(
					_window.getAbsX() + x, 
					_window.getAbsY() + y, 
					_window.getAbsX() + x + width, 
					_window.getAbsY() + y + height);
			
		}
	}


	private void updateCanvas(int xCoords[], int yCoords[]) {
		int tlx=_window.getWidth(),tly=_window.getHeight();
		int blx=0,bly=0;

		for (int i = 0; i < xCoords.length; i++) {
			tlx = Math.min(tlx, xCoords[i]);
			tly = Math.min(tly, yCoords[i]);

			blx = Math.max(blx, xCoords[i]);
			bly = Math.max(bly, yCoords[i]);
		}

		updateCanvas(tlx, tly, (blx - tlx), (bly - tly));
	}
	
	private void updateCanvas(Collection<Segment> segments) {
		int tlx=_window.getWidth(),tly=_window.getHeight();
		int blx=0,bly=0;

		for (Segment s : segments) {
			tlx = Math.min(tlx, s.getX1());
			tly = Math.min(tly, s.getY1());

			blx = Math.max(blx, s.getX2());
			bly = Math.max(bly, s.getY2());
		}

		updateCanvas(tlx, tly, (blx - tlx), (bly - tly));
	}

	@Override
	public BufferedImage getImage() {
		if (_image != null) {
			return _image;
		} else {
			final XawtWindow awtRootWindow = (XawtWindow)_window.getRootWindow().getWindowListener();
			return awtRootWindow._image;
		}
	}

	@Override
	public void free() {
		_image = null;
	}

	private void tileArea(
			final int originX,
			final int originY,
			final int x, 
			final int y, 
			final int width, 
			final int height,
			final Graphics2D graphics,
			final Image image) {

		final int iw = image.getWidth(null);
		final int ih = image.getHeight(null);
		final int xm = x-originX % iw;
		final int ym = y-originY % ih;
		final int xs = x-(xm==0?0:iw)-xm;
		final int ys = y-(ym==0?0:ih)-ym;
		final Graphics2D g = (Graphics2D)graphics.create();
		g.setClip(x, y, width, height);
		for(int yc = ys; yc < y+height; yc+=ih) {
			for(int xc = xs; xc < x+width; xc+=iw) {
				g.drawImage(image, xc,yc,iw,ih,null);
			}
		}
	}

	@Override
	public void drawBorder() {
		final Graphics2D graphics = getBorderGraphics();
		final Window borderWindow = _window.getBorderWindow();
		final int borderWidth = _window.getBorderWidth();
		final int borderWidthX2 = borderWidth + borderWidth;
		final int w = _window.getWidth() + borderWidthX2;
		final int h = _window.getHeight() + borderWidthX2;
		drawBorder(graphics, 0,0,w,borderWidth,borderWindow);
		drawBorder(graphics, 0,borderWidth,borderWidth,_window.getHeight(),borderWindow);
		drawBorder(graphics, w-borderWidth,borderWidth,borderWidth,_window.getHeight(),borderWindow);
		drawBorder(graphics, 0,h-borderWidth,w,borderWidth,borderWindow);
	}

	private BufferedImage _borderImage = null;

	@Override
	public void setBorderPixmap(final Pixmap pixmap) {
		if(pixmap == null) {
			_borderImage = null;
		}
		else {
			final XawtPixmap awtPixmap = (XawtPixmap)pixmap.getListener();
			_borderImage = awtPixmap.getImage();
		}
	}

	private void drawBorder(
			final Graphics2D graphics,
			final int x, 
			final int y, 
			final int width, 
			final int height,
			final Window borderWindow) {

		final BackgroundMode mode = borderWindow.getBorderMode();
		if(mode.equals(BackgroundMode.Pixel)) {
			final int rgb = borderWindow.getColorMap().getRGB(_window.getBorderPixel());
			graphics.setBackground(new Color(rgb));
			graphics.clearRect(x, y, width, height);
			updateCanvas(x, y, width, height);
		}
		else if(mode.equals(BackgroundMode.Pixmap)) {
			final XawtWindow awtBorderWindow = (XawtWindow)borderWindow.getWindowListener();
			final Image image = awtBorderWindow._borderImage;
			final int tileOriginX = borderWindow.getAbsX() - borderWindow.getBorderWidth() - _window.getAbsX() + _window.getBorderWidth();
			final int tileOriginY = borderWindow.getAbsY() - borderWindow.getBorderWidth() - _window.getAbsY() + _window.getBorderWidth();
			tileArea(tileOriginX, tileOriginY,x,y,width,height, graphics, image);
			updateCanvas(x-_window.getBorderWidth(), y-_window.getBorderWidth(), width, height);
		}
	}
}
