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

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.RequestHandler;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Drawable;
import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Image;
import com.liaquay.tinyx.model.Image.ImageType;
import com.liaquay.tinyx.model.Server;

public class PutImage implements RequestHandler {

	@Override
	public void handleRequest(
			final Server server, 
			final Client client, 
			final Request request, 
			final Response response) throws IOException {

		final XInputStream inputStream = request.getInputStream();
		final int imageTypeIndex = request.getData();
		final Image.ImageType imageType = Image.ImageType.getFromIndex(imageTypeIndex);
		if(imageType == null) {
			response.error(Response.ErrorCode.Match, imageTypeIndex); // TODO is this correct?
			return;
		}
		final int drawableResourceId = inputStream.readInt();
		final Drawable destDrawable = server.getResources().get(drawableResourceId, Drawable.class);
		if(destDrawable == null) {
			response.error(Response.ErrorCode.Drawable, drawableResourceId);
			return;
		}
		final int graphicsContextResourceId = inputStream.readInt();
		final GraphicsContext graphicsContext = server.getResources().get(graphicsContextResourceId, GraphicsContext.class);
		if(graphicsContext == null) {
			response.error(Response.ErrorCode.GContext, graphicsContextResourceId);
			return;
		}	          

		final int width = inputStream.readUnsignedShort();
		final int height = inputStream.readUnsignedShort();
		final int destinationX = inputStream.readSignedShort();
		final int destinationY = inputStream.readSignedShort();
		final int leftPad = inputStream.readUnsignedByte();
		final int depth = inputStream.readUnsignedByte();
		inputStream.skip(2);

		// TODO This test does not seem correct (Weird X doesn't do this check!)
		//		if(depth != drawable.getDepth() && (imageType.equals(ImageType.XYPixmap) || imageType.equals(ImageType.ZPixmap))) {
		//			response.error(Response.ErrorCode.Match, drawableResourceId);/
		//			return;
		//		}

		// Image must also be in XY Format?
		if (imageType.equals(ImageType.BitMap) && depth != 1) {
			response.error(Response.ErrorCode.Match, drawableResourceId); //TODO??
		}

		// Read image data...
		final int remainingBytes = request.getLength() - inputStream.getCounter() ;
		byte[] buffer = new byte[remainingBytes];
		int bytesRead = inputStream.read(buffer, 0, remainingBytes);
		while (bytesRead < remainingBytes) {
			bytesRead+=inputStream.read(buffer, bytesRead, buffer.length - bytesRead);
		}

		destDrawable.putImage(graphicsContext, imageType, buffer, width, height, destinationX, destinationY, leftPad, depth);
	}
}
