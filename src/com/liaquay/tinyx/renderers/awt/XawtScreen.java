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

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import com.liaquay.tinyx.model.Screen;
import com.liaquay.tinyx.model.Window;

public class XawtScreen {

	public interface Listener {
		public void closed();
	}

	private Set<Listener> _listeners = new HashSet<Listener>(1);

	private Listener _proxy = new Listener() {
		@Override
		public void closed() {
			for(final Listener listener : _listeners) {
				listener.closed();
			}
		}
	};

	public Set<Listener> getListeners() {
		return _listeners;
	}

	private final JFrame _frame;
	private final XawtWindow _rootWindowListener;
	private final MyJPanel _jPanel;

	public XawtScreen(final TinyXAwt server, final Screen screen) {

		_jPanel = new MyJPanel();
		
		final Window rootWindow = screen.getRootWindow();


		_jPanel.setRootWindow(rootWindow);
		_jPanel.setBounds(
				rootWindow.getX(),
				rootWindow.getY(),
				rootWindow.getWidthPixels() + rootWindow.getBorderWidth() + rootWindow.getBorderWidth(), 
				rootWindow.getHeightPixels() + rootWindow.getBorderWidth() + rootWindow.getBorderWidth());

		_jPanel.getAccessibleContext();
		
//		_canvas.createBufferStrategy(2);

		_frame = new JFrame();
		_frame.setPreferredSize(new Dimension(1024, 800));

		_rootWindowListener = new XawtWindow(rootWindow, _jPanel);
		rootWindow.setListener(_rootWindowListener);
		
		_frame.add(_jPanel);
		_frame.pack();
		_frame.setResizable(true);
		_frame.setVisible(true);
		_frame.setIgnoreRepaint(true);
		
		_frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent arg0) {
				_proxy.closed();
				_frame.dispose();
			}
		});
		
		screen.map();

		_jPanel.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(final MouseEvent e) {
				System.out.println(String.format("Moved x=%d y=%d", e.getX(),e.getY()));

				// TODO pass in correct screen index
				server.getServer().pointerMoved(0, e.getX(), e.getY(), (int)(e.getWhen()&0xffffffff));
			}
			
			@Override
			public void mouseDragged(final MouseEvent e) {
				System.out.println(String.format("Moved x=%d y=%d", e.getX(),e.getY()));
				
				// TODO pass in correct screen index
				server.getServer().pointerMoved(0, e.getX(), e.getY(), (int)(e.getWhen()&0xffffffff));
			}
		});
		
		_jPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(final MouseEvent e) {
			}

			@Override
			public void mouseEntered(final MouseEvent arg0) {
			}

			@Override
			public void mouseExited(final MouseEvent arg0) {
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				System.out.println(String.format("Button pressed %d, x=%d y=%d", e.getButton(), e.getX(),e.getY()));

				// TODO pass in correct screen index
				server.getServer().buttonPressed(0, e.getX(), e.getY(), e.getButton(), (int)(e.getWhen()&0xffffffff));
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				System.out.println(String.format("Button release %d, x=%d y=%d",  e.getButton(), e.getX(),e.getY()));

				// TODO pass in correct screen index
				server.getServer().buttonReleased(0, e.getX(), e.getY(), e.getButton(), (int)(e.getWhen()&0xffffffff));
			}
		});

		_jPanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				System.out.println("Released ");
				System.out.println("Keycode " + e.getKeyCode());
				System.out.println("Location " + e.getKeyLocation());
				System.out.println("Modifiers " + e.getModifiersEx());
				server.getServer().keyReleased(e.getKeyCode(), (int)(e.getWhen()&0xffffffff));
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				System.out.println("Pressed ");
				System.out.println("Keycode " + e.getKeyCode());
				System.out.println("Location " + e.getKeyLocation());
				System.out.println("Modifiers " + e.getModifiersEx());
				server.getServer().keyPressed(e.getKeyCode(), (int)(e.getWhen()&0xffffffff));
			}
		});
	_jPanel.grabFocus();
	}
}



