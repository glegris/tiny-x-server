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
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.font.FontDetail;

public class OpenFont implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int fid = inputStream.readInt();
		final String requestedFontName = inputStream.readString();

		final FontInfo pattern;
		final FontInfo fixedFontInfo = server.getFontInfoFromAlias("fixed");
		if(requestedFontName.startsWith("-")) {
			pattern = new FontInfo(requestedFontName);
		}
		else {
			final FontInfo aliasFontInfo = server.getFontInfoFromAlias(requestedFontName);
			pattern = aliasFontInfo == null ? fixedFontInfo : aliasFontInfo;
		}
		final Font font;
		final FontInfo fontInfo;
		final FontInfo matchingFontInfo = server.getFontFactory().getFirstMatchingFont(pattern);
		if(matchingFontInfo == null) {
			fontInfo = server.getFontFactory().getFirstMatchingFont(fixedFontInfo);
		}
		else {
			fontInfo = matchingFontInfo;
		}
		
		if(fontInfo == null) {
			// TODO Log error
			throw new RuntimeException("Could not find fixed font.");
		}
		else {
			final FontInfo mergedFontInfo = fontInfo.merge(pattern);
			final FontDetail fontDetail = server.getFontFactory().getFontDetail(mergedFontInfo);
			font = new Font(fid, mergedFontInfo, fontDetail);			
		}

		server.openFont(font);
	}
}
