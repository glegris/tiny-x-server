/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.extensions.BigRequestsExtension;
import com.liaquay.tinyx.model.extensions.Extension;
import com.liaquay.tinyx.requesthandlers.extensions.BigRequestHandler;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.GraphicsContextAttributeHandlers;
import com.liaquay.tinyx.requesthandlers.winattribhandlers.WindowAttributeHandlers;

public class RequestHandlerMap implements RequestHandler {

	private static final Unimplemented UNIMPLEMENTED = new Unimplemented();

	private RequestHandler[] _handlers = new RequestHandler[256];

	private static int EXTENSION_BASE = 128;
	private static int EXTENSION_EVENT_BASE = 64;

	private int _currentExtension = 128;
	private Map<String, Extension> _extensionMap = new TreeMap<String, Extension>();

	private void addExtension(final String extensionName, final int numEvents, final int numErrors, final int minorOp, final Extension extension) {
		//		final int extensionOpCode = _currentExtension++;
		//		if(extensionOpCode > 255) {
		//			throw new RuntimeException("Too many extensions");
		//		}
		_extensionMap.put(extensionName, extension);
//		_handlers[EXTENSION_BASE+minorOp] = requestHandler;
	}

	public RequestHandlerMap() {
		for(int i = 0; i < _handlers.length; ++i) {
			_handlers[i] = UNIMPLEMENTED;
		}
		
		//		addExtension("RANDR", 0, 139, 72,0, null);
		addExtension("BIG-REQUESTS2", 0,0,0, new BigRequestsExtension(new BigRequestHandler()));


		final GraphicsContextAttributeHandlers graphicsContextAttributeHandlers = new GraphicsContextAttributeHandlers();
		final WindowAttributeHandlers windowAttributeHandlers = new WindowAttributeHandlers();

		_handlers[1] = new CreateWindow(windowAttributeHandlers);
		_handlers[2] = new ChangeWindowAttributes();
		_handlers[3] = new GetWindowAttributes();
		_handlers[4] = new DestroyWindow();
		_handlers[5] = new DestroyWindows();
		_handlers[6] = new ChangeSaveSet();
		_handlers[8] = new MapWindow();
		_handlers[9] = new MapSubwindows();
		_handlers[10] = new UnmapWindow();
		_handlers[11] = new UnmapSubwindows();
		_handlers[14] = new GetGeometry();
		_handlers[16] = new InternAtom();
		_handlers[17] = new GetAtomName();
		_handlers[18] = new ChangeProperty();
		_handlers[20] = new GetProperty();
		_handlers[21] = new ListProperties();
		_handlers[38] = new QueryPointer();
		_handlers[43] = new GetInputFocus();
		_handlers[53] = new CreatePixmap();
		_handlers[54] = new FreePixmap();
		_handlers[55] = new CreateGraphicsContext(graphicsContextAttributeHandlers);
		_handlers[60] = new FreeGraphicsContext();
		_handlers[72] = new PutImage();
		_handlers[78] = new CreateColorMap();
		_handlers[91] = new QueryColours();
		_handlers[97] = new QueryBestSize();
		_handlers[98] = new QueryExtension(_extensionMap);
		_handlers[99] = new ListExtensions(_extensionMap);
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
