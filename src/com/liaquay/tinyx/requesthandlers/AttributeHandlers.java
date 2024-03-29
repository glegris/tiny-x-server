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
package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.Response.ErrorCode;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Server;

public class AttributeHandlers<T> {
	private final AttributeHandler<T>[] _handlers;
	
	public AttributeHandlers(final AttributeHandler<T>[] handlers){
		_handlers = handlers;
	}
	
	public final void read(
			final Server server,
			final Client client,
			final Request request, 
			final Response response,
			final T t,
			final int attributeMask) throws IOException {
		
		for(int i = 0; i < _handlers.length; ++i) {
			if(((1<<i) & attributeMask) != 0) {
				final int pos = request.getInputStream().getCounter();
				_handlers[i].read(server, client, request, response, t);
				if(!ErrorCode.None.equals(response.getResponseCode())) {
					return;
				}
				final int length = request.getInputStream().getCounter() - pos;
				if(length > 4) throw new RuntimeException("Attribute length has exceeded word length");
				request.getInputStream().skip(4-length);
			}
		}
	}
	
	public final void write(final XOutputStream outputStream, final T t, final int attributeMask) throws IOException {
		for(int i = 0; i < _handlers.length; ++i) {
			if(((1<<i) & attributeMask) != 0) {
				_handlers[i].write(outputStream, t);
			}
		}
	}
	
	protected final AttributeHandler<T> getHandler(final int index, final int attributeMask) {
		if(((1<<index) & attributeMask) != 0) {
			return _handlers[index];
		}
		return null;
	}
}
