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
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.FontFactory;
import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.Server;

public class OpenFont implements RequestHandler {
	
	private final static Logger LOGGER = Logger.getLogger(OpenFont.class.getName());

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final FontFactory fontFactory = server.getFontFactory();
		final XInputStream inputStream = request.getInputStream();
		final int fid = inputStream.readInt();
		final String requestedFontName = inputStream.readString();
		FontMatch fontInfo = server.getFontFactory().getFirstMatchingFont(requestedFontName);

		if(fontInfo == null) {
			fontInfo = server.getFontFactory().getFirstMatchingFont("fixed");
		}
		
		if(fontInfo == null) {
			final String message = "Could not open fixed font";
			LOGGER.log(Level.SEVERE, message);
			throw new RuntimeException(message);
		}
		
		final FontDetail fontDetail = fontFactory.open(fontInfo);
		final Font font = new Font(fid, fontDetail, fontFactory);
		server.openFont(font);
	}
}
