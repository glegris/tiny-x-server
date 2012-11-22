package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
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


		// Response
		final XOutputStream outputStream = response.respond(1);

//		Query our fonts DB
//		server.getFontFactory().getFontNames()
		
		int numberOfMatches = 1;
		outputStream.writeShort(numberOfMatches);

		outputStream.writePad(22);


		for (int i = 0; i < (numberOfMatches < maxNames ? numberOfMatches : maxNames); i++) {
			byte[] name = "test-font".getBytes();

			outputStream.writeByte(name.length);
			outputStream.write(name, 0, name.length);
		}
	}

}
