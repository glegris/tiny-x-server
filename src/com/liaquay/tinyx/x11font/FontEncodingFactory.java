package com.liaquay.tinyx.x11font;

import java.io.IOException;

public interface FontEncodingFactory {
	public FontEncoding open(final String encoding) throws IOException;
	public void close(final FontEncoding encoding);
}
 