package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;
import java.util.Map;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.extensions.Extension;

public class QueryExtension implements RequestHandler {


	Map<String, Extension> _extensionMap;

	public QueryExtension(Map<String, Extension> extensionMap) {
		this._extensionMap = extensionMap;
	}

	//		Handled by the response.respond method
	//    1     1                               Reply
	//    1                                     unused
	//    2     CARD16                          sequence number
	//    4     0                               reply length
	
	
	//    1     BOOL                            present
	//    1     CARD8                           major-opcode
	//    1     CARD8                           first-event
	//    1     CARD8                           first-error
	//    20                                    unused
	@Override
	public void handleRequest(final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final String requestedExtensionName = request.getInputStream().readString();

		System.out.println("Extension query for " + requestedExtensionName);

//		final Integer extensionOpCode = _extensionMap.get(requestedExtensionName);

		final XOutputStream outputStream = response.respond(1, 24);

		for (String extensionName : _extensionMap.keySet()) {
			if	(extensionName.equals(requestedExtensionName)){
				Extension ext = _extensionMap.get(extensionName);
				

				outputStream.writeByte(1);  // true

				outputStream.writeByte(131); // Made up type, comes from the extension
				//					if(extension.eventcount==0) 
				outputStream.writeByte(1);
				//					else 
				//						io.writeByte(ext[i].eventbase);
				//					if(ext[i].errorcount==0) 
				outputStream.writeByte(1);
				//					else 
				//						io.writeByte(ext[i].errorbase);
				outputStream.writePad(20);

				ext.dispatch(outputStream);
				
				return;
			}
		}

		outputStream.writeByte(0);  // false
		outputStream.writeByte(0);
		outputStream.writeByte(0);
		outputStream.writeByte(0);
		outputStream.writePad(20);
	}

}
