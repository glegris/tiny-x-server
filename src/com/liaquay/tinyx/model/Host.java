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

import java.util.Arrays;

public class Host implements Comparable<Host> {
	
	public enum Family {
		Internet,
		DECnet,
		Chaos,
		ServerInterpreted,
		InternetV6;
		
		public static Family getFromIndex(final int index) {
			final Family[] e = values();
			if (index<e.length && index>=0) return e[index];
			return null;
		}
	}
	
	private final Family _family;

	private final byte[] _address;
	private final int _hashCode;

	public Host(final byte[] address, final Family family) {
		_address = address;
		_family = family;
		
		int a = 0;
		for(int i = 0 ; i < Math.min(4, _address.length); ++ i) {
			a |= address[i] << (i << 3); 
		}
		_hashCode = a;
	}
	
	public Family getFamily() {
		return _family;
	}

	public byte[] getAddress() {
		return _address;
	}
	
	@Override
	public boolean equals(final Object o) {
		final Host host = (Host)o;
		return host._family == _family && Arrays.equals(host._address, _address);
	}
	
	@Override
	public int hashCode() {
		return _hashCode;
	}

	@Override
	public int compareTo(final Host host) {
		final int c = _family.compareTo(host._family);
		if(c != 0) return c;
		return compareArray(_address, host._address);
	}
	
	private static final int compareArray(final byte[]b1, final byte[] b2) {
		if(b1.length > b2.length) return 1;
		if(b1.length < b2.length) return -1;
		for(int i = 0; i < b1.length; ++i) {
			if(b1[i] > b2[i]) return 1;
			if(b1[i] < b2[i]) return -1;
		}
		return 0;
	}
}
