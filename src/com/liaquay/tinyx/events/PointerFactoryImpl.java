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
import com.liaquay.tinyx.model.ClientWindowAssociation;
import com.liaquay.tinyx.model.Event;
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.PointerGrab;
import com.liaquay.tinyx.model.Window;

public class PointerFactoryImpl {
	
	public Event create(
			final int event,
			final int eventMask,
			final int button,
			final PointerGrab grab,
			final Pointer pointer,
			final Window child,
			final int keyButtonMask,
			final int when) {

		// Obtain the root window for the event.
		final Window rootWindow = grab == null ?
				pointer.getScreen().getRootWindow() :
				grab.getGrabWindow().getRootWindow();
		
		final int rootWindowId = rootWindow.getId ();

		// Find the window that contained the event
		final int childWindowId = child == null ? 0 : child.getId();
		
		final int rootX = pointer.getX() - rootWindow.getAbsX();
		final int rootY = pointer.getY() - rootWindow.getAbsY();

		return new TimestampedEventImpl(event, button, when) {

			@Override
			public void writeTimestampedBody(final XOutputStream outputStream, final Client client, final Window w) throws IOException {
				
				final Window eventWindow;
				if(grab == null) {
					eventWindow = w;
				}
				else if(child == null) {
					eventWindow = grab.getGrabWindow();
				}
				else if(grab.isOwnerEvents()) {
					// Get the window association that this event would have delivered to
					final ClientWindowAssociation clientWindowAssociation = child.getDeliverToAssociation(eventMask, client);
					
					if(clientWindowAssociation == null) {
						eventWindow = grab.getGrabWindow();
					}
					else {
						eventWindow = clientWindowAssociation.getWindow();
					}
				}
				else {
					eventWindow = grab.getGrabWindow();
				}

				// TODO Is this correct?
				final boolean sameScreen = rootWindow == pointer.getScreen().getRootWindow();
				final int eventWindowId = eventWindow.getId();
				
				final int eventX;
				final int eventY;
				
				if(sameScreen) {
					eventX = pointer.getX() - eventWindow.getAbsX();
					eventY = pointer.getY() - eventWindow.getAbsY();
				}
				else {
					eventX = 0;
					eventY = 0;
				}
				
				outputStream.writeInt (rootWindowId);
				outputStream.writeInt (eventWindowId);
				outputStream.writeInt (childWindowId == eventWindowId ? 0 : childWindowId);
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
