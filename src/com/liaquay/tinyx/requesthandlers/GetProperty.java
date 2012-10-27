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
import com.liaquay.tinyx.model.Property;
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

		final String propertyName = server.getAtoms().get(propertyId);
		if(propertyName == null) {
			response.error(Response.ErrorCode.Atom, propertyId);
			return;
		}

		final String typeName = server.getAtoms().get(typeId);
		if(typeName == null) {
			response.error(Response.ErrorCode.Atom, typeId);
			return;
		}

		int actualTypeAtomId = 0;
		int format = 0;
		int l = 0;
		int i = 0;
		int bytesAfter = 0;
		int valueLength = 0;
		final Property property = window.getProperty(propertyId);
		if(property != null) {
			final PropertyValue value = property.getValue();
			actualTypeAtomId = value.getTypeAtomId();
			if(typeId != 0 && actualTypeAtomId != typeId) {
				bytesAfter = value.getLengthInBytes();
			}
			else {
				final int n = value.getLengthInBytes();
				i = 4 * longOffset;
				final int t = n - i;

				if (longLength < 0 || longLength > 536870911)
					longLength = 536870911;	// Prevent overflow.

					if (t < longLength * 4) {
						l = t;
					} 
					else {
						l = longLength * 4;
					}

					bytesAfter = n - (i + l);

					if (l < 0) {
						response.error(Response.ErrorCode.Value, 0);
						return;
					}
					else {
						valueLength = value.getLength();
					}
			}
		}
		final int pad = -l & 3;
		final XOutputStream outputStream = response.respond(format, l+pad);
		outputStream.writeInt(actualTypeAtomId);	// Type.
		outputStream.writeInt(bytesAfter);	// Bytes after.
		outputStream.writeInt(valueLength);	// Value length. 
		response.padHeader();

		if(l > 0 && property != null) {
			final PropertyValue value = property.getValue();
			switch(value.getFormat()) {
			case ByteFormat:
				writeBytes(outputStream, ((BytePropertyValue)value).getData(), i, l);
				break;
			case ShortFormat:
				writeShorts(outputStream, ((ShortPropertyValue)value).getData(), i, l);
				break;
			case IntFormat:
				writeInts(outputStream, ((IntPropertyValue)value).getData(), i, l);
				break;
			default:
				throw new RuntimeException("Undefined property format " + value.getFormat());
			}
		}

		if(delete) {
			window.deleteProperty(propertyId);
		}			
	}

	private void writeBytes(
			final XOutputStream outputStream,
			final byte[] data, 
			final int offsetInBytes,
			final int lengthInBytes) throws IOException {

		outputStream.write(data, offsetInBytes, lengthInBytes);
	}	

	private void writeShorts(
			final XOutputStream outputStream,
			final short[] data, 
			final int offsetInBytes,
			final int lengthInBytes) throws IOException {

		final int offsetInShorts = offsetInBytes >> 1;
					final int lengthInShorts =  lengthInBytes >> 1;
					for(int i = 0; i < lengthInShorts; ++ i){						
						outputStream.writeShort(data[offsetInShorts + i]);
					}
	}	

	private void writeInts(
			final XOutputStream outputStream,
			final int[] data, 
			final int offsetInBytes,
			final int lengthInBytes) throws IOException {

		final int offsetInInts = offsetInBytes >> 2;
		final int lengthInInts =  lengthInBytes >> 2;
		for(int i = 0; i < lengthInInts; ++ i){						
			outputStream.writeInt(data[offsetInInts + i]);
		}
	}
}
