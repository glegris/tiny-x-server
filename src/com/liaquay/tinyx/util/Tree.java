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
package com.liaquay.tinyx.util;

public class Tree<T extends Tree<T>> {
	private Tree<T> _parent = null;
	private Tree<T> _firstChild = null;
	private Tree<T> _lastChild = null; // TODO populate
	private Tree<T> _nextSibling = null;
	private Tree<T> _prevSibling = null;

	public Tree(final Tree<T> parent) {
		_parent = parent;
		if(parent != null) {
			parent.addChild(this);
		}
	}

	@SuppressWarnings("unchecked")
	public final T getParent() {
		return (T)_parent;
	}

	public void addChild(final Tree<T> child) {
		child._prevSibling = _lastChild;
		child._nextSibling = null;
		
		if(_lastChild != null) {
			_lastChild._nextSibling = child;
		}
		else {
			_firstChild = child;
		}
		_lastChild = child;
		child._parent = this;
	}

	public final void removeChild(final Tree<T> child) {
		//TODO double link!
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