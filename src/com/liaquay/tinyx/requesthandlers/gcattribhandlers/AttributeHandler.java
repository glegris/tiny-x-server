package com.liaquay.tinyx.requesthandlers.gcattribhandlers;

import java.io.IOException;

import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;

public interface AttributeHandler<T> {
	public void read(final XInputStream inputStream, final T t) throws IOException;
	public void write(final XOutputStream outputStream, final T T) throws IOException;
}
