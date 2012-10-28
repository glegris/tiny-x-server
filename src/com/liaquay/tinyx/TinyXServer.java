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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.liaquay.tinyx.model.ColorMap;
import com.liaquay.tinyx.model.Depths;
import com.liaquay.tinyx.model.Screen;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Server.ResourceFactory;
import com.liaquay.tinyx.model.TrueColorMap;
import com.liaquay.tinyx.model.Visual;
import com.liaquay.tinyx.model.Visual.BackingStoreSupport;
import com.liaquay.tinyx.model.Visual.VisualClass;
import com.liaquay.tinyx.sockets.SocketServer;
import com.liaquay.tinyx.sockets.SocketServer.Listener;

public class TinyXServer {

	public interface ClientFactory {
		public Runnable createClient(final InputStream inputStream, final OutputStream outputStream) throws IOException;
	}

	private final SocketServer _socketServer;

	public TinyXServer(final int port, final ClientFactory clientFactory) throws IOException {
		_socketServer = new SocketServer(port, new Listener() {			
			@Override
			public boolean connected(final Socket socket) {
				new Thread() {
					public void run() {
						try {
							final InputStream inputStream = socket.getInputStream();
							final OutputStream outputStream = socket.getOutputStream();
							final Runnable client = clientFactory.createClient(inputStream, outputStream);
							if(client != null) client.run();
						}
						catch(final Exception e) {
							e.printStackTrace();
						}
						finally {
							try {
								socket.close();
							} catch (IOException e) {
							}
						}
					}
				}.start();
				return true;
			}
		});
	}

	private void listen() throws IOException {
		_socketServer.listen();
	}

	/**
	 * Just an example of how to configure a server.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {

		// Create a new server
		final Server server = new Server();

		// Configure the new server

		final Visual visual = server.createVisual(new ResourceFactory<Visual>() {
			@Override
			public Visual create(final int resourceId) {
				return new Visual(
						resourceId, 
						32,
						BackingStoreSupport.BackingStoreAlways,
						VisualClass.TrueColor, 
						8,  // Bits Per RGB
						256, // TODO How do colour maps relate to visuals
						0x000000ff, // Red mask
						0x0000ff00, // Green mask
						0x00ff0000  // Blue mask
						);
			}
		});

		final Depths depths = new Depths();
		depths.add(visual);   

		final ColorMap defaultColorMap = server.createColorMap(new ResourceFactory<ColorMap>() {
			@Override
			public ColorMap create(final int resourceId) {
				return new TrueColorMap(resourceId);
			}
		});

		server.addScreen(new ResourceFactory<Screen>() {
			@Override
			public Screen create(final int resourceId) {
				return new Screen(
						resourceId, 
						defaultColorMap,
						visual,
						32,
						1280,
						800,
						1280,
						800,
						depths);
			}
		});

		final TinyXServer tinyXServer = new TinyXServer(6001, new ConnectionFactory(server));
		tinyXServer.listen();
	}
}
