package com.liaquay.tinyx.x11font;

import java.io.IOException;

public class FontEncodingsReader {
	public interface Listener {
		public void encoding(final String encoding, final String filename);
	}
	
	public static void read(final String dirName, final Listener listener) throws IOException {
		
		final String encodingsDir = dirName +
				(dirName.endsWith("/") ? "" : "/") +
				"encodings.dir";
		
		DiadicFileReader.read(encodingsDir, new DiadicFileReader.Listener()  {
			@Override
			public void parameter(final String encoding, final String filename) {
				final String prefixedFilename = filename.startsWith("/") ? filename :						
						dirName + (dirName.endsWith("/") ? "" : "/") + filename;
				listener.encoding(encoding, prefixedFilename);
			}
		});
	}
	
	public static void main(final String[] args) throws IOException {
		read("/usr/share/fonts/X11/misc", new Listener(){
			@Override
			public void encoding(final String encoding, final String filename) {
				System.out.println("Encoding: " + encoding + " " + filename);
			}
		});
	}
}
