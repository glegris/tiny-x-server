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
import com.liaquay.tinyx.model.Focus;
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.Window;

public class ButtonFactoryImpl {
	
	public Event create(
			final int event,
			final int button,
			final Focus focus,
			final Pointer pointer,
			final int keyButtonMask,
			final int when) {

		final Window focusWindow;
		switch(focus.getMode()) {
		case None:
			// Do not deliver an event for a focus of none.
			return null;
		case PointerRoot:
			// Deliver events relative to the pointer root.
			focusWindow = pointer.getScreen().getRootWindow();
			break;
		default:
			// Deliver events relative to the focus window.
			focusWindow = focus.getWindow();
			break;
		}
		final int focusWindowId = focusWindow.getId();

		final boolean sameScreen = focusWindow.getRootWindow() == pointer.getScreen().getRootWindow();

		// Obtain the root window for the event.
		final Window rootWindow = pointer.getScreen().getRootWindow();
		final int rootWindowId = rootWindow.getId ();

		// Find the window that contained the event
		final Window child =pointer.childWindowAt();
		final int childWindowId = child == null ? 0 : child.getId ();
		
		final int rootX = pointer.getX() - focusWindow.getAbsX();
		final int rootY = pointer.getY() - focusWindow.getAbsY();
		final int eventX;
		final int eventY;

		if(sameScreen) {
			eventX = pointer.getX() - focusWindow.getAbsX();
			eventY = pointer.getY() - focusWindow.getAbsY();
		}
		else {
			eventX = 0;
			eventY = 0;
		}	
		return new TimestampedEventImpl(event, button, when) {

			@Override
			public void writeTimestampedBody(final XOutputStream outputStream) throws IOException {
				outputStream.writeInt (rootWindowId);
				outputStream.writeInt (focusWindowId);
				outputStream.writeInt (childWindowId);
				outputStream.writeShort (rootX);
				outputStream.writeShort (rootY);
				outputStream.writeShort (eventX);
				outputStream.writeShort (eventY);
				outputStream.writeShort (keyButtonMask);
				outputStream.writeByte (sameScreen ? 1 : 0);
			}
		};
	}
}
