package com.liaquay.tinyx;

import java.io.IOException;

import com.liaquay.tinyx.io.XInputStream;

public class RequestAdaptor implements Request {
	
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
		
		System.out.println(String.format("Read request code %d, data %d, length %d, seq %d", _majorOpCode, _data, _length, _requestSequence));
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
		_inputStream.skip(remaining);
	}
}
