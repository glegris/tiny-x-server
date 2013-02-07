/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.x11font;

public class FontEncodingTokenizer {
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
			if(c == '#') break;
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
				if(Character.isWhitespace(c)) {
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
