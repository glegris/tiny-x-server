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

import java.io.IOException;

import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Event;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.eventfactories.MapRequestFactory;

public class MapRequestFactoryImpl implements MapRequestFactory {

	public static MapRequestFactory FACTORY = new MapRequestFactoryImpl();
	
	@Override
	public Event create(
			final boolean sendEvent, 
			final Window parent,
			final Window window) {

		return new EventImpl(Event.MapRequest, 0) {
			@Override
			public final void writeBody(final XOutputStream outputStream, final Client client, final Window window) throws IOException {
				outputStream.writeInt(parent == null ? 0 : parent.getId());
				outputStream.writeInt(window.getId());
			}
		};
	}
}
