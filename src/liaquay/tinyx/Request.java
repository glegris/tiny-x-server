package com.liaquay.tinyx;

import com.liaquay.tinyx.io.XInputStream;

public interface Request {
	
	/**
	 * @return the request code
	 */
	public int getMajorOpCode();
	
	/**
	 * @return the length in bytes of the extra data.
	 */
	public int getLength();
	
	/** 
	 * @return the request data byte, sometimes referred to as the minor op code
	 */
	public int getData();	
	
	/**
	 * @return the sequence number of the request
	 */
	public int getSequenceNumber();
	
	/**
	 * @return the input stream to read extra data from
	 */
	public XInputStream getInputStream();
}
