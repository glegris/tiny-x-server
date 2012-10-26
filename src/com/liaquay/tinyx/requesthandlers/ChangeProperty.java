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
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Property;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class ChangeProperty implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();		
		final int modeIndex = request.getData();
		final Property.Mode mode = Property.Mode.getFromIndex(modeIndex);
		if(mode==null) {
			response.error(Response.ErrorCode.Value, modeIndex);
			return;
		}
		final int windowId = inputStream.readInt(); 		
		final Window window = server.getResources().get(windowId, Window.class);
		if(window == null) {
			response.error(Response.ErrorCode.Window, windowId);			
			return;
		}
		final int propertyId =inputStream.readInt(); 
		final String propertyName = server.getAtoms().get(propertyId);
		if(propertyName == null) {
			response.error(Response.ErrorCode.Atom, propertyId);
			return;
		}
		final int typeId = inputStream.readInt();
		final String typeName = server.getAtoms().get(typeId);
		if(typeName == null) {
			response.error(Response.ErrorCode.Atom, typeId);
			return;
		}
		final int noOfBits = inputStream.readUnsignedByte();
		final Property.Format format = Property.Format.getFromNoOfBits(noOfBits);
		if (format==null) {
			response.error(Response.ErrorCode.Value, noOfBits);
			return;	
		}
		inputStream.skip(3);
		final Property property = window.getProperty(propertyId);
		final int n = inputStream.readInt();
		
		// TODO implement change of property
	}
}