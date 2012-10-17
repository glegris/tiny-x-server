package com.liaquay.tinyx.model;

public interface Resource {
	public static final int SERVER_BIT=0x20000000;
	public static final int SERVER_MINID=32;

	public static final int BITSFORRESOURCES=22;
	public static final int BITSFORCLIENTS=7;

	public static final int MAXCLIENTS=(1<<BITSFORCLIENTS); // Actually max-clients + 1 !

	public static final int CLIENTOFFSET=BITSFORRESOURCES;
	public static final int CLIENT_ID_MASK=(((1<<BITSFORCLIENTS)-1)<<BITSFORRESOURCES);
	public static final int RESOURCE_ID_MASK=(1<<BITSFORRESOURCES)-1;
	
	public int getId();
}
