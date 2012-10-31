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
import com.liaquay.tinyx.model.Atom;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Property;
import com.liaquay.tinyx.model.Property.Format;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.properties.BytePropertyValue;
import com.liaquay.tinyx.model.properties.IntPropertyValue;
import com.liaquay.tinyx.model.properties.PropertyValue;
import com.liaquay.tinyx.model.properties.ShortPropertyValue;

public class GetProperty implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();		
		final int windowId = inputStream.readInt(); 		
		final Window window = server.getResources().get(windowId, Window.class);
		if(window == null) {
			response.error(Response.ErrorCode.Window, windowId);		
			return;
		}

		final boolean delete = request.getData() == 1;
		final int propertyId =inputStream.readInt(); 
		final int typeId = inputStream.readInt();
		final int longOffset = inputStream.readInt();
		int longLength = inputStream.readInt();

		final Atom propertyAtom = server.getAtoms().get(propertyId);
		if(propertyAtom == null) {
			response.error(Response.ErrorCode.Atom, propertyId);
			return;
		}

		if(typeId != 0) {
			final Atom typeAtom = server.getAtoms().get(typeId);
			if(typeAtom == null) {
				response.error(Response.ErrorCode.Atom, typeId);
				return;
			}
		}
		int actualTypeAtomId = 0;
		int formatNoBits = 0;
		int lengthInUnits = 0;// TODO Should this be long ?
		int offsetInUnits = 0;// TODO Should this be long ?
		int lengthInBytes = 0;
		int bytesAfter = 0;
		final Property property = window.getProperties().get(propertyId);
		if(property != null) {
			final PropertyValue value = property.getValue();
			final Format format = value.getFormat();
			actualTypeAtomId = value.getTypeAtom().getId();
			formatNoBits = format.getNoOfBits();
			if(typeId != 0 && actualTypeAtomId != typeId) {
				bytesAfter = value.getLengthInBytes();
			}
			else {
				offsetInUnits = format.intsToUnits(longOffset);
				if(offsetInUnits >= value.getLength()) {
					response.error(Response.ErrorCode.Value, 0);
					return;
				}
				lengthInUnits = format.intsToUnits(longLength);
				if(lengthInUnits+offsetInUnits > value.getLength()) {
					lengthInUnits = value.getLength() - offsetInUnits;
				}
				bytesAfter = (value.getLength() - (lengthInUnits + offsetInUnits)) * format.getNoOfBytes();
				lengthInBytes = lengthInUnits * format.getNoOfBytes();
			}
		}
		final XOutputStream outputStream = response.respond(formatNoBits, lengthInBytes);
		outputStream.writeInt(actualTypeAtomId);	// Type.
		outputStream.writeInt(bytesAfter);	        // Bytes after.
		outputStream.writeInt(lengthInUnits);	    // Value length. 
		response.padHeader();

		if(lengthInUnits > 0 && property != null) {
			final PropertyValue value = property.getValue();
			switch(value.getFormat()) {
			case ByteFormat:
				writeBytes(outputStream, ((BytePropertyValue)value).getData(), offsetInUnits, lengthInUnits);
				break;
			case ShortFormat:
				writeShorts(outputStream, ((ShortPropertyValue)value).getData(), offsetInUnits, lengthInUnits);
				break;
			case IntFormat:
				writeInts(outputStream, ((IntPropertyValue)value).getData(), offsetInUnits, lengthInUnits);
				break;
			default:
				throw new RuntimeException("Undefined property format " + value.getFormat());
			}
		}

		if(delete  && bytesAfter == 0) {
			window.getProperties().remove(propertyId);
		}			
	}

	private void writeBytes(
			final XOutputStream outputStream,
			final byte[] data, 
			final int offset,
			final int length) throws IOException {

		outputStream.write(data, offset, length);
	}	

	private void writeShorts(
			final XOutputStream outputStream,
			final short[] data, 
			final int offset,
			final int length) throws IOException {

		for(int i = 0; i < length; ++ i){
			outputStream.writeShort(data[offset + i]);
		}
	}	

	private void writeInts(
			final XOutputStream outputStream,
			final int[] data, 
			final int offset,
			final int length) throws IOException {

		for(int i = 0; i < length; ++ i){	
			outputStream.writeInt(data[offset + i]);
		}
	}
}
