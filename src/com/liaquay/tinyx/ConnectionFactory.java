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
package com.liaquay.tinyx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.liaquay.tinyx.io.LsbXInputStream;
import com.liaquay.tinyx.io.LsbXOutputStream;
import com.liaquay.tinyx.io.MsbXInputStream;
import com.liaquay.tinyx.io.MsbXOutputStream;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Depths;
import com.liaquay.tinyx.model.Depths.Depth;
import com.liaquay.tinyx.model.Format;
import com.liaquay.tinyx.model.Resource;
import com.liaquay.tinyx.model.Screen;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Visual;
import com.liaquay.tinyx.requesthandlers.RequestHandlerMap;

public class ConnectionFactory implements TinyXServer.ClientFactory {
		
	private final Server _server = new Server();
	private final RequestHandler _requestHandler = new RequestHandlerMap();
	
	private static void writeFormat(final XOutputStream out, final Format format) throws IOException {
	    out.writeByte(format.getDepth());
	    out.writeByte(format.getBpp());
	    out.writeByte(format.getScanLinePad());
	    out.writePad(5);
	}
	
	private void writeVendor(final XOutputStream out)  throws IOException {
		out.write(_server.getVendor(), 0, _server.getVendor().length);
		out.writePad(-_server.getVendor().length & 3);
	}
	
	private void writeProlog(final XOutputStream out, final int clientId) throws IOException {
		
		final ByteArrayOutputStream extra = new ByteArrayOutputStream();
		final XOutputStream extraOutputStream;
		switch(out.getByteOrder()) {
		case LSB: extraOutputStream = new LsbXOutputStream(extra);break;
		case MSB: extraOutputStream = new MsbXOutputStream(extra);break;
		default: throw new RuntimeException("Unknown byte order " + out.getByteOrder());
		}
		extraOutputStream.writeInt(0);	                      // Release number.
		extraOutputStream.writeInt(clientId << Resource.CLIENTOFFSET);   // The client id shifted into position
		extraOutputStream.writeInt(Resource.RESOURCE_ID_MASK);           // Mask of bits used as resource identifiers
		extraOutputStream.writeInt(0);		                  // Motion buffer size.
		extraOutputStream.writeShort(_server.getVendor().length);	 // Vendor length.
		extraOutputStream.writeShort(0x7fff);	                  // Max request length. TODO Really?
		extraOutputStream.writeByte(_server.getScreens().length);   // Number of screens.
		extraOutputStream.writeByte(_server.getFormats().length);   // Number of formats
		extraOutputStream.writeByte(0);	                      // Image byte order (0=LSB, 1=MSB).
		extraOutputStream.writeByte(1);	                      // Bitmap bit order (0=LSB, 1=MSB).
		extraOutputStream.writeByte(8);	                      // Bitmap format scan-line unit.
		extraOutputStream.writeByte(8);	                      // Bitmap format scan-line pad.
		extraOutputStream.writeByte(_server.getKeyboard().getMinKeyCode());// Minimum key code
		extraOutputStream.writeByte(_server.getKeyboard().getMaxKeyCode());// Maximum key code
		extraOutputStream.writePad (4);	                      // Unused.
		writeVendor(extraOutputStream);
		for(int i = 0; i < _server.getFormats().length; ++i) {
			writeFormat(extraOutputStream, _server.getFormats()[i]);
		}
		for(int i = 0; i < _server.getScreens().length; ++i) {
			writeScreen(extraOutputStream, _server.getScreens()[i]);
		}
		
		out.writeByte(1);		                  // Success.
		out.writeByte(0);		                  // Unused.
		out.writeShort(11);                       // ProtocolMajorVersion
		out.writeShort(0);                        // ProtocolMinorVersion
		out.writeShort(extra.size()>>2);	      // Length of data.

		out.write(extra.toByteArray(), 0, extra.size());
		out.send();
	}
	
	private static void writeDepth(final XOutputStream out, final Depth depth)  throws IOException {
		final Collection<Visual> visuals = depth.getVisuals();
		out.writeByte(depth.getDepth());
		out.writePad(1);
		out.writeShort(visuals.size());
		out.writePad(4);
		for (final Visual visual : visuals){
			writeVisual(out, visual);
		}
	}
	
