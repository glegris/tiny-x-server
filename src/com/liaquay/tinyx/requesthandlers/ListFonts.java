/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Nathan Ludkin
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

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.FontFactory;
import com.liaquay.tinyx.model.FontMatch;
import com.liaquay.tinyx.model.Server;

public class ListFonts implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server,
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();

		// Max names defines how many names should be returned by the server
		final int maxNames = inputStream.readUnsignedShort();

		// The pattern to search for fonts based on
		final String pattern = inputStream.readString();

		final FontFactory fontFactory = server.getFontFactory();
		
		// Query our fonts registry
		final List<FontMatch> matches = fontFactory.getMatchingFonts(pattern);
		
		// Response
		final XOutputStream outputStream = response.respond(1);

		final int numberOfMatches = matches.size() > maxNames ? maxNames : matches.size();
		outputStream.writeShort(numberOfMatches);

		response.padHeader();

		int counter = 1;
		for (final FontMatch currentFont : matches) {
			outputStream.writeString(currentFont.getMergedFontName());

			if (counter >= maxNames)
				break;

			counter++;
		}
	}
}
