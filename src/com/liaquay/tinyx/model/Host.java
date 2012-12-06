package com.liaquay.tinyx.model;


public class Host {
	
	int family;
//    0         Internet
//    1         DECnet
//    2         Chaos
//    5         ServerInterpreted
//    6         InternetV6
	
	
	byte[] address;

	public int getFamily() {
		return family;
	}

	public void setFamily(int family) {
		this.family = family;
	}

	public byte[] getAddress() {
		return address;
	}

	public void setAddress(byte[] address) {
		this.address = address;
	}
	
	
}
