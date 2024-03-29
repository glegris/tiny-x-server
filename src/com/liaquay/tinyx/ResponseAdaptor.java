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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.io.LsbXOutputStream;
import com.liaquay.tinyx.io.MsbXOutputStream;
import com.liaquay.tinyx.io.XOutputStream;

public class ResponseAdaptor implements Response {
	
	private final static Logger LOGGER = Logger.getLogger(ResponseAdaptor.class.getName());

	public enum ReplyCode {
		Error,
		Ok
	}

	private final ByteArrayOutputStream _extra = new ByteArrayOutputStream();
	private final XOutputStream _extraOutputStream;
	private final XOutputStream _outputStream;

	private boolean _headerSent = false;
	private boolean _prepared = false;
	private boolean _sent = false;
	private Request _request = null;
	private ErrorCode _responseCode = ErrorCode.None;
	
	public ResponseAdaptor(final XOutputStream outputStream) {
		_outputStream = outputStream;
		
		switch(outputStream.getByteOrder()) {
		case LSB: _extraOutputStream = new LsbXOutputStream(_extra);break;
		case MSB: _extraOutputStream = new MsbXOutputStream(_extra);break;
		default: throw new RuntimeException("Unknown byte order " + outputStream.getByteOrder());
		}
	}
	
	private void reset() {
		// Prepare the extra buffer in case it is needed
		_extra.reset();
		
		// Indicate that the header is yet to be sent
		_headerSent = false;
		
		_prepared = false;
		
		_sent = false;
		
		_responseCode = ErrorCode.None;
		
		_outputStream.resetCounter();
		_extraOutputStream.resetCounter();		
	}
	
	public void setRequest(final Request request) {
		
		// Clear response for new request
		reset();
		
		// Make a note of the request so the response can have the correct sequence number
		_request = request;
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
	public XOutputStream respond(final int data) throws IOException {
		if(_headerSent) {
			throw new IOException("Attempt to re-send message header");			
		}
		_prepared = true;
		_replyCode = ReplyCode.Ok;
		_data = data;
		return _extraOutputStream;
	}
	
	private int _extraLength = 0;
	
	/**
	 * Send a response header along with the length of extra data.
	 * This method allows for an efficient response without double buffering...
	 * but you have to know how much extra data is to be sent a priori.
	 * 
	 * @param extraLength the length of extra data to send in bytes
	 * @throws IOException 
	 */
	@Override
	public XOutputStream respond(final int data, final int extraLength) throws IOException {
		if(_headerSent) {
			throw new IOException("Attempt to re-send message header");			
		}
		_prepared = true;
		_outputStream.writeByte(ReplyCode.Ok.ordinal());
		_outputStream.writeByte(data); 
		_outputStream.writeShort(_request.getSequenceNumber() & 0xffff);
		final int lengthInWords = (extraLength+3)>>2;
		_outputStream.writeInt(lengthInWords);
		_headerSent = true;
		_extraLength = lengthInWords << 2;
		return _outputStream;
	}

	/**
	 * Deliver the message to the client.
	 */
	public void send() throws IOException {
		padAlign();
		
		if(_sent) {
			throw new IOException("Attempt made to re-send a response");			
		}
		_sent = true;
		
		if(!_prepared) {
			return;
		}
		
		if(_headerSent) {
			if(_outputStream.getCounter() > 32 + _extraLength) {
				throw new IOException("Response message too long");
			}
			if(_outputStream.getCounter() < 32 + _extraLength) {
				_outputStream.writePad(32 + _extraLength - _outputStream.getCounter());
			}
		}
		else {
			_outputStream.writeByte(_replyCode.ordinal());
			_outputStream.writeByte(_data);
			_outputStream.writeShort(_request.getSequenceNumber() & 0xffff);
			final int extraLength =_extra.size() < 24 ? 0 :  _extra.size() - 24;
			_outputStream.writeInt((extraLength+3)>>2); // Send the size of the extra bytes but do not include the 32 byte header.
			_outputStream.write(_extra.toByteArray(), 0, _extra.size());
			if(_extra.size() < 24) {
				_outputStream.writePad(24-_extra.size());
			}
		}
		_outputStream.send();
		
		// Clear down the response.
		// This should allow multiple responses to a single request as is required by ListFontsWithInfo.
		reset();
	}

	@Override
	public void error(final ErrorCode errorCode, final int resourceId) throws IOException {
		
		{ // Debugging - trace where errors are being raised
			try {
				throw new RuntimeException("TRACE error " + errorCode + " for value " + resourceId);
			}
			catch(final Exception e) {
				LOGGER.log(Level.WARNING, "TRACE error " + errorCode + " for value " + resourceId, e);
			}
		}
		
		_outputStream.writeByte(ReplyCode.Error.ordinal());
		_outputStream.writeByte(errorCode.ordinal());
		_outputStream.writeShort(_request.getSequenceNumber() & 0xffff);
		_outputStream.writeInt(resourceId);
		_outputStream.writeShort(_request.getData());
		_outputStream.writeByte(_request.getMajorOpCode());
		_responseCode = errorCode;
		_outputStream.writePad(32 - _outputStream.getCounter());
	}

	@Override
	public void padHeader() throws IOException {
		if(_headerSent) {
			_outputStream.writePad(32 - _outputStream.getCounter());
		}
		else {
			_extraOutputStream.writePad(24 - _extraOutputStream.getCounter());
		}
	}

	@Override
	public void padAlign() throws IOException {
		if(_headerSent) {
			_outputStream.writePad(-_outputStream.getCounter() & 3);
		}
		else {
			_extraOutputStream.writePad(-_extraOutputStream.getCounter() & 3);
		}
	}
	
	public ErrorCode getResponseCode() {
		return _responseCode;
	}
}
