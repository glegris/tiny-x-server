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
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Atom;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Property;
import com.liaquay.tinyx.model.Property.Mode;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.properties.BytePropertyValue;
import com.liaquay.tinyx.model.properties.IntPropertyValue;
import com.liaquay.tinyx.model.properties.ShortPropertyValue;

public class ChangeProperty implements RequestHandler {

	private final static Logger LOGGER = Logger.getLogger(ChangeProperty.class.getName());

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
		final Atom propertyAtom = server.getAtoms().get(propertyId);
		if(propertyAtom == null) {
			response.error(Response.ErrorCode.Atom, propertyId);
			return;
		}
		final int typeId = inputStream.readInt();
		final Atom typeAtom = server.getAtoms().get(typeId);
		if(typeAtom == null) {
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
		Property property = window.getProperties().get(propertyId);
		final int n = inputStream.readInt();
		
		if(!mode.equals(Mode.PropModeReplace)) {
			if(property == null) {
				response.error(Response.ErrorCode.Match, propertyId); //TODO This is probably not the correct error code
				return;						
			}
			if(!format.equals(property.getValue().getFormat())) {
				response.error(Response.ErrorCode.Match, noOfBits);
				return;				
			}
			if(property.getValue().getTypeAtom().getId() != typeId) {
				response.error(Response.ErrorCode.Match, typeId);
				return;
			}
		}
		
		if(property == null) {
			property = new Property(propertyAtom);
			window.getProperties().add(property);
		}
		
		switch(format) {
		case ByteFormat: changeBytes(inputStream, property, typeAtom, mode, n); break;
		case ShortFormat: changeShorts(inputStream, property, typeAtom, mode, n); break;
		case IntFormat: changeInts(inputStream, property, typeAtom, mode, n); break;
		}
	}

	private void changeBytes(
			final XInputStream inputStream,
			final Property property,
			final Atom typeAtom,
			final Property.Mode mode,
			final int n) throws IOException {

		final byte[] data = new  byte[n];
		inputStream.read(data, 0, n);
		
		LOGGER.log(Level.INFO, "Change property received for " +property.getPropertyAtom().getText() + " value " + new String(data));
		
		switch(mode) {
		case PropModeReplace: {
			final BytePropertyValue bytePropertyValue = new BytePropertyValue(typeAtom, data);
			property.setValue(bytePropertyValue);
			break;
		}
		case PropModeAppend: {
			final BytePropertyValue bytePropertyValue = (BytePropertyValue)property.getValue();
			bytePropertyValue.append(data);
			break;
		}
		case PropModePrepend: {
			final BytePropertyValue bytePropertyValue = (BytePropertyValue)property.getValue();
			bytePropertyValue.append(data);
			break;
		}
		}
	}
	
	private void changeShorts(
			final XInputStream inputStream,
			final Property property,
			final Atom typeAtom,
			final Property.Mode mode,
			final int n) throws IOException {

		final short[] data = new  short[n];
		for(int i = 0; i < n; ++i) {
			data[i] = (short)inputStream.readSignedShort();
		}
		switch(mode) {
		case PropModeReplace: {
			final ShortPropertyValue bytePropertyValue = new ShortPropertyValue(typeAtom, data);
			property.setValue(bytePropertyValue);
			break;
		}
		case PropModeAppend: {
			final ShortPropertyValue bytePropertyValue = (ShortPropertyValue)property.getValue();
			bytePropertyValue.append(data);
			break;
		}
		case PropModePrepend: {
			final ShortPropertyValue bytePropertyValue = (ShortPropertyValue)property.getValue();
			bytePropertyValue.append(data);
			break;
		}
		}
	}
	
	private void changeInts(
			final XInputStream inputStream,
			final Property property,
			final Atom typeAtom,
			final Property.Mode mode,
			final int n) throws IOException {

		final int[] data = new  int[n];
		for(int i = 0; i < n; ++i) {
			data[i] = (short)inputStream.readInt();
		}
		switch(mode) {
		case PropModeReplace: {
			final IntPropertyValue bytePropertyValue = new IntPropertyValue(typeAtom, data);
			property.setValue(bytePropertyValue);
			break;
		}
		case PropModeAppend: {
			final IntPropertyValue bytePropertyValue = (IntPropertyValue)property.getValue();
			bytePropertyValue.append(data);
			break;
		}
		case PropModePrepend: {
			final IntPropertyValue bytePropertyValue = (IntPropertyValue)property.getValue();
			bytePropertyValue.append(data);
			break;
		}
		}
	}	
}