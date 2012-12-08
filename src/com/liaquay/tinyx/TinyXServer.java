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
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.sockets.SocketServer;
import com.liaquay.tinyx.sockets.SocketServer.Listener;

public class TinyXServer {

	private final static Logger LOGGER = Logger.getLogger(TinyXServer.class.getName());

	public interface Executable {
		public void run();
		public void stop();
	}

	public interface ClientFactory {
		public Executable createClient(final InputStream inputStream, final OutputStream outputStream, final InetAddress address) throws IOException;
	}

	private final SocketServer _socketServer;
	private final Set<Executable> _executables = new HashSet<Executable>();

	public TinyXServer(final int port, final Server server) throws IOException {
		final ClientFactory clientFactory = new ConnectionFactory(server);

		_socketServer = new SocketServer(port, new Listener() {			
			@Override
			public boolean connected(final Socket socket) {
				new Thread() {
					public void run() {
						try {
							final InputStream inputStream = socket.getInputStream();
							final OutputStream outputStream = socket.getOutputStream();
							final InetAddress address = socket.getInetAddress();
							
							final Executable client = clientFactory.createClient(inputStream, outputStream, address);
							if(client != null) {
								synchronized (_executables) {
									_executables.add(client);
								}
								try {
									client.run();
								}
								finally {
									synchronized (_executables) {
										_executables.add(client);
									}
									try {
										client.run();
									}
									finally {
										synchronized (_executables) {
											_executables.remove(client);
										}
									}
								}
							}
						}
						catch(final Exception e) {
							// TODO this issues an error even on a normal exit.
							LOGGER.log(Level.SEVERE, "Connection failed", e);
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

			@Override
			public void exited() {
				final List<Executable> _executablesRemaining;

				synchronized (_executables) {
					_executablesRemaining = new ArrayList<Executable>(_executables);
				}

				for(final Executable executable : _executablesRemaining) {
					executable.stop();
				}
			}
		});
	}

	public void listen() throws IOException {
		_socketServer.listen();
	}

	public void close() {
		_socketServer.close();
	}
}
