package com.liaquay.tinyx.model;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class AccessControls {
	boolean mode = true;

	Set<Host> hosts = new HashSet<Host>();

	public boolean getMode() {
		return mode;
	}

	public void setMode(boolean mode) {
		this.mode = mode;
	}

	public Set<Host> getHosts() {
		return hosts;
	}

	public void setHosts(Set<Host> hosts) {
		this.hosts = hosts;
	}

	public boolean validHost(InetAddress inetAddress) {
		if (mode) { // Disabled
			return true;
		} else {
			for (Host h : hosts) {
				if (inetAddress.getAddress().equals(h.getAddress())) {
					return true;
				}
			}
		}
		return false;
	}
}
