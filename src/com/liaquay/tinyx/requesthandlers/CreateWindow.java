package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Visual;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.Window.WindowClass;
import com.liaquay.tinyx.requesthandlers.winattribhandlers.WindowAttributeHandlers;

public class CreateWindow implements RequestHandler {

	private final WindowAttributeHandlers _attributeHandlers;
	
	public CreateWindow(final WindowAttributeHandlers attributeHandlers){
		_attributeHandlers = attributeHandlers;
	}
	
	@Override
	public void handleRequest(final Server server, 
			                   final Client client, 
			                   final Request request, 
			                   final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int depth = request.getData();
		final int windowResourceId = inputStream.readInt();
		final int parentWindowResourceId = inputStream.readInt();
		final int x = inputStream.readSignedShort();
		final int y = inputStream.readSignedShort();
		final int width = inputStream.readUnsignedShort();
		final int height = inputStream.readUnsignedShort();
		final int borderWidth = inputStream.readUnsignedShort();
		final int windowClassIndex = inputStream.readUnsignedShort();
		final int visualResourceId = inputStream.readInt();
		final int attributeMask = inputStream.readInt();
		
		final WindowClass[] windowClasses = WindowClass.values();
		if(windowClassIndex < 0 || windowClassIndex >= windowClasses.length) {
			response.error(Response.ErrorCode.Value, windowClassIndex);
			return;
		}
		final WindowClass windowClass = windowClasses[windowClassIndex];
		
		final Window parentWindow = server.getResources().get(parentWindowResourceId, Window.class);
		if(parentWindow == null) {
			response.error(Response.ErrorCode.Window, parentWindowResourceId);	
			return;
		}
		
		final Visual visual = visualResourceId == 0 ? 
				parentWindow.getVisual() :       
				server.getResources().get(visualResourceId, Visual.class);
		
		final Window window = new Window(
				windowResourceId,
				parentWindow,
				visual,
				depth,
				width,
				height,
				x,
				y,
				borderWidth,
				windowClass);
		
		_attributeHandlers.read(inputStream, window, attributeMask);
		
		server.getResources().add(window);
	}

}
