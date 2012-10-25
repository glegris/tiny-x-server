package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class ChangeWindowAttributes implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {
		// TODO logging
		System.out.println(String.format("ERROR: unimplemented request request code %d, data %d, length %d, seq %d", 
				request.getMajorOpCode(), 
				request.getData(),
				request.getLength(),
				request.getSequenceNumber()));		
	}

}
