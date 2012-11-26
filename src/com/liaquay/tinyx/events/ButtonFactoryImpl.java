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
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.Window;

public class ButtonFactoryImpl {
	public Event create(
			final int		event,
			final int		button,
			final Window	root,
			final Window	eventWindow,
			final Window	child,
			final Pointer	pointer,
			final int keyButtonMask,
			final boolean	sameScreen) {
	
		return new TimestampedEventImpl(event, button) {
			@Override
			public void writeTimestampedBody(final XOutputStream outputStream) throws IOException {
				outputStream.writeInt (root.getId ());
				outputStream.writeInt (eventWindow.getId ());
				outputStream.writeInt (child == null ? 0 : child.getId ());
				outputStream.writeShort (pointer.getX() - root.getAbsX());
				outputStream.writeShort (pointer.getY() - root.getAbsY());
				outputStream.writeShort (pointer.getX() - eventWindow.getAbsX());
				outputStream.writeShort (pointer.getY() - eventWindow.getAbsY());
				outputStream.writeShort (keyButtonMask);
				outputStream.writeByte (sameScreen ? 1 : 0);
			}
		};
	}
}
