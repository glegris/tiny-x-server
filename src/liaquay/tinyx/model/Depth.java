package com.liaquay.tinyx.model;

/**
 * TODO What is this! 
 *
 */
public class Depth {
	private final int _depth;
	private final Visual[] _visuals;
	
	public Depth(final int depth, final Visual[] visuals) {
		_depth = depth;
		_visuals = visuals;
	}
	
	public int getDepth() {
		return _depth;
	}
	
	public Visual[] getVisuals() {
		return _visuals;
	}
}
