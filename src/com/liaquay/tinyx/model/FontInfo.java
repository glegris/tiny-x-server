package com.liaquay.tinyx.model;

public class FontInfo {
	
	/**
	 * A generic Font parameter.
	 * A null value indicates a wild-card
	 */
	private static class FontParameter<T> {
		private final T _value;
		
		public FontParameter(final T value) {
			_value = value;
		}
		
		public boolean matches(final FontParameter<T> t) {
			if(_value == null || t._value == null) return true;
			return _value.equals(t._value);
		}
		
		public FontParameter<T> merge(final FontParameter<T> t) {
			final T value = _value == null ? t._value : _value;
			return new FontParameter<T>(value);
		}
	}
	
	private interface Parser<T> {
		public FontParameter<T> parse(final String s);
	}
	
	private static class StringParser implements Parser<String> {
		@Override
		public FontParameter<String> parse(final String s) {
			return new FontParameter<String>(s == null ? null : s.equals("*") ? null : s);
		}
	}
	
	private static final StringParser STRING_PARSER = new StringParser();
	
	private static class IntegerParser implements Parser<Integer> {
		@Override
		public FontParameter<Integer> parse(final String s) {
			return new FontParameter<Integer>(s == null ? null : s.equals("*") ? null : Integer.parseInt(s));
		}
	}
	
	private static final IntegerParser INTEGER_PARSER = new IntegerParser();
	
	private static class FontSizeParser implements Parser {
		@Override
		public FontParameter<Integer> parse(final String s) {
			return new FontParameter<Integer>(s.equals("*") || s.equals("0") ? null : Integer.parseInt(s));
		}
	}	
	
	private static final FontSizeParser FONT_SIZE_PARSER = new FontSizeParser();
	
	final static Parser[] PARSERS = new Parser[] {
		
		// ?
		STRING_PARSER,
		
		//    FOUNDRY: Type foundry - vendor or supplier of this font
		STRING_PARSER,

		//    FAMILY_NAME: Typeface family
		STRING_PARSER,

		//    WEIGHT_NAME: Weight of type
		STRING_PARSER,

		//    SLANT: Slant (upright, italic, oblique, reverse italic, reverse oblique, or "other")
		STRING_PARSER,

		//    SETWIDTH_NAME: Proportionate width (e.g. normal, condensed, narrow, expanded/double-wide)
		STRING_PARSER,

		//    ADD_STYLE_NAME: Additional style (e.g. (Sans) Serif, Informal, Decorated)
		STRING_PARSER,

		//    PIXEL_SIZE: Size of characters, in pixels; 0 (Zero) means a scalable font
		FONT_SIZE_PARSER,

		//    POINT_SIZE: Size of characters, in tenths of points
		INTEGER_PARSER,

		//    RESOLUTION_X: Horizontal resolution in dots per inch (DPI), for which the font was designed
		INTEGER_PARSER,

		//    RESOLUTION_Y: Vertical resolution, in DPI
		INTEGER_PARSER,

		//    SPACING: monospaced, proportional, or "character cell"
		STRING_PARSER,

		//    AVERAGE_WIDTH: Average width of characters of this font; 0 means scalable font
		FONT_SIZE_PARSER,

		//    CHARSET_REGISTRY: Registry defining this character set
		STRING_PARSER,

		//    CHARSET_ENCODING: Registry's character encoding scheme for this set
		STRING_PARSER,

		STRING_PARSER,
	};
	
	final FontParameter[] _parameters;
	
	private FontInfo(final FontParameter<?>[] parameters) {
		_parameters = parameters;
	}
	
	public FontInfo(final String name) {
		_parameters = new FontParameter[PARSERS.length];
		final String[] split = name.split("-");
		for(int i = 0 ; i < PARSERS.length; ++i) {
			final Parser<?> parser = PARSERS[i];
			if(split.length > i) {
				_parameters[i] = parser.parse(split[i]);
			}
			else {
				_parameters[i] = parser.parse(null);
			}
		}
	}
	
	public FontInfo() {
		_parameters = new FontParameter[PARSERS.length];
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < _parameters.length; ++i) {
			sb.append('-');
			final Object v = _parameters[i]._value;
			sb.append(v == null ? "*" : v);
		}
		return sb.toString();
	}
	
	public FontInfo merge(final FontInfo fontInfo) {
		final FontParameter[] parameters = new FontParameter[PARSERS.length];
		for(int i = 0 ; i < parameters.length; ++i) {
			parameters[i] = _parameters[i].merge(fontInfo._parameters[i]);
		}
		return new FontInfo(parameters);
	}
	
	public boolean matches(final FontInfo fontInfo) {
		for(int i = 0 ; i < _parameters.length; ++i) {
			if(!_parameters[i].matches(fontInfo._parameters[i])) return false;
		}
		return true;
	}
	
	public static void main(String[] a){
		final FontInfo f1 = new FontInfo("--*-Arial-*-r-*-*-*-*-*-*-*-*-ISO8859-*");
		System.out.println(f1);
		final FontInfo f2 = new FontInfo("--*-Arial-*-r-*-*-14-*-*-*-*-*-*-*");
		System.out.println(f2);
		final FontInfo f3 = f1.merge(f2);
		System.out.println(f3);
		System.out.println(f2.getPixelSize());
		System.out.println(f1.matches(f2) + "");
	}

	public String getFamilyName() {
		return (String)_parameters[2]._value;
	}

	public int getPixelSize() {
		return (Integer)_parameters[7]._value;
	}
}
