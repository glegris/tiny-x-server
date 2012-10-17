package com.liaquay.tinyx;

import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class Connection implements Runnable {

	private final Client _client;
	private final Server _server;
	private final RequestAdaptor _request;
	private final ResponseAdaptor _response;
	private final RequestHandler _requestHandler;
	
	public Connection(final XInputStream inputStream,
			           final XOutputStream outputStream,
			           final Server server,
			           final Client client,
			           final RequestHandler requestHandler) {
		
		_server = server;
		_client = client;
		_request = new RequestAdaptor(inputStream);
		_response = new ResponseAdaptor(outputStream);
		_requestHandler = requestHandler;
	}
	
	public void run() {
		
		try {
			while(true) {
				_request.readRequest();
				_response.setRequest(_request);
				_requestHandler.handleRequest(_server, _client, _request, _response);
				_request.skipRemaining();
				_response.send();
			}
		}
		catch(final Exception e) {
			
		}
		finally {
			_server.freeClient(_client);
		}
	}
}
