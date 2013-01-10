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
package com.liaquay.tinyx.requesthandlers.extensions;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class RenderHandler implements RequestHandler {

	int major = 0;
	int minor = 11;
	
	@Override
	public void handleRequest(
			final Server server, 
			final Client client,
			final Request request, 
			final Response response) throws IOException {

		XInputStream inputStream = request.getInputStream();
		
		int blahevent = inputStream.readSignedByte();
		int blahopcode = inputStream.readSignedByte();
		int blah = inputStream.readSignedByte();

		
		int major = inputStream.readSignedByte();
		int minor = inputStream.readSignedByte();

		
		byte[] data = new byte[300];
		inputStream.read(data, 0, 300);
		
		System.out.println();

		//		
//		System.out.print("Major: " + major + " Minor: " + minor);
//		
//
//		String str = inputStream.readString();
//		System.out.print(str);
//		
//		for (int i = 0; i < request.getLength()*4; i++) {
//			int a = inputStream.readSignedByte();
//			System.out.print((char) a);
//		}


//		final XOutputStream outputStream = response.respond(1, 0);
//		outputStream.writeInt(server.getMaximumRequestLength());
	}
}