	private static void writeDepths(final XOutputStream out, final Depths depths) throws IOException {
		final Collection<Depth> values = depths.values();
	    out.writeByte(values.size());
	    for (final Depth depth : values) {
	      writeDepth(out, depth);
	    }
	}
	
	private static void writeVisual(final XOutputStream out, final Visual visual) throws IOException {
		out.writeInt (visual.getId());		// Visual ID.
		out.writeByte (visual.getVisualClass().ordinal());	// Class.
		out.writeByte (visual.getBitsPerRGB());	// Bits per RGB value.
		out.writeShort (visual.getColormapEntries());	// Colour-map entries.
		out.writeInt (visual.getRedMask());	// Red mask.
		out.writeInt (visual.getGreenMask());	// Green mask.
		out.writeInt (visual.getBlueMask());	// Blue mask.
		out.writePad (4);	// Unused.	
	}
	
	private void writeScreen(final XOutputStream out, final Screen screen)  throws IOException {
		out.writeInt(screen.getRootWindow().getId ());		// Root window ID.
		out.writeInt(screen.getDefaultColorMap().getId ());	// Default colour-map ID.
		out.writeInt(screen.getDefaultColorMap().getWhitePixel ());	// White pixel.
		out.writeInt(screen.getDefaultColorMap().getBlackPixel ());	// Black pixel.
		out.writeInt(0);	// Current input masks. // TODO Almost certainly wrong!
		out.writeShort(screen.getWidthPixels ());	// Width in pixels.
		out.writeShort(screen.getHeightPixels ());	// Height in pixels.
		out.writeShort(screen.getWidthMM());	// Width in millimeters.
		out.writeShort(screen.getHeightMM());	// Height in millimeters.
		out.writeShort(screen.getMinInstalledMaps());	// Minimum installed maps.
		out.writeShort(screen.getMaxInstalledMaps());	// Maximum installed maps.
		out.writeInt(screen.getRootVisual().getId ());	// Root visual ID.
		out.writeByte(screen.getRootVisual().getBackingStoreSupport().ordinal()); // TODO does this belong on a visual?
		out.writeByte ((byte) (screen.getRootVisual().getSaveUnder () ? 1 : 0));// TODO does this belong on a visual?
		out.writeByte (screen.getRootDepth ());	// Root depth.
		writeDepths(out, screen.getDepths());
	}
	
	@Override
	public Runnable createClient(final InputStream inputStream, final OutputStream outputStream) throws IOException {
		
		final XInputStream xinputStream;
		final XOutputStream xoutputStream;

		final int byteOrderCode = inputStream.read();
		switch(byteOrderCode) {
		case 0x6c:// LSB
			xinputStream = new LsbXInputStream(inputStream);
			xoutputStream = new LsbXOutputStream(outputStream);
			break;
		case 0x42:// MSB
			xinputStream = new MsbXInputStream(inputStream);
			xoutputStream = new MsbXOutputStream(outputStream);
			break;
		default:
			// Protocol Error
			return null;
		}				
		
		// Read the prolog
		xinputStream.readUnsignedByte(); 
		xinputStream.readUnsignedShort();
		xinputStream.readUnsignedShort();
		final int nameLength = xinputStream.readUnsignedShort();
		final int dataLength = xinputStream.readUnsignedShort(); 
		final byte[] name = new byte[nameLength];
		final byte[] data = new byte[dataLength];
		xinputStream.skip(2);
		if(nameLength>0){
			xinputStream.read(name, 0, nameLength);
			xinputStream.skip((-nameLength)&3);
		}
		if(dataLength>0){
			xinputStream.read(data, 0, dataLength);
			xinputStream.skip((-dataLength)&3);
		}			
		
		final Client client = _server.allocateClient();
		if(client == null) {
			// TODO we could not allocate a new client!
			return null;
		}
		else {
			// Send the opening response
			writeProlog(xoutputStream, client.getClientId());
			
			return new Connection(xinputStream, xoutputStream, _server, client, _requestHandler);
		}
	}
}
