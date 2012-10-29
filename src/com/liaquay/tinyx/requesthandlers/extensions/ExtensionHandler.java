package com.liaquay.tinyx.requesthandlers.extensions;

import java.io.IOException;

import com.liaquay.tinyx.Response;

public interface ExtensionHandler {

	void dispatch(Response response) throws IOException;
	
}
