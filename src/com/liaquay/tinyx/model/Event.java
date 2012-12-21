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
package com.liaquay.tinyx.model;

import java.io.IOException;

import com.liaquay.tinyx.io.XOutputStream;

public interface Event {
	
	public static final int KeyPress=2;
	public static final int KeyRelease=3;
	public static final int ButtonPress=4;
	public static final int ButtonRelease=5;
	public static final int MotionNotify=6;
	public static final int EnterNotify=7;
	public static final int LeaveNotify=8;
	public static final int FocusIn=9;
	public static final int FocusOut=10;
	public static final int KeymapNotify=11;
	public static final int Expose=12;
	public static final int GraphicsExpose=13;
	public static final int NoExpose=14;
	public static final int VisibilityNotify=15;
	public static final int CreateNotify=16;
	public static final int DestroyNotify=17;
	public static final int UnmapNotify=18;
	public static final int MapNotify=19;
	public static final int MapRequest=20;
	public static final int ReparentNotify=21;
	public static final int ConfigureNotify=22;
	public static final int ConfigureRequest=23;
	public static final int GravityNotify=24;
	public static final int ResizeRequest=25;
	public static final int CirculateNotify=26;
	public static final int CirculateRequest=27;
	public static final int PropertyNotify=28;
	public static final int SelectionClear=29;
	public static final int SelectionRequest=30;
	public 	static final int SelectionNotify=31;
	public 	static final int ColormapNotify=32;
	public static final int ClientMessage=33;
	public static final int MappingNotify=34;
	public static final int LASTEvent=35;
	
	public static final int NoEventMask=0;
	public static final int KeyPressMask=(1<<0);
	public static final int KeyReleaseMask=(1<<1);
	public static final int ButtonPressMask=(1<<2);
	public static final int ButtonReleaseMask=(1<<3);
	public static final int EnterWindowMask=(1<<4);
	public static final int LeaveWindowMask=(1<<5);  
	public static final int PointerMotionMask=(1<<6);  
	public static final int PointerMotionHintMask=(1<<7);  
	public static final int Button1MotionMask=(1<<8);  
	public static final int Button2MotionMask=(1<<9);  
	public static final int Button3MotionMask=(1<<10); 
	public static final int Button4MotionMask=(1<<11); 
	public static final int Button5MotionMask=(1<<12); 
	public static final int ButtonMotionMask=(1<<13); 
	public static final int KeymapStateMask=(1<<14);
	public static final int ExposureMask=(1<<15); 
	public static final int VisibilityChangeMask=(1<<16); 
	public static final int StructureNotifyMask=(1<<17); 
	public static final int ResizeRedirectMask=(1<<18); 
	public static final int SubstructureNotifyMask=(1<<19); 
	public static final int SubstructureRedirectMask=(1<<20); 
	public static final int FocusChangeMask=(1<<21); 
	public static final int PropertyChangeMask=(1<<22); 
	public static final int ColormapChangeMask=(1<<23); 
	public static final int OwnerGrabButtonMask=(1<<24); 
	
	public static final int AtMostOneClient=
				(SubstructureRedirectMask | ResizeRedirectMask | ButtonPressMask);

	public static final int MotionMask=
			        (PointerMotionMask | Button1MotionMask |
				Button2MotionMask | Button3MotionMask | Button4MotionMask |
				Button5MotionMask | ButtonMotionMask );

	public static final int PropagateMask=
			        (KeyPressMask | KeyReleaseMask | ButtonPressMask | 
				 ButtonReleaseMask | MotionMask );

	public static final int PointerGrabMask = (
				ButtonPressMask | ButtonReleaseMask |
				EnterWindowMask | LeaveWindowMask |
				PointerMotionHintMask | KeymapStateMask |
				MotionMask );

	public void write(final XOutputStream outputStream, final int sequenceNumber, final Client client, final Window window) throws IOException;
}
