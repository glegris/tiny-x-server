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
