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
import com.liaquay.tinyx.model.Server;

public class QueryFont implements RequestHandler {


	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int fid = inputStream.readInt();

		Font f = (Font) server.getResources().get(fid);
		String name = f.getRequestedName();

		System.out.println("Name: " + name);

		String newName = name.replaceAll("\\*", ".*[^-]");

		for (String fontName : server.getFontFactory().getFontNames()) {
			if (f.matches(fontName)) {
				f.setFont(fontName);
			}
		}


		String fontName = f.getFontName();

		int[] prop=new int[2];
		prop[0] = server.getAtoms().allocate("FONT").getId();
		prop[1] = server.getAtoms().allocate(fontName).getId();

		final XOutputStream outputStream = response.respond(1);
		//		, 7 + 
		//				(prop!=null ? prop.length/2 : 0)*2 + 
		//				//                (hascinfo ? 
		//				//                 (f.font.max_char_or_byte2-f.font.min_char_or_byte2+1)*3 :
		//				0);
		// Request... What the heck is a FONTABLE?? That's just weird

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
		outputStream.writeShort(0);                      // left-side-bearing
		outputStream.writeShort(f.getMaxWidth());      // right-side-bearing
		outputStream.writeShort(f.getMaxWidth());       // character-width
		outputStream.writeShort(f.getMaxAscent());  // ascent
		outputStream.writeShort(f.getMaxDescent()); // descent
		outputStream.writeShort(0);                        // attribute

		outputStream.writePad(4);

		int minCharOrByte2 = 0x20;
		int maxCharOrByte2 = 0xff;

		outputStream.writeShort(minCharOrByte2);  // min-char-or-byte2
		outputStream.writeShort(maxCharOrByte2);    // max-char-or-byte2

		outputStream.writeShort(f.getDefaultChar()); // default-char
		outputStream.writeShort(prop!=null ? prop.length/2 : 0); // m
		outputStream.writeByte(0);  // draw-direction
		//	         0     LeftToRight
		//	         1     RightToLeft

		outputStream.writeByte(0); // min-byte1
		outputStream.writeByte(0); // max-byte1
		outputStream.writeByte(0);  // all-char-exists
		outputStream.writeShort(f.getMaxAscent());  // font-ascent
		outputStream.writeShort(f.getMaxDescent()); // font-descent

		outputStream.writeInt(0xff-0x20+1); // m 
		//		outputStream.writeInt(0);//fontName.length()); // reply-hint

		if(prop!=null){
			for(int j=0; j<prop.length; j++){
				outputStream.writeInt(prop[j]);
			}
		}

		byte[] src=new byte[1];	
		char[] dst=new char[1];	
		int width;
		for(int i=minCharOrByte2; i<=maxCharOrByte2; i++){
//			src[0]=(byte)i;
			//	          encode(src, 0, 1, dst);
			char w= 32;//(char) f.font.charWidth(dst[0]);
			outputStream.writeShort(0);                    // left-side-bearing
			outputStream.writeShort(w);                    // right-side-bearing
			outputStream.writeShort(32);                   // character-width
			outputStream.writeShort(f.getMaxAscent());                   // ascent
			outputStream.writeShort(f.getMaxDescent());                   // descent
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
