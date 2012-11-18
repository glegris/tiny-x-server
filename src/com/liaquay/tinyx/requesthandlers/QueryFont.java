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
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class QueryFont implements RequestHandler {

	// Request... What the heck is a FONTABLE?? That's just weird
	
//    1     47                              opcode
//    1                                     unused
//    2     2                               request length
//    4     FONTABLE                        font
	

//    1     1                               Reply
//    1                                     unused
//    2     CARD16                          sequence number
//    4     7+2n+3m                         reply length
//    12     CHARINFO                       min-bounds
//    4                                     unused
//    12     CHARINFO                       max-bounds
//    4                                     unused
//    2     CARD16                          min-char-or-byte2
//    2     CARD16                          max-char-or-byte2
//    2     CARD16                          default-char
//    2     n                               number of FONTPROPs in properties
//    1                                     draw-direction
//         0     LeftToRight
//         1     RightToLeft
//    1     CARD8                           min-byte1
//    1     CARD8                           max-byte1
//    1     BOOL                            all-chars-exist
//    2     INT16                           font-ascent
//    2     INT16                           font-descent
//    4     m                               number of CHARINFOs in char-infos
//    8n     LISTofFONTPROP                 properties
//    12m     LISTofCHARINFO                char-infos
    
	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {
		// TODO Auto-generated method stub

	}

}
