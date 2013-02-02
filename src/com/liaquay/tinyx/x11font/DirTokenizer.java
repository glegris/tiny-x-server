package com.liaquay.tinyx.x11font;

public class DirTokenizer {
	public interface Listener {
		public boolean token(final int index, final String text);
	}
	
	private enum State {
		Separator,
		Token,
		QuotedToken
	}
	
	public static final int tokenize(final String s, final Listener listener) {
		int tokens = 0;
		State state = State.Separator;
		final StringBuilder sb = new StringBuilder();
		if(s.startsWith("!")) return tokens;
		for(int i = 0; i < s.length(); ++i) {
			final char c = s.charAt(i);
			switch(state) {
			case Separator:
				if(!Character.isWhitespace(c)) {
					if(c=='"') {
						state = State.QuotedToken;
					}
					else {
						state = State.Token;
						sb.append(c);
					}
				}
				break;
			case QuotedToken:
				if(c=='"') {
					state = State.Separator;
					if(listener.token(tokens++, sb.toString())) return tokens;
					sb.setLength(0);
				}
				else {
					sb.append(c);
				}
				break;
			case Token:
				if(Character.isWhitespace(c) && tokens == 0) {
					state = State.Separator;
					if(!listener.token(tokens++, sb.toString())) return tokens;
					sb.setLength(0);
				}
				else {
					sb.append(c);
				}
				break;
			}
		}
		if(sb.length() > 0) {
			listener.token(tokens++, sb.toString());
		}
		return tokens;
	}
	
	public static void main(final String[] args) {
		tokenize("olglyph-10 \"-sun-open look glyph-----10-100-75-75-p-101-sunolglyph-1", new Listener() {
			
			@Override
			public boolean token(final int index, final String text) {
				System.out.println(index + " " + text);
				return true;
			}
		});
	}
}
