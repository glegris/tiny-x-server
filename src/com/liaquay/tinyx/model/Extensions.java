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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Extensions {
	private int _nextMajorOpCode = 128;
	private int _nextErrorCode = 17;
	private int _nextEventCode = 64;
	
	private Map<String, Extension> _extensions = new TreeMap<String, Extension>();
	private List<Extension> _extensionList = new ArrayList<Extension>(5);
 	
	public Extension createExtension(
			final String name,
			final int errorCodeCount,
			final int eventCodeCount) {
		
		final Extension extension = new Extension(name, _nextMajorOpCode, _nextErrorCode, _nextEventCode, errorCodeCount, eventCodeCount);
		_extensions.put(name, extension);
		_extensionList.add(extension);
		_nextMajorOpCode++;
		_nextErrorCode += errorCodeCount;
		_nextEventCode += eventCodeCount;
		return extension;
	}
	
	public List<Extension> getExtensions() {
		return _extensionList;
	}
	
	public Extension getExtension(final String name) {
		return _extensions.get(name);
	}
}
