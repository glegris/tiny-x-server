package com.liaquay.tinyx.requesthandlers.extensions.shape.handlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class ShapeGetRectangles implements RequestHandler {

	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {
		// TODO Auto-generated method stub
		// TODO logging
		System.out.println(String.format("ERROR: unimplemented request request code %d, data %d, length %d, seq %d", 
				request.getMajorOpCode(), 
				request.getData(),
				request.getLength(),
				request.getSequenceNumber()));		
	}

}
