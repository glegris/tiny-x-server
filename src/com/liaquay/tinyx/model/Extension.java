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

public class Extension {
	private final String _name;
	private final int _majorOpCode;
	private final int _firstErrorCode;
	private final int _firstEventCode;
	private final int _errorCodeCount;
	private final int _eventCodeCount;
	
	public Extension(
			final String name,
			final int majorOpCode,
			final int firstErrorCode,
			final int firstEventCode,
			final int errorCodeCount,
			final int eventCodeCount
			)	{
		_name = name;
		_majorOpCode = majorOpCode;
		_firstErrorCode = firstErrorCode;
		_firstEventCode = firstEventCode;
		_errorCodeCount = errorCodeCount;
		_eventCodeCount = eventCodeCount;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getMajorOpCode() {
		return _majorOpCode;
	}
	
	public int getFirstErrorCode() {
		return _firstErrorCode;
	}
	
	public int getFirstEventCode() {
		return _firstEventCode;
	}
	
	public int getErrorCodeCount() {
		return _errorCodeCount;
	}
	
	public int getEventCodeCount() {
		return _eventCodeCount;
	}
}
