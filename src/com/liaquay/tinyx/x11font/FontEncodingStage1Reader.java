package com.liaquay.tinyx.x11font;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

public class FontEncodingStage1Reader {
	public interface Listener {
		public void startEncoding();
		public void encoding(final String symbol, final String name);
		public void startMappingUnicode();
		public void mapping(final char from, final char to);
		public void undefine(final char c);
		public void size(final int size);
		public void firstIndex(final int index);
	}
	
	public static void read(final String filename, final Listener listener) throws IOException {
		final FileInputStream fileInputStream = new FileInputStream(filename);
		final InputStream inputStream = filename.toLowerCase().endsWith(".gz") ? new GZIPInputStream(fileInputStream) : fileInputStream;
		try {
			read(inputStream, listener);
		}
		finally {
			fileInputStream.close();
		}
	}
	
	enum State {
		Scanning,
		Encoding,
		MappingUnicode,
		MappingOther
	}
	
	public static void read(final InputStream inputStream, final Listener listener) throws IOException {
		final Reader reader = new InputStreamReader(inputStream);
		final BufferedReader bufferedReader = new BufferedReader(reader);
		{
			String line;
			final String[] tokens = new String[5];
			final FontEncodingTokenizer.Listener tl = new FontEncodingTokenizer.Listener() {
				@Override
				public boolean token(final int index, final String text) {
					tokens[index] = text;
					return index < 5;
				}
			};
			State state = State.Scanning;
			while((line = bufferedReader.readLine()) != null) {
				final int tokenCount = FontEncodingTokenizer.tokenize(line, tl);

				if(tokenCount > 0){
					final String token = tokens[0];
					if(token.equalsIgnoreCase("STARTENCODING")) {
						state =  State.Encoding;
						listener.startEncoding();
						continue;
					}
					if(token.equalsIgnoreCase("STARTMAPPING")) {
						if(tokenCount > 1) {
							final String mappingType = tokens[1];
							if(mappingType.equalsIgnoreCase("unicode")) {
								state =  State.MappingUnicode;
								listener.startMappingUnicode();
							}
							else {
								state =  State.MappingOther;
							}
						}
						else {							
							state =  State.MappingUnicode;
							listener.startMappingUnicode();
						}
						continue;
					}					
					if(token.equalsIgnoreCase("SIZE")) {
						if(tokenCount > 2) {
							final int rows = parseInt(tokens[1]);
							final int cols = parseInt(tokens[2]);
							final int size = (rows<<8) + cols;
							listener.size(size);
						}
						else if(tokenCount > 1) {
							final int cols = parseInt(tokens[1]);
							final int size = cols;
							listener.size(size);
						}
						continue;
					}
					if(token.equalsIgnoreCase("FIRSTINDEX")) {
						if(tokenCount > 2) {
							final int rows = parseInt(tokens[1]);
							final int cols = parseInt(tokens[2]);
							final int index = (rows<<8) + cols;
							listener.firstIndex(index);
						}
						else if(tokenCount > 1) {
							final int cols = parseInt(tokens[1]);
							final int index = cols;
							listener.firstIndex(index);
						}
						continue;
					}					
				}
				switch(state) {
				case Scanning:
					break;
				case Encoding:
					if(tokenCount > 1){
						listener.encoding(tokens[0], tokens[1]);
					}
					break;
				case MappingUnicode:
					if(tokenCount > 1){
						if(tokens[0].equalsIgnoreCase("UNDEFINE")) {
							if(tokenCount > 2) {
								final int start = parseInt(tokens[1]);
								final int end = parseInt(tokens[2]);
								for(int i = start; i < end; ++i) {
									listener.undefine((char)i);
								}
							}
							else if(tokenCount > 1) {
								listener.undefine(parseChar(tokens[1]));
							}
						}
						else {
							if(tokenCount > 2) {
								final int start = parseInt(tokens[1]);
								final int end = parseInt(tokens[2]);
								int to = parseInt(tokens[2]);
								for(int i = start; i < end; ++i) {
									listener.mapping((char)i, (char)to++);
								}
							}
							else if(tokenCount > 1) {
								listener.mapping(parseChar(tokens[0]), parseChar(tokens[1]));
							}
						}
					}
					break;
				}
			}
		}
	}
	
	private static final int parseInt(final String numberString) {
		return Integer.decode(numberString);
	}
	
	private static final char parseChar(final String numberString) {
		return (char)parseInt(numberString);
	}
	
	public static void main(final String[] args) throws IOException {
		read("/usr/share/fonts/X11/encodings/large/big5hkscs-0.enc.gz",  new FontEncodingStage1Reader.Listener() {
			
			@Override
			public void mapping(final char from, final char to) {
				System.out.println("Mapping: " + (int)from + " " + (int)to);
			}

			@Override
			public void encoding(final String symbol, final String name) {
				System.out.println("Encoding: " + symbol + " " + name);
			}

			@Override
			public void undefine(final char c) {
				System.out.println("Undefine: " + (int)c);
			}

			@Override
			public void startEncoding() {
				System.out.println("Start Encoding");
			}

			@Override
			public void startMappingUnicode() {
				System.out.println("Start Mapping");
			}

			@Override
			public void size(final int size) {
				System.out.println("Size: " +size);
			}

			@Override
			public void firstIndex(final int index) {
				System.out.println("First index: " + index);
			}
		});
	}
}
