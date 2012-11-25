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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import com.liaquay.tinyx.model.Screen;
import com.liaquay.tinyx.model.Server;

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
	
	public XawtScreen(final Server server, final Screen screen) {
		
		_frame = new Frame();
		_frame.setResizable(false);
		
		_frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent arg0) {
				_proxy.closed();
				_frame.dispose();
			}
		});
		
		_rootWindow = new XawtWindow(server, screen.getRootWindow());
		_frame.add(_rootWindow.getCanvas());
		_frame.pack();
		_frame.setVisible(true);
		
		screen.map();
	}
}
