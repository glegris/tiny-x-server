package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Cursor;
import com.liaquay.tinyx.model.Font;
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

		final int sourceFontId = inputStream.readInt();
		final Font sourceFont = server.getResources().get(sourceFontId, Font.class);
		if(sourceFont == null) {
			response.error(Response.ErrorCode.Font, sourceFontId);
			return;
		}
		
		final int maskFontId = inputStream.readInt();
		final Font maskFont;
		if(maskFontId == 0) { 
			maskFont = null;
		}
		else {
			maskFont = server.getResources().get(maskFontId, Font.class);
			if(maskFont == null) {
				response.error(Response.ErrorCode.Font, maskFontId);
				return;
			}
		}
		
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
		
		
		// TODO create pixmap and associate with the cursor
		
		// TODO logging
		System.out.println(String.format("ERROR: unimplemented request request code %d, data %d, length %d, seq %d", 
				request.getMajorOpCode(), 
				request.getData(),
				request.getLength(),
				request.getSequenceNumber()));		
		
		final Cursor cursor = new Cursor(cursorId);
		server.getResources().add(cursor);
		
		cursor.setForegroundColorRed(foregroundRed);
		cursor.setForegroundColorGreen(foregroundGreen);
		cursor.setForegroundColorBlue(foregroundBlue);
		
		cursor.setBackgroundColorRed(backgroundRed);
		cursor.setBackgroundColorGreen(backgroundGreen);
		cursor.setBackgroundColorBlue(backgroundBlue);
	}

}
