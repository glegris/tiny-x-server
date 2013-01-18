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
package com.liaquay.tinyx.model;


public class FontName extends FontString {

	public FontName(final String name) {
		super(name);
	}

	public FontInfo getFontInfo(final String pattern, final StringBuilder scratch) {
		scratch.setLength(0);
		final boolean match = matchAndMergeFielded(pattern, scratch);
		if(!match) return null;
		final String fontInfoString = scratch.toString();
		return parse(fontInfoString);
	}

	public FontInfo getFontInfo(final String pattern) {
		return getFontInfo(pattern, new StringBuilder());
	}

	public String parseString(final String s) {
		return s == null ? "" : s;
	}

	public int parseInt(final String s) {
		try {
			return s == null ? 0 : Integer.parseInt(s);
		}
		catch(final Exception e) { 
			return 0;
		}
	}

	public FontInfo parse(final String name) {

		final String[] split = name.split("-");
		final String[] snn = new  String[Math.max(14, split.length-1)];
		for(int i=0; i <split.length-1; ++i) snn[i] = split[i+1];

		final String foundry = parseString(snn[0]);
		final String familyName = parseString(snn[1]);
		final String weightName = parseString(snn[2]);
		final String slant = parseString(snn[3]);
		final String setWidthName = parseString(snn[4]);
		final String addStyleName = parseString(snn[5]);
		final int pixelSize = parseInt(snn[6]);
		final int pointSize = parseInt(snn[7]);
		final int resolutionX = parseInt(snn[8]);
		final int resolutionY = parseInt(snn[9]);
		final String spacing = parseString(snn[10]);
		final int averageWidth = parseInt(snn[11]);
		final String charsetRegistry = parseString(snn[12]);
		final String charsetEncoding = parseString(snn[13]);

		return new FontInfo(
				foundry,
				familyName, 
				weightName,
				slant, 
				setWidthName, 
				addStyleName,
				pixelSize, 
				pointSize, 
				resolutionX, 
				resolutionY,
				spacing, 
				averageWidth, 
				charsetRegistry,
				charsetEncoding);
	}
	
	public static void main(String[] a) {
		FontName n = new FontName("-misc-Arial-medium-r-normal--12-100-75-75-c-60-iso8859-1");
		FontInfo i = n.getFontInfo("*");
		System.out.println(i);
	}
}
