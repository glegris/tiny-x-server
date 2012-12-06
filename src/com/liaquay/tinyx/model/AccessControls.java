package com.liaquay.tinyx.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccessControls {
	boolean mode;
	
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
	
	
}
