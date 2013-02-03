package com.liaquay.tinyx.x11font;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class FileFontEncodingFactory implements FontEncodingFactory {

	private Map<String, String> _fileNameMap = new TreeMap<String, String>();
	private Map<String, FontEncoding> _fontEncodingMap = new TreeMap<String, FontEncoding>();
	
	public FileFontEncodingFactory() {}
	
	public void loadBuiltIns() throws IOException {
		loadBuiltIn("iso8859-1");
		loadBuiltIn("iso8859-2");
		loadBuiltIn("iso8859-3");
		loadBuiltIn("iso8859-4");
		loadBuiltIn("iso8859-5");
		loadBuiltIn("iso8859-6");
		loadBuiltIn("iso8859-7");
		loadBuiltIn("iso8859-8");
		loadBuiltIn("iso8859-9");
		loadBuiltIn("iso8859-10");
		loadBuiltIn("iso8859-15");
	}
	
	private void loadBuiltIn(final String encoding) throws IOException {
		final FontEncoding mapping = FontEncodingState2Reader.readFromResource("com/liaquay/tinyx/x11font/"+ encoding + ".enc");
		_fontEncodingMap.put(encoding, mapping);
	}
	
	public void load(final String folderName) throws IOException {
		FontEncodingsReader.read(folderName, new FontEncodingsReader.Listener() {
			@Override
			public void encoding(final String encoding, final String filename) {
				_fileNameMap.put(encoding, filename);
			}
		});
	}

	@Override
	public FontEncoding open(final String encoding) throws IOException {
		
		final FontEncoding openEncoding = _fontEncodingMap.get(encoding);
		if(openEncoding != null) return openEncoding;
		
		final String fileName = _fileNameMap.get(encoding);
		if(fileName == null) {
			// TODO Log error
			
			return NullFontEncoding.INSTANCE;
		}
		
		final FontEncoding readEncoding = FontEncodingState2Reader.read(fileName);
		_fontEncodingMap.put(encoding, readEncoding);
		return readEncoding;
	}

	@Override
	public void close(final FontEncoding encoding) {
	}
	
	public static void main(final String[] args) throws IOException {
		final FileFontEncodingFactory f = new FileFontEncodingFactory();
		f.load("/usr/share/fonts/X11/misc");
		final FontEncoding encoding1 = f.open("iso8859-11");
		System.out.println(encoding1);
		final FontEncoding encoding2 = f.open("iso8859-11");
		System.out.println(encoding2);
	}
}
