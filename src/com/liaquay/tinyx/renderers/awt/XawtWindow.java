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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.Window.Listener;

public class XawtWindow  {
	
	private static Color[] COLORS = new Color[] {
		Color.BLACK,
		Color.BLUE,
		Color.CYAN,
		Color.GREEN,
		Color.MAGENTA,
		Color.ORANGE
	};
	
	private static int _ci = 0;
	private static Color getColor() {
		_ci = (_ci + 1) % COLORS.length;
		return COLORS[_ci];
	}
	
	private Window.Listener _windowListener = new Listener() {
		
		@Override
		public void childCreated(final Window child) {
			child.setListener(_windowListener);
		}

		@Override
		public void mapped(final Window window, final boolean mapped) {
			
			paintWindow(window);
			for(int i = 0; i < window.getChildCount(); i++) {
				final Window child = window.getChild(i);
				mapped(child, mapped);
			}
		}
	};
	
    private Canvas _canvas = new Canvas();
    
    private void paintWindow(final Window window) {
    	final Graphics2D graphics = (Graphics2D)_canvas.getGraphics();
    	
    	graphics.setClip(
    			window.getClipX(), 
    			window.getClipY(),
    			window.getClipWidth(), 
    			window.getClipHeight());
    	
    	final int borderWidth = window.getBorderWidth();
    	graphics.translate(window.getAbsX()-borderWidth, window.getAbsY()-borderWidth);

    	paintBorder(window, graphics);
    	
    	final int borderWidthX2 = borderWidth + borderWidth;
    	
    	
    	graphics.setClip(
    			borderWidth, 
    			borderWidth, 
    			window.getClipWidth() - borderWidthX2, 
    			window.getClipHeight() - borderWidthX2);
    	
    	graphics.translate(borderWidth, borderWidth);
    	
    	paintContent(window, graphics);
    	
    	graphics.translate(
    			-window.getAbsX() - borderWidth, 
    			-window.getAbsY() - borderWidth);
    }
    
    private void paintBorder(final Window window, final Graphics2D graphics) {
    	graphics.setColor(getColor());
    	graphics.fillRect(
    			0, 
    			0,
    			window.getWidth() + window.getBorderWidth()+ window.getBorderWidth(), 
    			window.getHeight() + window.getBorderWidth()+ window.getBorderWidth());
    }
    
    private void paintContent(final Window window, final Graphics2D graphics) {
    	graphics.setColor(getColor());
    	graphics.fillRect(0, 0, window.getWidth(), window.getHeight());    	
    }
    
	public XawtWindow(final Server server, final Window window) {
		
		_canvas.setBounds(
				window.getX(),
				window.getY(),
				window.getWidthPixels() + window.getBorderWidth() + window.getBorderWidth(), 
				window.getHeightPixels() + window.getBorderWidth() + window.getBorderWidth());
		
		window.setListener(_windowListener);
		
		_canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				final Window evw = window.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button %d, x=%d y=%d window=%x08 ", e.getButton(), e.getX(),e.getY(),evw.getId()));
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				final Window evw = window.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button pressed %d, x=%d y=%d window=%x08 ", e.getButton(), e.getX(),e.getY(),evw.getId()));
				// TODO pass in correct screen index
				server.buttonPressed(0, e.getX(), e.getY(), e.getButton(), e.getWhen());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				final Window evw = window.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button release %d, x=%d y=%d window=%x08 ", e.getButton(), e.getX(),e.getY(),evw.getId()));
				// TODO pass in correct screen index
				server.buttonReleased(0, e.getX(), e.getY(), e.getButton(), e.getWhen());
			}
		});
		
		_canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				System.out.println("Released ");
				System.out.println("Keycode " + e.getKeyCode());
				System.out.println("Location " + e.getKeyLocation());
				System.out.println("Modifiers " + e.getModifiersEx());
				server.keyReleased(e.getKeyCode(), e.getWhen());
			}
			
			@Override
			public void keyPressed(final KeyEvent e) {
				System.out.println("Pressed ");
				System.out.println("Keycode " + e.getKeyCode());
				System.out.println("Location " + e.getKeyLocation());
				System.out.println("Modifiers " + e.getModifiersEx());
				server.keyPressed(e.getKeyCode(), e.getWhen());
			}
		});
	}
	
	public Canvas getCanvas() {
		return _canvas;
	}
}
