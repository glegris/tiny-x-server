package com.liaquay.tinyx.util;

public class Tree<T extends Tree<T>> {
	private Tree<T> _parent;
	private Tree<T> _firstChild;
	private Tree<T> _nextSibling;
	private Tree<T> _prevSibling;

	public Tree(final Tree<T> parent) {
		_parent = parent;
		if(parent != null) {
			if(parent._firstChild != null) {
				parent._firstChild._prevSibling = this;
				_nextSibling = _parent._firstChild;
			}
			parent._firstChild = this;
		}
	}

	@SuppressWarnings("unchecked")
	public final T getParent() {
		return (T)_parent;
	}

	public void addChild(final Tree<T> child) {
		if(_firstChild != null) {
			_firstChild._prevSibling = child;
		}
		_firstChild = child;
		child._parent = this;
	}

	public final void removeChild(final Tree<T> child) {
		if(child._prevSibling == null) {
			_firstChild = child._nextSibling;
		}
		else {
			child._prevSibling._nextSibling = child._nextSibling;
		}
		if(child._nextSibling != null) {
			child._nextSibling._prevSibling = child._prevSibling;
		}
		child._parent = null;
	}
}