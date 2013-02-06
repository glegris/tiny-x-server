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
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.TextExtents;

public class QueryFont implements RequestHandler {

	private final static Logger LOGGER = Logger.getLogger(QueryFont.class.getName());

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int fid = inputStream.readInt();
		final Font font = (Font) server.getResources().get(fid);
		if(font == null) {
			response.error(Response.ErrorCode.Font, fid);
			return;
		}
		
		final String fontName = font.getFontDetail().getName();
		
		LOGGER.log(Level.INFO, "QueryFont for name: " + fontName);
		
		final XOutputStream outputStream = response.respond(1);
		
		final int[] prop=new int[2];
		prop[0] = server.getAtoms().get("FONT").getId();
		prop[1] = server.getAtoms().getOrAllocate(fontName).getId();
		
		final FontDetail fontDetail = font.getFontDetail();
		
		// Min-bounds
		write(outputStream, fontDetail.getMinBounds());

		// Unused
		outputStream.writePad(4);

		// Max-bounds
		write(outputStream, fontDetail.getMaxBounds());

		outputStream.writePad(4);


		outputStream.writeShort(fontDetail.getFirstChar());		// min-char-or-byte2
		outputStream.writeShort(fontDetail.getLastChar());		// max-char-or-byte2

		outputStream.writeShort(fontDetail.getDefaultChar());	// default-char
		outputStream.writeShort(prop!=null ? prop.length/2 : 0); // m
		outputStream.writeByte(fontDetail.isLeftToRight() ? 0 : 1);  // draw-direction

		outputStream.writeByte(0);						// min-byte1
		outputStream.writeByte(0);						// max-byte1
		outputStream.writeByte(0);						// all-char-exists
		outputStream.writeShort(fontDetail.getAscent());	// font-ascent
		outputStream.writeShort(fontDetail.getDescent());	// font-descent

		outputStream.writeInt(fontDetail.getLastChar()-fontDetail.getFirstChar()+1); // m 

		if(prop!=null){
			for(int j=0; j<prop.length; j++){
				outputStream.writeInt(prop[j]);
			}
		}

		for(int i=fontDetail.getFirstChar(); i<=fontDetail.getLastChar(); i++){
			final TextExtents textExtents = fontDetail.getTextExtents(i);
			if (textExtents != null) {
				write(outputStream, textExtents);
			} else {
				final String message = "No Glyph details found for char: " + i;
				LOGGER.log(Level.SEVERE, message);
				throw new RuntimeException(message);
			}
		}			
	}
	
	private final static void write(
			final XOutputStream outputStream,
			final TextExtents textExtents) throws IOException {
		
		outputStream.writeShort(textExtents.getLeft()); // left-side-bearing
		outputStream.writeShort(textExtents.getRight()); // right-side-bearing
		outputStream.writeShort(textExtents.getWidth()); // character-width
		outputStream.writeShort(textExtents.getAscent()); // ascent
		outputStream.writeShort(textExtents.getDescent()); // descent
		outputStream.writeShort(textExtents.getAttributes()); // attribute
	}
}
