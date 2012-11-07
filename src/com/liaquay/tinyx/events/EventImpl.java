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
import com.liaquay.tinyx.model.Event;

public abstract class EventImpl implements Event {

	private final int _eventType;
	private final int _argument;
	
	public EventImpl(final int eventType, final int argument) {
		_eventType = eventType;
		_argument = argument;
	}
	@Override
	public final void write(final XOutputStream outputStream, final int sequenceNumber) throws IOException {
		outputStream.resetCounter();
		outputStream.writeByte(_eventType);
		outputStream.writeByte(_argument);
		outputStream.writeShort(sequenceNumber);
		writeBody(outputStream);
		outputStream.writePad(32 - outputStream.getCounter());
	}
	
	public abstract void writeBody(final XOutputStream outputStream) throws IOException;
}
