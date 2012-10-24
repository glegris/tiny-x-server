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

public class ListExtensions implements RequestHandler {

	Map<String, Extension> _extensionMap;

	public ListExtensions(Map<String, Extension> extensionMap) {
		this._extensionMap = extensionMap;
	}

	//	     1     1                               Reply
	//	     1     CARD8                           number of STRs in names
	//	     2     CARD16                          sequence number
	//	     4     (n+p)/4                         reply length
	//	     24                                    unused
	//	     n     LISTofSTR                       names
	//	     p                                     unused, p=pad(n)
	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {

		final XOutputStream outputStream = response.respond(1, 0);
		outputStream.writeByte(1);

		// CARD8 (Number of extensions)
		outputStream.writeByte(_extensionMap.size());

		//	      io.writeShort(c.seq);  ???
		if (_extensionMap.size() > 0) {
			// CARD16 (Sequence number)
			outputStream.writeByte(0); 
			outputStream.writeByte(client.getClientId()); // TODO: Work out what the sequence number is

			byte[] buf=new byte[1024]; //c.bbuffer;
			byte[] b;
			int n=0;

			for (String extensionName : _extensionMap.keySet()) {
				b=extensionName.getBytes();
				buf[n]=(byte)b.length; n++;
				System.arraycopy(b, 0, buf, n, b.length); n+=b.length;
			}

			outputStream.writeInt((n+3)/4);

			for (int i=0; i < 24; i++) {
				outputStream.writeByte(0);
			}

			outputStream.write(buf, 0, n);
					
			if(((-n)&3)>0){
				int a = (-n)&3;
				for (int i=0; i < a; i++) {
					outputStream.writeByte(0);
				}					
			}

		} else {
			outputStream.writeByte(0);
			for (int i=0; i < 24; i++) {
				outputStream.writeByte(0);
			}
		}
	}

}
