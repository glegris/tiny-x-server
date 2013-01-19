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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.FontAlias;
import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.font.FontDetail;

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

		final List<String> patterns = new ArrayList<String>();
		patterns.add(requestedFontName);
		for(final FontAlias alias : server.getFontAliases(requestedFontName)) {
			patterns.add(alias.getPattern());
		}
		
		final List<FontInfo> matches = new ArrayList<FontInfo>();

		for(final String pattern : patterns) {
			matches.addAll(server.getFontFactory().getMatchingFonts(pattern));
		}
		
		int countDown = maxNames;//fonts.size();
		int counter = 0;
		for (final FontInfo fontInfo : matches) {
			
			final FontDetail fontDetail = server.getFontFactory().getFontDetail(fontInfo);
			
			writeFontInfo(server, fontInfo, fontDetail, response, --countDown);
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
			final FontInfo fontInfo, 
			final FontDetail fontDetail,
			final Response response,
			final int countDown) throws IOException {
		
		final String fontName = fontInfo.toString();
		final int fontNameLength = fontName.length();
		final XOutputStream outputStream = response.respond(fontNameLength);
		
		final int[] prop=new int[2];
		prop[0] = server.getAtoms().get("FONT").getId();
		prop[1] = server.getAtoms().getOrAllocate(fontName).getId();
		
		// Min-bounds
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeShort(fontDetail.getMinWidth());
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeShort(0);

		// Unused
		outputStream.writePad(4);

		// Max-bounds
		outputStream.writeShort(0);						// left-side-bearing
		outputStream.writeShort(fontDetail.getMaxWidth());		// right-side-bearing
		outputStream.writeShort(fontDetail.getMaxWidth());       // character-width
		outputStream.writeShort(fontDetail.getMaxAscent());		// ascent
		outputStream.writeShort(fontDetail.getMaxDescent());		// descent
		outputStream.writeShort(0);						// attribute

		outputStream.writePad(4);


		outputStream.writeShort(fontDetail.getFirstChar());		// min-char-or-byte2
		outputStream.writeShort(fontDetail.getLastChar());		// max-char-or-byte2

		outputStream.writeShort(fontDetail.getDefaultChar());	// default-char
		outputStream.writeShort(prop!=null ? prop.length/2 : 0); // m
		outputStream.writeByte(0);  // draw-direction
		//	         0     LeftToRight
		//	         1     RightToLeft

		outputStream.writeByte(0);						// min-byte1
		outputStream.writeByte(0);						// max-byte1
		outputStream.writeByte(0);						// all-char-exists
		outputStream.writeShort(fontDetail.getMaxAscent());		// font-ascent
		outputStream.writeShort(fontDetail.getMaxDescent());	// font-descent

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
}
