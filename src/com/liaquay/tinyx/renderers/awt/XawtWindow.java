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
import java.awt.Panel;

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
	
    private Panel _border = new Panel(null);
    private Panel _content = new Panel(null);
    
	public XawtWindow(final Window window) {
		xawtMapped(window.isMapped());
		
		_border.setBackground(getColor());
		_content.setBackground(getColor());
		
		_border.add(_content);
		
		_border.setBounds(
				window.getX(),
				window.getY(),
				window.getWidthPixels(), 
				window.getHeightPixels());
		
		_content.setBounds(
				window.getBorderWidth(),
				window.getBorderWidth(),
				window.getWidthPixels() - (window.getBorderWidth()<<1),
				window.getHeightPixels() - (window.getBorderWidth()<<1));
		
		window.setListener(new Listener() {
			
			@Override
			public void childCreated(final Window parent, final Window child) {
				
				final XawtWindow childXawtWindow = new XawtWindow(child);
				
				_content.add(childXawtWindow._border);
			}

			@Override
			public void mapped(final boolean mapped) {
				xawtMapped(mapped);
			}
		});
	}
	
	public Panel getBorder() {
		return _border;
	}
	
	public void xawtMapped(final boolean mapped) {
		_border.setVisible(mapped);
		_content.setVisible(mapped);
	}
}
