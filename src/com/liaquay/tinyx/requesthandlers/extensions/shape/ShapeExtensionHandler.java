package com.liaquay.tinyx.requesthandlers.extensions.shape;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.requesthandlers.extensions.shape.handlers.ShapeCombine;
import com.liaquay.tinyx.requesthandlers.extensions.shape.handlers.ShapeGetRectangles;
import com.liaquay.tinyx.requesthandlers.extensions.shape.handlers.ShapeMask;
import com.liaquay.tinyx.requesthandlers.extensions.shape.handlers.ShapeOffset;
import com.liaquay.tinyx.requesthandlers.extensions.shape.handlers.ShapeQuery;
import com.liaquay.tinyx.requesthandlers.extensions.shape.handlers.ShapeQueryExtents;
import com.liaquay.tinyx.requesthandlers.extensions.shape.handlers.ShapeRectangles;
import com.liaquay.tinyx.requesthandlers.extensions.shape.handlers.ShapeSelectInput;

public class ShapeExtensionHandler implements RequestHandler {


	private final static Logger LOGGER = Logger.getLogger(ShapeExtensionHandler.class.getName());

	Map<Integer, RequestHandler> _handlers = new HashMap<Integer, RequestHandler>();

	public ShapeExtensionHandler() {
		_handlers.put(0, new ShapeQuery());
		_handlers.put(1, new ShapeRectangles());
		_handlers.put(2, new ShapeMask());
		_handlers.put(3, new ShapeCombine());
		_handlers.put(4, new ShapeOffset());
		_handlers.put(5, new ShapeQueryExtents());
		_handlers.put(6, new ShapeSelectInput());
//		_handlers.put(6, new ShapeInputSelected());
		_handlers.put(7, new ShapeGetRectangles());
	}

	@Override
	public void handleRequest(Server server, Client client, Request request,
			Response response) throws IOException {

		int minorOpcode = request.getData();

		final RequestHandler requestHandler = _handlers.get(minorOpcode);

		if (requestHandler != null) {
			LOGGER.log(Level.INFO, "SHAPE EXTENSION: Processing " + requestHandler.getClass().getSimpleName() + "... opCode: " + minorOpcode);
			requestHandler.handleRequest(server, client, request, response);
		}
	}
}
