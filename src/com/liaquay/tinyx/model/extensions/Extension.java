package com.liaquay.tinyx.model.extensions;

import com.liaquay.tinyx.io.XOutputStream;

public interface Extension {
	String getName();
	
	int getErrorCodes();
	
	void dispatch(XOutputStream out);
}
