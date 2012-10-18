package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.Response.ErrorCode;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.Server;

public class CreateGraphicsContext implements RequestHandler {

	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int graphicsContextId = inputStream.readInt();
		final int drawableResourdeId = inputStream.readInt();
		final Drawable drawable = client.getClientResources().get(drawableResourdeId, Drawable.class);
		if(drawable == null) {
			response.error(Response.ErrorCode.Drawable, drawableResourdeId);
		}
		else {
			
		}
	}

}
                                                                                                                                                                                                                         