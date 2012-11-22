package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Resource;
import com.liaquay.tinyx.model.Server;

public class CreateGlyphCursor implements RequestHandler {

	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {
		// TODO Auto-generated method stub


		final XInputStream inputStream = request.getInputStream();
		
		int cursorId = inputStream.readInt();

		int sourceFont = inputStream.readInt();
		Resource sourceFontRes = server.getResources().get(sourceFont);

		int maskFont = inputStream.readInt();

		int sourceChar = inputStream.readUnsignedShort();
		int maskChar = inputStream.readUnsignedShort();

		int foregroundRed = inputStream.readUnsignedShort();
		int foregroundGreen = inputStream.readUnsignedShort();
		int foregroundBlue = inputStream.readUnsignedShort();

		int backgroundRed = inputStream.readUnsignedShort();
		int backgroundGreen = inputStream.readUnsignedShort();
		int backgroundBlue = inputStream.readUnsignedShort();

	}

}
