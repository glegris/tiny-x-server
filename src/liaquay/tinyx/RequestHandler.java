package com.liaquay.tinyx;

import java.io.IOException;

import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public interface RequestHandler {
	public void handleRequest(final Server server,
	                           final Client client,
	                           final Request request, 
	                           final Response response) throws IOException;
}
