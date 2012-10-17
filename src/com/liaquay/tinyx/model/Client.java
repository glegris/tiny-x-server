package com.liaquay.tinyx.model;

public class Client {
	private final int _clientId;
	protected final ClientResources _clientResources = new ClientResources();
	private final int _endFakeID;

	private int _fakeID;

	public Client(final int clientId, final int fakeID) {
		_clientId = clientId;    
		_fakeID = fakeID;
	    _endFakeID = (_fakeID | Resource.RESOURCE_ID_MASK)+1;
	}
	
	public int getClientId() {
		return _clientId;
	}
	
	protected int allocateFakeId(){
		final int id =_fakeID++;
		if (id !=_endFakeID){
			return id;
		}
		// TODO : Error handling!
		System.out.println("error: fakeClient "+id);
		throw new RuntimeException("Error allocating fake ID");
	}	
	
	public void free() {
		_clientResources.free();
	}
}
