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
package com.liaquay.tinyx.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer {
	
	private final static Logger LOGGER = Logger.getLogger(SocketServer.class.getName());
	
	public interface Listener {
		public boolean connected(final Socket socket);
		public void exited();
	}

	private final Listener _listener;
	private final ServerSocket _serverSocket;

	private boolean _open = true;

	public SocketServer(final int port, final Listener listener) throws IOException {
		_listener = listener;
		_serverSocket = new ServerSocket(port);
	}

	public void close() {
		if(_open) {
			_open = false;
			try {
				_serverSocket.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			_open = false;
		}
	}

	public void listen() throws IOException {
		try {
			while(_open) {
				final Socket client = _serverSocket.accept();
				if(!_listener.connected(client))  {
					try {
						client.close();
					}
					catch(final Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		catch(final Exception e) {
			if(_open) {
				LOGGER.log(Level.SEVERE, "Error listening on socket", e);
			}
		}
		finally {
			_listener.exited();
		}
	}

	public static void main(final String[] args) throws IOException {
		final SocketServer server = new SocketServer(6001, new Listener() {
			@Override
			public boolean connected(final Socket socket) {
				System.out.println("" + socket.getRemoteSocketAddress());
				return true;
			}

			@Override
			public void exited() {
				LOGGER.log(Level.INFO, "Socket server exiting");
			}
		});
		server.listen();
	}
}
