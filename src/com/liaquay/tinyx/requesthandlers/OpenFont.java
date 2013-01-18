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
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.FontAlias;
import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.font.FontDetail;

public class OpenFont implements RequestHandler {
	
	private final static Logger LOGGER = Logger.getLogger(OpenFont.class.getName());

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int fid = inputStream.readInt();
		final String requestedFontName = inputStream.readString();
		final List<String> patterns = new ArrayList<String>();
		patterns.add(requestedFontName);
		for(final FontAlias alias : server.getFontAliases(requestedFontName)) {
			patterns.add(alias.getPattern());
		}
		FontInfo fontInfo = null;
		for(final String pattern : patterns) {
			fontInfo = server.getFontFactory().getFirstMatchingFont(pattern);
			if(fontInfo != null) break;
		}
		if(fontInfo == null) fontInfo = server.getFixedFontInfo();
		if(fontInfo == null) {
			final String message = "Could not open fixed font";
			LOGGER.log(Level.SEVERE, message);
			throw new RuntimeException(message);
		}
		final FontDetail fontDetail = server.getFontFactory().getFontDetail(fontInfo);
		final Font font = new Font(fid, fontInfo, fontDetail);			
		server.openFont(font);
	}
}
