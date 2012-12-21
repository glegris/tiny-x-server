package com.liaquay.tinyx.requesthandlers.extensions.shape.handlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Window;

public class ShapeMask implements RequestHandler {

	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {

		XInputStream inputStream = request.getInputStream();

		ShapeOpcode shapeOp = ShapeOpcode.getFromIndex(inputStream.readUnsignedByte());
		ShapeKind shapeKind = ShapeKind.getFromIndex(inputStream.readUnsignedByte());

		inputStream.readUnsignedShort();

		int windowId = inputStream.readInt();
		final Window window = server.getResources().get(windowId, Window.class);
		if(window == null) {
			response.error(Response.ErrorCode.Window, windowId);
			return;
		}

		int xOffset = inputStream.readSignedShort();
		int yOffset = inputStream.readSignedShort();

		int srcPixmapId = inputStream.readInt();

		if (srcPixmapId == 0) {
			//TODO Send shape notify with window dimensions..
		} else {
			final Pixmap srcPixmap = server.getResources().get(srcPixmapId, Pixmap.class);
			if(srcPixmap == null) {
				response.error(Response.ErrorCode.Pixmap, srcPixmapId);
				return;
			}
			
			//TODO Send shape notify with pixmap dimensions
		}		
	}

}
