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
package com.liaquay.tinyx.events;

import com.liaquay.tinyx.model.Event;
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.eventfactories.KeyFactory;

public class KeyPressFactoryImpl extends KeyFactoryImpl implements KeyFactory {
	
	public static KeyFactory FACTORY = new KeyPressFactoryImpl();
	
	public Event create(
			final Window focusWindow,
			final Window child,
			final Pointer pointer,
			final int key,
			final int when) {
		
		return create(
				Event.KeyPress,
				focusWindow,
				child,
				pointer,
				key,
				when);
	}
}
