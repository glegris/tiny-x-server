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
import com.liaquay.tinyx.Response.ErrorCode;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.ScreenSaver;
import com.liaquay.tinyx.model.Server;

public class SetScreenSaver implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {
//	     1     107                             opcode
//	     1                                     unused
//	     2     3                               request length
//	     2     INT16                           timeout
//	     2     INT16                           interval
//	     1                                     prefer-blanking
//	          0     No
//	          1     Yes
//	          2     Default
//	     1                                     allow-exposures
//	          0     No
//	          1     Yes
//	          2     Default
//	     2                                     unused

		final ScreenSaver ss = server.getScreenSaver();

		final XInputStream inputStream = request.getInputStream();
		final int timeout = inputStream.readSignedShort();
		if (timeout == -1) {
			ss.setDefaultTimeout();
		} else if (timeout < -1) {
			response.error(ErrorCode.Value, 0);
		} else if (timeout == 0) {
			//If the timeout value is zero, screen-saver is disabled 
			ss.setEnabled(false);
			//TODO (but an activated screen-saver is not deactivated)
		} else {
			// Must be positive numbers
			ss.setEnabled(true);
			
		}
		final int interval = inputStream.readSignedShort();
		if (interval == -1) {
			ss.setDefaultInterval();
		} else if (interval < -1) {
			response.error(ErrorCode.Value, 0);
		}

		final int blanking = inputStream.readUnsignedByte();
		ss.setBlanking(blanking);
		
		final int allowExposures = inputStream.readUnsignedByte();
		ss.setAllowExposures(allowExposures);
	}
}
