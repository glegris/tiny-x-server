/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Nathan Ludkin
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
package com.liaquay.tinyx.model;

import com.liaquay.tinyx.model.font.FontDetail;

public class Font extends AbstractResource {

	private final FontInfo _fontName;
	private final FontDetail _fontDetail;
	
	public interface Listener {
		public void fontClosed(final Font font);
		public TextExtents getTextExtents(final int character);
		public TextExtents getTextExtents(final String text);
	}
	
	/**
	 * Empty implementation of the listener so that we don't have null checks 
	 * throughout the code. 
	 */
	private static final class NullListener implements Listener {
		@Override
		public void fontClosed(final Font font) {}

		@Override
		public TextExtents getTextExtents(final int character) {
			return new TextExtents(0,0,0,0,0,0);
		}

		@Override
		public TextExtents getTextExtents(final String text) {
			return new TextExtents(0,0,0,0,0,0);
		}
	}
	
	private static final Listener NULL_LISTENER = new NullListener();

	private Listener _listener = NULL_LISTENER;

	public void setListener(final Listener listener) {
		_listener = listener;
	}
	
	public Listener getListener() {
		return _listener;
	}
	
	public Font(final int id, final FontInfo fontName, final FontDetail fontDetail) {
		super(id);

		_fontName = fontName;
		_fontDetail = fontDetail;
	}

	@Override
	public void free() {
		_listener.fontClosed(this);
	}

	public FontInfo getFontInfo() {
		return _fontName;
	}
	
	public int getMaxAscent() {
		return _fontDetail.getMaxAscent();
	}

	public int getMaxWidth() {
		return _fontDetail.getMaxWidth();
}

	public int getMaxDescent() {
		return _fontDetail.getMaxDescent();
	}

	public int getMinWidth() {
		return _fontDetail.getMinWidth();
	}

	public int getDefaultChar() {
		return _fontDetail.getDefaultChar();
	}

	public int getFirstChar() {
		return _fontDetail.getFirstChar();
	}

	public int getLastChar() {
		return _fontDetail.getLastChar();
	}

	public TextExtents getTextExtents(final int character) {
		return _listener.getTextExtents(character);
	}
	
	public TextExtents getTextExtents(final String text) {
		return _listener.getTextExtents(text);
	}
	
	public boolean isLeftToRight() {
		return true; // TODO move to font detail.
	}
}
