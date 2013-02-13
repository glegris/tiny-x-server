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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.FontFactory;
import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.TextExtents;

public class ListFontsWithInfo implements RequestHandler {

	private final static Logger LOGGER = Logger.getLogger(ListFontsWithInfo.class.getName());
	
	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int maxNames = inputStream.readUnsignedShort();
		final String requestedFontName = inputStream.readString();

		final List<FontMatch> matches = server.getFontFactory().getMatchingFonts(requestedFontName);
		final FontFactory fontFactory = server.getFontFactory();
		int countDown = Math.min(maxNames, matches.size());
		int counter = 0;
		for (final FontMatch fontInfo : matches) {
			
			final FontDetail fontDetail = fontFactory.open(fontInfo);
			
			if(fontDetail == null) {
				LOGGER.log(Level.SEVERE, "Attempt to open non-existant (but matching) font " + fontInfo.getMergedFontName());
				writeFontInfo(server, server.getFixedFont().getFontDetail(), response, --countDown);
			}
			else {
				try {
					writeFontInfo(server, fontDetail, response, --countDown);
				}
				finally {
					fontFactory.close(fontDetail);
				}
			}
			counter++;
			
			if (counter >= maxNames) {
				break;
			}
		}

		// Send a blank response to indicate the last query font response
		final XOutputStream outputStream = response.respond(0);
		response.padHeader();
		outputStream.writePad(28);
	}
	
	public static void writeFontInfo(
			final Server server, 
			final FontDetail fontDetail,
			final Response response,
			final int countDown) throws IOException {
		
		final String fontName = fontDetail.getName();
		final int fontNameLength = fontName.length();
		final XOutputStream outputStream = response.respond(fontNameLength);
		
		final int[] prop=new int[2];
		prop[0] = server.getAtoms().get("FONT").getId();
		prop[1] = server.getAtoms().getOrAllocate(fontName).getId();
		
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

		outputStream.writeByte(0);						// min-byte1 TODO
		outputStream.writeByte(0);						// max-byte1 TODO
		outputStream.writeByte(0);						// all-char-exists TODO
		outputStream.writeShort(fontDetail.getAscent());	// font-ascent
		outputStream.writeShort(fontDetail.getDescent());	// font-descent

		outputStream.writeInt(countDown); // replies hint 

		if(prop!=null){
			for(int j=0; j<prop.length; j++){
				outputStream.writeInt(prop[j]);
			}
		}

		final byte[] fontNameBytes = fontName.getBytes();
		outputStream.write(fontNameBytes, 0, fontNameBytes.length);
		response.send();
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
