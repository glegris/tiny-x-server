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
	
	static final int KeyPress=2;
	static final int KeyRelease=3;
	static final int ButtonPress=4;
	static final int ButtonRelease=5;
	static final int MotionNotify=6;
	static final int EnterNotify=7;
	static final int LeaveNotify=8;
	static final int FocusIn=9;
	static final int FocusOut=10;
	static final int KeymapNotify=11;
	static final int Expose=12;
	static final int GraphicsExpose=13;
	static final int NoExpose=14;
	static final int VisibilityNotify=15;
	static final int CreateNotify=16;
	static final int DestroyNotify=17;
	static final int UnmapNotify=18;
	static final int MapNotify=19;
	static final int MapRequest=20;
	static final int ReparentNotify=21;
	static final int ConfigureNotify=22;
	static final int ConfigureRequest=23;
	static final int GravityNotify=24;
	static final int ResizeRequest=25;
	static final int CirculateNotify=26;
	static final int CirculateRequest=27;
	static final int PropertyNotify=28;
	static final int SelectionClear=29;
	static final int SelectionRequest=30;
	static final int SelectionNotify=31;
	static final int ColormapNotify=32;
	static final int ClientMessage=33;
	static final int MappingNotify=34;
	static final int LASTEvent=35;
	
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

	public void write(final XOutputStream outputStream, final int sequenceNumber) throws IOException;
}
