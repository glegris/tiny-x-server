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
package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.Response.ErrorCode;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Host;
import com.liaquay.tinyx.model.Host.Family;
import com.liaquay.tinyx.model.Server;

public class ChangeHosts implements RequestHandler {

	private final static Logger LOGGER = Logger.getLogger(ChangeHosts.class.getName());

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final int mode = request.getData();
		if(mode < 0 || mode > 1) {
			LOGGER.log(Level.SEVERE, "Unknown changeHost mode " + mode);
			response.error(ErrorCode.Value, mode);
			return;
		}
		
		final XInputStream inputStream = request.getInputStream();

		final int familyIndex = inputStream.readUnsignedByte();
		final Family family = Host.Family.getFromIndex(familyIndex);
		if(family == null) {
			LOGGER.log(Level.SEVERE, "Unknown changeHost family " + family);
			response.error(ErrorCode.Value, familyIndex);
			return;
		}
		
		inputStream.skip(1);

		final int lengthOfAddress = inputStream.readUnsignedShort();

		final byte[] address = new byte[lengthOfAddress];
		for (int i = 0; i < address.length; i++) {
			address[i] = (byte) inputStream.readUnsignedByte();
		}

		final Host h = new Host(address, family);

		if (mode == 0) {
			// Insert mode
			server.getAccessControls().getHosts().add(h);
		} 
		else {
			// Delete mode
			server.getAccessControls().getHosts().remove(h);
		}
	}
}
