package com.liaquay.tinyx.requesthandlers.extensions;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class BigRequestHandler implements RequestHandler {

	@Override
	public void handleRequest(Server server, Client client,
			Request request, Response response) throws IOException {
		
		final XOutputStream outputStream = response.respond(1, 24);
		outputStream.writeInt(server.getMaximumRequestLength());
	}
}
