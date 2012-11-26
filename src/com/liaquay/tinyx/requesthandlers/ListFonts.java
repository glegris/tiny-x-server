package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;
import java.util.List;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.FontString;
import com.liaquay.tinyx.model.Server;

public class ListFonts implements RequestHandler {

	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();

		// Max names defines how many names should be returned by the server
		final int maxNames = inputStream.readUnsignedShort();

		// The pattern to search for fonts based on
		final String pattern = inputStream.readString();

		// Query our fonts registry
		List<FontString> matches = server.getFontFactory().getMatchingFonts(pattern);
		
		int length = 0;
		for (FontString match : matches) {
			length+=match.toString().length();
		}

		// Response
		final XOutputStream outputStream = response.respond(1);//, (length+3)/4);

		final int numberOfMatches = matches.size() > maxNames ? maxNames : matches.size();
		outputStream.writeShort(numberOfMatches);

		outputStream.writePad(22);

		int counter = 1;
		for (FontString currentFont : matches) {
			byte[] name = currentFont.toString().getBytes();

			outputStream.writeByte(name.length);
			outputStream.write(name, 0, name.length);

			if (counter >= maxNames)
				break;
			
			counter++;
		}
		
		outputStream.writePad((-length)&3);
	}

}
