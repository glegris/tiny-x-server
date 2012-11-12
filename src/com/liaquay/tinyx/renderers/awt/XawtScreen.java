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

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.Set;

import com.liaquay.tinyx.model.Screen;

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
	
	private final Frame _frame;
	private final XawtWindow _rootWindow;
	
	public XawtScreen(final Screen screen) {
		
		_frame = new Frame();
		
		_frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(final WindowEvent arg0) {}
			
			@Override
			public void windowIconified(final WindowEvent arg0) {}
			
			@Override
			public void windowDeiconified(final WindowEvent arg0) {
			}
			
			@Override
			public void windowDeactivated(final WindowEvent arg0) {
			}
			
			@Override
			public void windowClosing(final WindowEvent arg0) {
				_proxy.closed();
				_frame.dispose();
			}
			
			@Override
			public void windowClosed(final WindowEvent arg0) {
			}

			@Override
			public void windowActivated(final WindowEvent e) {
			}
		});
		
		_rootWindow = new XawtWindow(screen.getRootWindow());
		_frame.add(_rootWindow.getBorder());
		_frame.pack();
		_frame.setVisible(true);
	}
}
