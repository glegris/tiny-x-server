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
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		
		final int cursorId = inputStream.readInt();

		final int sourceFont = inputStream.readInt();
		final Resource sourceFontRes = server.getResources().get(sourceFont);

		final int maskFont = inputStream.readInt();

		// TODO 
		// For 2-byte matrix fonts, the 16-bit value should be formed with the byte1 member in the most-significant byte and the byte2 member in the least-significant byte.
		final int sourceChar = inputStream.readUnsignedShort();
		final int maskChar = inputStream.readUnsignedShort();

		final int foregroundRed = inputStream.readUnsignedShort();
		final int foregroundGreen = inputStream.readUnsignedShort();
		final int foregroundBlue = inputStream.readUnsignedShort();

		final int backgroundRed = inputStream.readUnsignedShort();
		final int backgroundGreen = inputStream.readUnsignedShort();
		final int backgroundBlue = inputStream.readUnsignedShort();

		// TODO implement
	}

}
