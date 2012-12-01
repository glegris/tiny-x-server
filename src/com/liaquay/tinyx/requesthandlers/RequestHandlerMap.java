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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Extension;
import com.liaquay.tinyx.model.Extensions;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.requesthandlers.extensions.BigRequestHandler;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.GraphicsContextAttributeHandlers;
import com.liaquay.tinyx.requesthandlers.winattribhandlers.WindowAttributeHandlers;

public class RequestHandlerMap implements RequestHandler {
	
	private final static Logger LOGGER = Logger.getLogger(RequestHandlerMap.class.getName());

	private static final Unimplemented UNIMPLEMENTED = new Unimplemented();

	private RequestHandler[] _handlers = new RequestHandler[256];

	private void addExtension(
			final Extensions extensions,
			final String extensionName, 
			final int eventCodeCount, 
			final int errorCodeCount,
			final RequestHandler requestHandler) {
		
		final Extension extension = extensions.createExtension(extensionName, errorCodeCount, eventCodeCount);
		_handlers[extension.getMajorOpCode()] = requestHandler;
	}

	public RequestHandlerMap(final Extensions extensions) {
		for(int i = 0; i < _handlers.length; ++i) {
			_handlers[i] = UNIMPLEMENTED;
		}
		
		addExtension(extensions, "BIG-REQUESTS", 0,0, new BigRequestHandler());

		final GraphicsContextAttributeHandlers graphicsContextAttributeHandlers = new GraphicsContextAttributeHandlers();
		final WindowAttributeHandlers windowAttributeHandlers = new WindowAttributeHandlers();

		_handlers[1] = new CreateWindow(windowAttributeHandlers);
		_handlers[2] = new ChangeWindowAttributes(windowAttributeHandlers);
		_handlers[3] = new GetWindowAttributes();
		_handlers[4] = new DestroyWindow();
		_handlers[5] = new DestroyWindows();
		_handlers[6] = new ChangeSaveSet();
		_handlers[7] = new ReparentWindow();
		_handlers[8] = new MapWindow();
		_handlers[9] = new MapSubwindows();
		_handlers[10] = new UnmapWindow();
		_handlers[11] = new UnmapSubwindows();
		_handlers[12] = new ConfigureWindow();
		_handlers[14] = new GetGeometry();
		_handlers[15] = new QueryTree();
		_handlers[16] = new InternAtom();
		_handlers[17] = new GetAtomName();
		_handlers[18] = new ChangeProperty();
		_handlers[19] = new DeleteProperty();
		_handlers[20] = new GetProperty();
		_handlers[21] = new ListProperties();
		_handlers[22] = new SetSelectionOwner();
		_handlers[23] = new GetSelectionOwner();
		_handlers[24] = new ConvertSelection();
		_handlers[25] = new SendEvent();
		_handlers[26] = new GrabPointer();
		_handlers[27] = new UngrabPointer();
		_handlers[28] = new GrabButton();
		_handlers[29] = new UngrabButton();
		_handlers[30] = new ChangeActivePointerGrab();
		_handlers[31] = new GrabKeyboard();
		_handlers[32] = new UngrabKeyboard();
		_handlers[33] = new GrabKey();
		_handlers[34] = new UngrabKey();
		_handlers[35] = new AllowEvents();
		_handlers[36] = new GrabServer();
		_handlers[37] = new UngrabServer();
		_handlers[38] = new QueryPointer();
		_handlers[39] = new GetMotionEvents();
		_handlers[40] = new TranslateCoordinates();
		_handlers[41] = new WarpPointer();
		_handlers[42] = new SetInputFocus();
		_handlers[43] = new GetInputFocus();
		_handlers[44] = new QueryKeymap();
		_handlers[45] = new OpenFont();
		_handlers[46] = new CloseFont();
		_handlers[47] = new QueryFont();
		_handlers[48] = new QueryTextExtents();
		_handlers[49] = new ListFonts();
		_handlers[50] = new ListFontsWithInfo();
		_handlers[51] = new SetFontPath();
		_handlers[52] = new GetFontPath();
		_handlers[53] = new CreatePixmap();
		_handlers[54] = new FreePixmap();
		_handlers[55] = new CreateGraphicsContext(graphicsContextAttributeHandlers);
		_handlers[56] = new ChangeGraphicsContext();
		_handlers[57] = new CopyGraphicsContext();
		_handlers[58] = new SetDashes();
		_handlers[59] = new SetClipRectangles();
		_handlers[60] = new FreeGraphicsContext();
		_handlers[61] = new ClearArea();
		_handlers[62] = new CopyArea();
		_handlers[63] = new CopyPlane();
		_handlers[64] = new PolyPoint();
		_handlers[65] = new PolyLine();
		_handlers[66] = new PolySegment();
		_handlers[67] = new PolyRectangle();
		_handlers[68] = new PolyArc();
		_handlers[69] = new FillPoly();
		_handlers[70] = new PolyFillRectangle();
		_handlers[71] = new PolyFillArc();
		_handlers[72] = new PutImage();
		_handlers[73] = new GetImage();
		_handlers[74] = new PolyText8();
		_handlers[75] = new PolyText16();
		_handlers[76] = new ImageText8();
		_handlers[77] = new ImageText16();
		_handlers[78] = new CreateColorMap();
		_handlers[79] = new FreeColorMap();
		_handlers[80] = new CopyColormapAndFree();
		_handlers[81] = new InstallColormap();
		_handlers[82] = new UninstallColormap();
		_handlers[83] = new ListInstalledColormaps();
		_handlers[84] = new AllocColor();
		_handlers[85] = new AllocNamedColor();
		_handlers[86] = new AllocColorCells();
		_handlers[87] = new AllocColorPlanes();
		_handlers[88] = new FreeColors();
		_handlers[89] = new StoreColors();
		_handlers[90] = new StoreNamedColor();
		_handlers[91] = new QueryColours();
		_handlers[92] = new LookupColor();
		_handlers[93] = new CreateCursor();
		_handlers[94] = new CreateGlyphCursor();
		_handlers[95] = new FreeCursor();
		_handlers[96] = new RecolorCursor();
		_handlers[97] = new QueryBestSize();
		_handlers[98] = new QueryExtension();
		_handlers[99] = new ListExtensions();
		_handlers[100] = new ChangeKeyboardMapping();
		_handlers[101] = new GetKeyboardMapping();
		_handlers[102] = new ChangeKeyboardControl();
		_handlers[103] = new GetKeyboardControl();
		_handlers[104] = new Bell();
		_handlers[105] = new ChangePointerControl();
		_handlers[106] = new GetPointerControl();
		_handlers[107] = new SetScreenSaver();
		_handlers[108] = new GetScreenSaver();
		_handlers[109] = new ChangeHosts();
		_handlers[110] = new ListHosts();
		_handlers[111] = new SetAccessControl();
		_handlers[112] = new SetCloseDownMode();
		_handlers[113] = new KillClient();
		_handlers[114] = new RotateProperties();
		_handlers[115] = new ForceScreenSaver();
		_handlers[116] = new SetPointerMapping();
		_handlers[117] = new GetPointerMapping();
		_handlers[118] = new SetModifierMapping();
		_handlers[119] = new GetModifierMapping();
		_handlers[127] = new NoOperation();
	}

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final int majorOpCode = request.getMajorOpCode();
		if(majorOpCode < 0 || majorOpCode > 255) {
			throw new RuntimeException("Impossible majorOpCode " + majorOpCode);
		}
		final RequestHandler requestHandler = _handlers[majorOpCode];
		LOGGER.log(Level.INFO, "Processing " + requestHandler.getClass().getSimpleName() + "...");
		if (requestHandler instanceof ListFonts) {
			System.out.println();
		}
		requestHandler.handleRequest(server, client, request, response);
	}
}
