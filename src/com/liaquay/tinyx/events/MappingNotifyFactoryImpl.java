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
import com.liaquay.tinyx.model.eventfactories.MappingNotifyFactory;

public class MappingNotifyFactoryImpl implements MappingNotifyFactory {

	public static MappingNotifyFactory FACTORY = new MappingNotifyFactoryImpl();

	@Override
	public Event create(
			final Request request, 
			final int firstKeyCode, 
			final int count) {
		
		return new EventImpl(Event.MappingNotify, 0) {
			@Override
			public void writeBody(final XOutputStream outputStream, final Client client, final Window window) throws IOException {
				outputStream.writeByte (request.ordinal());
				outputStream.writeByte (firstKeyCode);
				outputStream.writeByte (count);	
			}
		};
	}
}
