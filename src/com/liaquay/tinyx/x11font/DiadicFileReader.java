package com.liaquay.tinyx.x11font;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class DiadicFileReader {

	public interface Listener {
		public void parameter(final String arg1, final String arg2);
	}
	
	public static void main(final String[] args) throws IOException {
		read("/usr/share/fonts/X11/misc/fonts.dir", new Listener(){
			@Override
			public void parameter(final String fontFileName, final String fontName) {
				System.out.println(fontFileName + " " + fontName);
			}
		});
		read("/usr/share/fonts/X11/misc/fonts.alias", new Listener(){
			@Override
			public void parameter(final String fontFileName, final String fontName) {
				System.out.println(fontFileName + " " + fontName);
			}
		});
	}
	
	public static void read(final String fileName, final Listener listener)  throws IOException {
		final InputStream inputStream = new FileInputStream(fileName);
		read(inputStream, listener);
	}
	
	public static void read(final InputStream inputStream, final Listener listener) throws IOException {
		final Reader reader = new InputStreamReader(inputStream);
		final BufferedReader bufferedReader = new BufferedReader(reader);
		{
			String line;
			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("!")) { // Skip comment lines
					int indexOfFirstSpace = -1;
					for(int i = 0; i < line.length(); ++i) {
						final char c = line.charAt(i);
						if(c == ' ' || c=='\t') {
							indexOfFirstSpace = i;
							break;
						}
					}
					if(indexOfFirstSpace > -1) {
						listener.parameter(line.substring(0, indexOfFirstSpace), line.substring(indexOfFirstSpace, line.length()).trim());
					}
				}
			}
		}
	}
}
