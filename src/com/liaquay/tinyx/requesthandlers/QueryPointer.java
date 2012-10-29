package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.Response.ErrorCode;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Pointer;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class QueryPointer implements RequestHandler {


	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {

		final XInputStream is = request.getInputStream();

		// Get the window that we are requesting the pointer for.
		final int windowId = is.readInt();
		final Window window = server.getResources().get(windowId, Window.class);

		// Send a bad window error
		if (window == null) {
			response.error(ErrorCode.Window, windowId);
		}
		
		// Create the response
		XOutputStream os = response.respond(1, 24);
		final Pointer p = window.getPointer();

		os.writeInt(windowId);
		os.writeInt(0);						//TODO: Fix child window information
		os.writeShort(p.getX());
		os.writeShort(p.getY());
		os.writeShort(window.getScreen().getWidthPixels() - p.getX());
		os.writeShort(window.getScreen().getHeightPixels() - p.getY());
		os.writeShort(p.getState());

		os.writePad(6);
	}

}
