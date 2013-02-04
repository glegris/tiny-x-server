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
		read("/usr/share/fonts/X11/misc/encodings.dir", new Listener(){
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
			final String[] tokens = new String[2];
			final DirTokenizer.Listener tl = new DirTokenizer.Listener() {
				@Override
				public boolean token(final int index, final String text) {
					tokens[index] = text;
					return index < 1;
				}
			};
			
			while((line = bufferedReader.readLine()) != null) {
				final int tokenCount = DirTokenizer.tokenize(line, tl);
				if(tokenCount == 2) {
					listener.parameter(tokens[0], tokens[1]);
				}
			}
		}
	}
}
