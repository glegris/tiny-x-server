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
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.Window;

public class KeyFactoryImpl {
	
	public Event create(
			final int event,
			final Window focusWindow,
			final Window child,
			final Pointer pointer,
			final int key,
			final int when) {

		// Obtain the root window for the event.
		final Window rootWindow = pointer.getScreen().getRootWindow();
		final int rootWindowId = rootWindow.getId ();

		// Find the window that contained the event
		final int childWindowId = child == null ? 0 : child.getId ();
		
		final int rootX = pointer.getX() - rootWindow.getAbsX();
		final int rootY = pointer.getY() - rootWindow.getAbsY();

		return new TimestampedEventImpl(event, key, when) {

			@Override
			public void writeTimestampedBody(final XOutputStream outputStream, final Client client, final Window eventWindow) throws IOException {
				
				final Window w = eventWindow.hasAncestor(focusWindow) ? eventWindow : focusWindow;
				final boolean sameScreen = w.getRootWindow() == pointer.getScreen().getRootWindow();
				final int eventX;
				final int eventY;
				if(sameScreen) {
					eventX = pointer.getX() - w.getAbsX();
					eventY = pointer.getY() - w.getAbsY();
				}
				else {
					eventX = 0;
					eventY = 0;
				}
				outputStream.writeInt (rootWindowId);
				outputStream.writeInt (eventWindow.getId());
				outputStream.writeInt (childWindowId);
				outputStream.writeShort (rootX);
				outputStream.writeShort (rootY);
				outputStream.writeShort (eventX);
				outputStream.writeShort (eventY);
				outputStream.writeShort (key);
				outputStream.writeByte (sameScreen ? 1 : 0);
			}
		};
	}
}
