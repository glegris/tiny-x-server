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

import java.util.Set;
import java.util.TreeSet;

public class AccessControls {
	
	private boolean _enabled = true;
	
	private final Set<Host> _hosts = new TreeSet<Host>();

	public AccessControls() {
		// By default allow connection from local host.
		_hosts.add(new Host(new byte[] {127, 0, 0 , 1}, Host.Family.Internet));
	}
	
	public boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(final boolean mode) {
		_enabled = mode;
	}

	public Set<Host> getHosts() {
		return _hosts;
	}
}
