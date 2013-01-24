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
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

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

	private final Frame _frame = new Frame();
	private final XawtWindow _rootWindowListener;
	private final Canvas _canvas = new MyCanvas();

	public XawtScreen(final TinyXAwt server, final Screen screen) {


		
		_frame.setResizable(true);

		_frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent arg0) {
				_proxy.closed();
				_frame.dispose();
			}
		});

		final Window rootWindow = screen.getRootWindow();

		_canvas.setBounds(
				rootWindow.getX(),
				rootWindow.getY(),
				rootWindow.getWidthPixels() + rootWindow.getBorderWidth() + rootWindow.getBorderWidth(), 
				rootWindow.getHeightPixels() + rootWindow.getBorderWidth() + rootWindow.getBorderWidth());

		_canvas.getAccessibleContext();

		_frame.add(_canvas);
		_frame.pack();
		_frame.setVisible(true);

		_rootWindowListener = new XawtWindow(rootWindow, _canvas);
		rootWindow.setListener(_rootWindowListener);

		// WTF If I don't do this the background is rendered grey!?!
		// TODO Nathan any ideas? I guess there is a delayed AWT event but my all attempts to prevent the redraw have failed?
		try { Thread.sleep(1000); } catch(final Exception e){}
		screen.map();

		_canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				final Window evw = rootWindow.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button %d, x=%d y=%d", e.getButton(), e.getX(),e.getY()));
				if (evw != null) {
					System.out.println(String.format("window=%x08", evw.getId()));
				}
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
				final Window evw = rootWindow.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button pressed %d, x=%d y=%d", e.getButton(), e.getX(),e.getY()));
				if (evw != null) {
					System.out.println(String.format("window=%x08", evw.getId()));
				}

				// TODO pass in correct screen index
				server.getServer().buttonPressed(0, e.getX(), e.getY(), e.getButton(), (int)(e.getWhen()&0xffffffff));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				final Window evw = rootWindow.windowAt(e.getX(), e.getY());
				System.out.println(String.format("Button release %d, x=%d y=%d",  e.getButton(), e.getX(),e.getY()));
				if (evw != null) {
					System.out.println(String.format("window=%x08", evw.getId()));
				}
				// TODO pass in correct screen index
				server.getServer().buttonReleased(0, e.getX(), e.getY(), e.getButton(), (int)(e.getWhen()&0xffffffff));
			}
		});

		_canvas.addKeyListener(new KeyAdapter() {
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
				System.out.println("KupdateCanvaseycode " + e.getKeyCode());
				System.out.println("Location " + e.getKeyLocation());
				System.out.println("Modifiers " + e.getModifiersEx());
				server.getServer().keyPressed(e.getKeyCode(), (int)(e.getWhen()&0xffffffff));
			}
		});
	}
}



