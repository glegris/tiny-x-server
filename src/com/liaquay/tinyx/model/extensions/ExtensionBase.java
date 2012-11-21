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
package com.liaquay.tinyx.model.extensions;

import com.liaquay.tinyx.RequestHandler;


abstract class ExtensionBase implements Extension {
	static int currentMaxType=127;
	static int currentMaxEventType=/*Event.EXTENSION_EVENT_BASE=*/64;
	static int currentMaxErrorType=17;


	//  static void init(String foo){
	//    if(foo==null) return;
	//    int start=0, end;
	//    while(true){
	//      if((end=foo.indexOf(',', start))>0){
	//	load(foo.substring(start, end));
	//	start=end+1;
	//	continue;
	//      }
	//      if(start<foo.length()){ load(foo.substring(start)); }
	//      break;
	//    }
	//  }

	int type;
	
	int firstevent;
	int firsterror;

	int eventcount;
	int errorcount;
	String name;
	RequestHandler handler;
	
	public ExtensionBase(String name, int firstEvent, int firstError, RequestHandler handler) {
		this.name = name;
		this.firstevent = firstEvent;
		this.firsterror = firstError;
		this.handler = handler;
	}
	
	@Override
	public RequestHandler getHandler() {
		return handler;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public int getEventcount() {
		return eventcount;
	}
	public void setEventcount(int eventcount) {
		this.eventcount = eventcount;
	}


	public int getFirstEvent() {
		return firstevent;
	}

	public void setFirstEvent(int firstevent) {
		this.firstevent = firstevent;
	}

	public int getFirstError() {
		return firsterror;	
	}


	public void setFirstError(int firsterror) {
		this.firsterror = firsterror;
	}

	public int getErrorcount() {
		return errorcount;
	}
	public void setErrorcount(int errorcount) {
		this.errorcount = errorcount;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
