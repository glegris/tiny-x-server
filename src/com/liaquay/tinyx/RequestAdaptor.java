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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.liaquay.tinyx.io.XInputStream;

public class RequestAdaptor implements Request {

	private final static Logger LOGGER = Logger.getLogger(RequestAdaptor.class.getName());

	private int _requestSequence = 0;
	private int _majorOpCode = 0;	
	private int _data = 0;
	private int _length = 0;
	private final XInputStream _inputStream;

	public RequestAdaptor(final XInputStream inputStream) {
		_inputStream = inputStream;
	}

	public void readRequest() throws IOException {
		_inputStream.resetCounter();
		_majorOpCode = _inputStream.readUnsignedByte();	
		_data = _inputStream.readUnsignedByte();
		_length = _inputStream.readUnsignedShort() << 2;
		_requestSequence++;

		LOGGER.log(Level.INFO, String.format("Read request code %d, data %d, length %d, seq %d", _majorOpCode, _data, _length, _requestSequence));
	}

	@Override
	public int getMajorOpCode() {
		return _majorOpCode;
	}

	@Override
	public int getLength() {
		return _length;
	}

	@Override
	public int getData() {
		return _data;
	}

	@Override
	public int getSequenceNumber() {
		return _requestSequence;
	}

	@Override
	public XInputStream getInputStream() {
		return _inputStream;
	}

	/**
	 * Skip past any unread input.
	 * 
	 * @throws IOException
	 */
	public void skipRemaining() throws IOException {
		final int remaining = _length - _inputStream.getCounter();
		if (remaining > 0) {
			_inputStream.skip(remaining);
		}
	}
}
