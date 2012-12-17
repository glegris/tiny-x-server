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
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.GraphicsContextAttributeHandlers;

public class ChangeGraphicsContext implements RequestHandler {
	
	private final GraphicsContextAttributeHandlers _attributeHandlers; 
	
	public ChangeGraphicsContext(final GraphicsContextAttributeHandlers attributeHandlers) {
		_attributeHandlers = attributeHandlers;
	}
	
	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {

//		 ............REQUEST: ChangeGC
//	     sequence number: 0000000d
//	      request length: 0008
//	                  gc: GXC 00400002
//	          value-mask: foreground | background | line-width | join-style | font
//	          value-list:
//		          foreground: ff000000
//		          background: ffffffff
//		          line-width: 0002
//		          join-style: Bevel
//		                font: FNT 00400003
//	             Request (fd 7): 4a ff 08 00 01 00 40 00 02 00 40 00 00 00 
//5a 00 0b 00 68 65 6c 6c 6f 20 77 6f 72 6c 64 00 00 00 

		final XInputStream inputStream = request.getInputStream();
		final int graphicsContextId = inputStream.readInt(); 		
		final GraphicsContext gc = server.getResources().get(graphicsContextId, GraphicsContext.class);
		if(gc == null) {
			response.error(Response.ErrorCode.GContext, graphicsContextId);		
			return;
		}		
		final int valueMask = inputStream.readInt();

		_attributeHandlers.read(server, client, request, response, gc, valueMask);
		
		
//	     4     GCONTEXT                        gc
//	     4     BITMASK                         value-mask (has n bits set to 1)
//	          encodings are the same as for CreateGC
//	     4n     LISTofVALUE                    value-list
//	          encodings are the same as for CreateGC

		
	
	}
}
