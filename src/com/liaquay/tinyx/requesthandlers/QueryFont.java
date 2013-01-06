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
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.renderers.awt.GlyphDetail;

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
		final String fontName = font.getFontInfo().toString();
		
		LOGGER.log(Level.INFO, "QueryFont for name: " + fontName);
		
		final XOutputStream outputStream = response.respond(1);
		
		final int[] prop=new int[2];
		prop[0] = server.getAtoms().get("FONT").getId();
		prop[1] = server.getAtoms().getOrAllocate(fontName).getId();
		
		// Min-bounds
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeShort(font.getMinWidth());
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeShort(0);

		// Unused
		outputStream.writePad(4);

		// Max-bounds
		outputStream.writeShort(0);						// left-side-bearing
		outputStream.writeShort(font.getMaxWidth());		// right-side-bearing
		outputStream.writeShort(font.getMaxWidth());       // character-width
		outputStream.writeShort(font.getMaxAscent());		// ascent
		outputStream.writeShort(font.getMaxDescent());		// descent
		outputStream.writeShort(0);						// attribute

		outputStream.writePad(4);


		outputStream.writeShort(font.getFirstChar());		// min-char-or-byte2
		outputStream.writeShort(font.getLastChar());		// max-char-or-byte2

		outputStream.writeShort(font.getDefaultChar());	// default-char
		outputStream.writeShort(prop!=null ? prop.length/2 : 0); // m
		outputStream.writeByte(0);  // draw-direction
		//	         0     LeftToRight
		//	         1     RightToLeft

		outputStream.writeByte(0);						// min-byte1
		outputStream.writeByte(0);						// max-byte1
		outputStream.writeByte(0);						// all-char-exists
		outputStream.writeShort(font.getMaxAscent());		// font-ascent
		outputStream.writeShort(font.getMaxDescent());		// font-descent

		outputStream.writeInt(font.getLastChar()-font.getFirstChar()+1); // m 

		//		outputStream.writeInt(0);//fontName.length()); // reply-hint

		if(prop!=null){
			for(int j=0; j<prop.length; j++){
				outputStream.writeInt(prop[j]);
			}
		}

		for(int i=font.getFirstChar(); i<=font.getLastChar(); i++){
			final GlyphDetail gd = font.getGlyphDetail(i);
			if (gd != null) {
				outputStream.writeShort(0);//gd.leftSideBearing());	// left-side-bearing
				outputStream.writeShort(0);//gd.rightSideBearing());	// right-side-bearing
				outputStream.writeShort(gd.getWidth());			// character-width
				outputStream.writeShort(gd.getAscent());		// ascent
				outputStream.writeShort(gd.getDescent());		// descent
				outputStream.writeShort(0);                    // attribute
			} else {
				LOGGER.log(Level.WARNING, "No Glyph details found for char: " + i);
			}
		}	

	}
}
