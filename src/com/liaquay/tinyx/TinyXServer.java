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
	
	public static void main(final String[] args) throws IOException {
		final TinyXServer tinyXServer = new TinyXServer(6001, new ConnectionFactory());
		tinyXServer.listen();
	}
}
