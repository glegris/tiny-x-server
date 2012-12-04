package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;
import java.util.List;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.FontString;
import com.liaquay.tinyx.model.Server;

public class ListFontsWithInfo implements RequestHandler {

	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();

		int maxNames = inputStream.readUnsignedShort();

		String pattern = inputStream.readString();

		System.out.println("Max names: " + maxNames + "  Pattern: " + pattern);

		
		XOutputStream outputStream = response.respond(1);
		
		List<FontString> fonts = server.getFontFactory().getMatchingFonts(pattern);

		for (FontString font : fonts) {
			
			final Font f= new Font(1, font, server.getFontFactory());
			FontHandler.writeFontInfo(server, f, outputStream);
			
		}

		//TODO: I need some help on this one Phil... This response is scary!
		
//		▶ (last in series)
//	     1     1                               Reply
//	     1     0                               last-reply indicator
//	     2     CARD16                          sequence number
//	     4     7                               reply length
//	     52                                    unused
	}
}
