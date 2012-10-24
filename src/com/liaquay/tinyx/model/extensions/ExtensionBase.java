/* -*-mode:java; c-basic-offset:2; -*- */
/* WeirdX - Guess.
 *
 * Copyright (C) 1999-2004 JCraft, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.liaquay.tinyx.model.extensions;


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
	int eventbase;
	int eventcount;
	int errorbase;
	int errorcount;
	String name;
	
	public ExtensionBase(String name) {
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getEventbase() {
		return eventbase;
	}
	public void setEventbase(int eventbase) {
		this.eventbase = eventbase;
	}
	public int getEventcount() {
		return eventcount;
	}
	public void setEventcount(int eventcount) {
		this.eventcount = eventcount;
	}
	public int getErrorbase() {
		return errorbase;
	}
	public void setErrorbase(int errorbase) {
		this.errorbase = errorbase;
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

	//  static final void dispatch(int reqType, Client c) throws IOException{
	//    for(int i=0; i<ext.length; i++){
	//      if(ext[i]==null)break;
	//      if(ext[i].type==reqType){
	//	ext[i].dispatch(c);
	//	return;
	//      }
	//    }
	//    System.err.println("Extension: unknown reqType "+reqType);
	//  }
	//
	//  static void swap(int etyp, Event e){
	//    for(int i=0; i<ext.length; i++){
	//      if(ext[i]==null)break;
	//      if(ext[i].eventcount==0)continue;
	//      if(ext[i].eventbase<=etyp && 
	//	 etyp<=ext[i].eventbase+ext[i].eventcount){
	//	ext[i].swap(e);
	//	return;
	//      }
	//    }
	//  }
	//
	//  abstract void dispatch(Client c) throws IOException;
	//  abstract void swap(Event e);



}
