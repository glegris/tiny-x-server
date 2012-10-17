package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.Response.ReplyCode;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class RequestHandlerMap implements RequestHandler {
	
	private static final Unimplemented UNIMPLEMENTED = new Unimplemented();
	
	private RequestHandler[] _handlers = new RequestHandler[256];
	
	private class QueryExtension implements RequestHandler {

		@Override
		public void handleRequest(final Server server, 
				                   final Client client, 
				                   final Request request, 
				                   final Response response) throws IOException {
			
			final String extensionName = request.getInputStream().readString();
			
			System.out.println("Extension query for " + extensionName);
			
			final Integer extensionOpCode = _extensionNameToOpCode.get(extensionName);
			final XOutputStream outputStream = response.respond(ReplyCode.Ok, 1, 0);
			
			if(extensionOpCode == null) {
				// Extension not installed
				outputStream.writeByte(0);
			}
			else {
				// This extension is available				
				outputStream.writeByte(extensionOpCode);
				
//				  if(ext[i].eventcount==0) io.writeByte(0);
//				  else io.writeByte(ext[i].eventbase);
//				  if(ext[i].errorcount==0) io.writeByte(0);
//				  else io.writeByte(ext[i].errorbase);
			}
			
			outputStream.writePad(32 - outputStream.getCounter());
		}
    }
	
	private int _currentExtension = 128;
	private Map<String, Integer> _extensionNameToOpCode = new TreeMap<String, Integer>();
	
	private void addExtension(final String extensionName, final RequestHandler requestHandler) {
		final int extensionOpCode = _currentExtension++;
		if(extensionOpCode > 255) {
			throw new RuntimeException("Too many extensions");
		}
		_extensionNameToOpCode.put(extensionName, extensionOpCode);
		_handlers[extensionOpCode] = requestHandler;
	}
	
	public RequestHandlerMap() {
		for(int i = 0; i < _handlers.length; ++i) {
			_handlers[i] = UNIMPLEMENTED;
		}
		
		// TODO use constants
		_handlers[98] = new QueryExtension();
	}

	@Override
	public void handleRequest(final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {
		
		final int majorOpCode = request.getMajorOpCode();
		if(majorOpCode < 0 || majorOpCode > 255) {
			throw new RuntimeException("Impossible majorOpCode " + majorOpCode);
		}
		final RequestHandler requestHandler = _handlers[majorOpCode];
		
		requestHandler.handleRequest(server, client, request, response);
	}
}
