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

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.KeyboardMapping;
import com.liaquay.tinyx.model.Server;

public class GetKeyboardMapping implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int firstKeycode = inputStream.readUnsignedByte();
		final int count = inputStream.readUnsignedByte();

		final KeyboardMapping keyboardMapping = server.getKeyboard().getKeyboardMapping();
		final int keysymsPerKeyCode = keyboardMapping.getKeysymsPerKeycode();
		final int length = count * keysymsPerKeyCode;
		final int offset = (firstKeycode - keyboardMapping.getFirstKeyCode())	* keysymsPerKeyCode;
		final int[] mappings = keyboardMapping.getMappingArray();

		final XOutputStream outputStream = response.respond(keysymsPerKeyCode, length << 2);
		response.padHeader();
		for(int i = 0; i < length; ++i) {
			final int index = offset + i;
			outputStream.writeInt(index < 0 || index >= mappings.length ? 0 :mappings[index]);
		}
	}
}
