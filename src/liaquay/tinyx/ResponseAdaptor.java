package com.liaquay.tinyx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.liaquay.tinyx.io.LsbXOutputStream;
import com.liaquay.tinyx.io.MsbXOutputStream;
import com.liaquay.tinyx.io.XOutputStream;

public class ResponseAdaptor implements Response {

	private final ByteArrayOutputStream _extra = new ByteArrayOutputStream();
	private final XOutputStream _extraOutputStream;
	private final XOutputStream _outputStream;

	private boolean _headerSent = false;
	private boolean _prepared = false;
	private boolean _sent = false;
	private Request _request = null;
	
	public ResponseAdaptor(final XOutputStream outputStream) {
		_outputStream = outputStream;
		
		switch(outputStream.getByteOrder()) {
		case LSB: _extraOutputStream = new LsbXOutputStream(_extra);break;
		case MSB: _extraOutputStream = new MsbXOutputStream(_extra);break;
		default: throw new RuntimeException("Unknown byte order " + outputStream.getByteOrder());
		}
	}
	
	public void setRequest(final Request request) {
		// Prepare the extra buffer in case it is needed
		_extra.reset();
		
		// Indicate that the header is yet to be sent
		_headerSent = false;
		
		// Make a note of the request so the response can have the correct sequence number
		_request = request;		
		
		_prepared = false;
		
		_sent = false;

		_outputStream.resetCounter();
	}
	
	private ReplyCode _replyCode;
	private int _data;
	
	/**
	 * Defers sending header until send is called.
	 * Any extra output is buffered in memory. 
	 * 
	 * @param responseCode the response code
	 */
	@Override
	public XOutputStream respond(final ReplyCode replyCode, final int data) {
		_prepared = true;
		_replyCode = replyCode;
		_data = data;
		return _extraOutputStream;
		
	}

	
	private int _extraLength = 0;
	
	/**
	 * Send a response header along with the length of extra data.
	 * This method allows for an efficient response without double buffering...
	 * but you have to know how much extra data is to be sent a priori.
	 * 
	 * @param responseCode the response code
	 * @param extraLength the length of extra data to send in bytes
	 * @throws IOException 
	 */
	@Override
	public XOutputStream respond(final ReplyCode replyCode, final int data, final int extraLength) throws IOException {
		_prepared = true;
		_outputStream.writeByte(replyCode.getValue());
		_outputStream.writeByte(data); 
		_outputStream.writeShort(_request.getSequenceNumber() & 0xffff);
		_outputStream.writeInt(extraLength);
		_headerSent = true;
		_extraLength = extraLength;
		return _outputStream;
	}

	/**
	 * Deliver the message to the client.
	 */
	public void send() throws IOException {
		if(_sent) {
			throw new IOException("Attempt made to re-send a response");			
		}
		_sent = true;
		
		if(!_prepared) {
			return;
		}
		
		if(_headerSent) {
			if(_outputStream.getCounter() != 32 + _extraLength) throw new IOException("Response message not correct length");
			_outputStream.send();
		}
		else {
			_outputStream.writeByte(_replyCode.getValue());
			_outputStream.writeByte(_data);
			_outputStream.writeShort(_request.getSequenceNumber() & 0xffff);
			_outputStream.writeInt(_extra.size() - 32); // Send the size of the extra bytes but do not include the 32 byte header.
			_outputStream.write(_extra.toByteArray(), 0, _extra.size());
			if(_outputStream.getCounter() < 32) {
				throw new IOException("Response message too short");
			}
		}
	}
}
