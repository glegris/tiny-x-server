package com.liaquay.tinyx.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	
	public interface Listener {
		public boolean connected(final Socket socket);
	}

	private final Listener _listener;
	private final ServerSocket _serverSocket;

	private boolean _open = true;

	public SocketServer(final int port, final Listener listener) throws IOException {
		_listener = listener;
		_serverSocket = new ServerSocket(port);
	}

	//	TODO Check this interrupts the listener socket.
	public void close() {
		if(_open) {
			try {
				_serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			_open = false;
		}
	}

	public void listen() throws IOException {
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

	public static void main(final String[] args) throws IOException {
		final SocketServer server = new SocketServer(6001, new Listener() {
			@Override
			public boolean connected(final Socket socket) {
				System.out.println("" + socket.getRemoteSocketAddress());
				return true;
			}
		});
		server.listen();
	}
}
