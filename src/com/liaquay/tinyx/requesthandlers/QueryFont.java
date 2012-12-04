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
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.FontString;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.renderers.awt.GlyphDetail;

public class QueryFont implements RequestHandler {


	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int fid = inputStream.readInt();

		Font f = (Font) server.getResources().get(fid);
		FontString fontName = f.getFontName();

		System.out.println("Name: " + fontName.toString());

		int[] prop=new int[2];
		prop[0] = server.getAtoms().allocate("FONT").getId();
		prop[1] = server.getAtoms().allocate(fontName.toString()).getId();

		final XOutputStream outputStream = response.respond(1);

		// Min-bounds
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeShort(f.getMinWidth());
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeShort(0);

		// Unused
		outputStream.writePad(4);

		// Max-bounds
		outputStream.writeShort(0);						// left-side-bearing
		outputStream.writeShort(f.getMaxWidth());		// right-side-bearing
		outputStream.writeShort(f.getMaxWidth());       // character-width
		outputStream.writeShort(f.getMaxAscent());		// ascent
		outputStream.writeShort(f.getMaxDescent());		// descent
		outputStream.writeShort(0);						// attribute

		outputStream.writePad(4);


		outputStream.writeShort(f.getFirstChar());		// min-char-or-byte2
		outputStream.writeShort(f.getLastChar());		// max-char-or-byte2

		outputStream.writeShort(f.getDefaultChar());	// default-char
		outputStream.writeShort(prop!=null ? prop.length/2 : 0); // m
		outputStream.writeByte(0);  // draw-direction
		//	         0     LeftToRight
		//	         1     RightToLeft

		outputStream.writeByte(0);						// min-byte1
		outputStream.writeByte(0);						// max-byte1
		outputStream.writeByte(0);						// all-char-exists
		outputStream.writeShort(f.getMaxAscent());		// font-ascent
		outputStream.writeShort(f.getMaxDescent());		// font-descent

		outputStream.writeInt(f.getLastChar()-f.getFirstChar()+1); // m 
		
		//		outputStream.writeInt(0);//fontName.length()); // reply-hint

		if(prop!=null){
			for(int j=0; j<prop.length; j++){
				outputStream.writeInt(prop[j]);
			}
		}

		for(int i=f.getFirstChar(); i<=f.getLastChar(); i++){
			GlyphDetail gd = f.getGlyphDetail((char) i);
			outputStream.writeShort(0);//gd.leftSideBearing());	// left-side-bearing
			outputStream.writeShort(0);//gd.rightSideBearing());	// right-side-bearing
			outputStream.writeShort(gd.getWidth());			// character-width
			outputStream.writeShort(gd.getAscent());		// ascent
			outputStream.writeShort(gd.getDescent());		// descent
			outputStream.writeShort(0);                    // attribute
		}	
		//	      if(hascinfo){
		//	          f.dumpCharInfo(c);                      // m 
		//	        }
		//	    outputStream.write(fontName.getBytes(), 0, fontName.length());
		//	    outputStream.writePad((-font.lfname.length)&3);



		//	    4     m                               number of CHARINFOs in char-infos
		//	    8n     LISTofFONTPROP                 properties
		//	    12m     LISTofCHARINFO                char-infos


	}

}
