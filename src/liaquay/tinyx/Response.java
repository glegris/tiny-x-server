package com.liaquay.tinyx;

import java.io.IOException;

import com.liaquay.tinyx.io.XOutputStream;

public interface Response {
	
	public enum ReplyCode {
		Ok(1),
		Error(0);
		
		private final int _value;
		
		private ReplyCode(final int value) {
			_value = value;
		}
		
		public int getValue() {
			return _value;
		}
	}
	
	public XOutputStream respond(final ReplyCode replyCode, final int data);
	public XOutputStream respond(final ReplyCode replyCode, final int data,  final int length) throws IOException;
}
