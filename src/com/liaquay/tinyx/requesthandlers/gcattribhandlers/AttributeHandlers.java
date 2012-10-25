package com.liaquay.tinyx.requesthandlers.gcattribhandlers;

import java.io.IOException;

import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;

public class AttributeHandlers<T> {
	private final AttributeHandler<T>[] _handlers;
	
	public AttributeHandlers(final AttributeHandler<T>[] handlers){
		_handlers = handlers;
	}
	
	public void read(final XInputStream inputStream, final T t, final int attributeMask) throws IOException {
		for(int i = 0; i < _handlers.length; ++i) {
			if(((1<<i) & attributeMask) != 0) {
				_handlers[i].read(inputStream, t);
			}
		}
	}
	
	public void write(final XOutputStream outputStream, final T t, final int attributeMask) throws IOException {
		for(int i = 0; i < _handlers.length; ++i) {
			if(((1<<i) & attributeMask) != 0) {
				_handlers[i].write(outputStream, t);
			}
		}
	}
}
