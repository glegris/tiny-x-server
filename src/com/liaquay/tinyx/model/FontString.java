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

public class FontString {
	
	private String _name;
	
	public FontString(final String name) {
		_name = name;
	}
	
	public String toString() {
		return _name;
	}
	
	public String getName() {
		return _name;
	}
	
	public boolean matchAndMergeFielded(final String pattern, final StringBuilder sb){

		final int l1=_name.length();
		final int l2=pattern.length();
		int i1=0;
		int i2=0;
		int i=0;

		while(i1<l1 && i2<l2){
			if(_name.charAt(i1)=='-') {
				i++;
			}
			if(pattern.charAt(i2)=='?' || Character.toLowerCase(_name.charAt(i1))==Character.toLowerCase(pattern.charAt(i2))) {
				sb.append(_name.charAt(i1));
				i1++; i2++;
			}
			else {
				if(pattern.charAt(i2)=='*'){
					sb.append(_name.charAt(i1));
					if(i2+1<l2 && pattern.charAt(i2+1)==_name.charAt(i1)) i2+=2;
					++i1;
				}
				else if(_name.charAt(i1)=='0' &&	(i==7 || i==8 || i==9 || i==10 || i==11 || i==12)){
					if(pattern.charAt(i2)=='-') {
						++i1;
					}
					else if(pattern.charAt(i2)<'0' || '9'<pattern.charAt(i2)) {
						return false;
					}
					else {
						sb.append(pattern.charAt(i2));
						++i2;
					}
				}
				else{
					return false;
				}
			}
		}
		return true;
	}
	public static void main(String[] a){
		final FontString f1 = new FontString("-urw-urw gothic l-book-o-normal--0-0-0-0-p-0-iso8859-1");
		System.out.println(f1);
		final String f2 = "-urw-urw go*thic l-book-??norm*--0-60-0-0-p-0-is?8859-?";
		System.out.println(f2);
		
		final StringBuilder sb = new StringBuilder();
		final boolean match = f1.matchAndMergeFielded(f2, sb);
		System.out.println(match + " " +sb.toString());
	}
}
