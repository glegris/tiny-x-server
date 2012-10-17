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
