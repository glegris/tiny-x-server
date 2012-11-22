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
package com.liaquay.tinyx.requesthandlers.keycontattribhandlers;

import java.io.IOException;

import com.liaquay.tinyx.Request;
import com.liaquay.tinyx.Response;
import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.model.Client;
import com.liaquay.tinyx.model.Keyboard;
import com.liaquay.tinyx.model.Server;

public class KeyMode extends KeyboardControlAttributeHandler {

	@Override
	public void read(
			final Server server, 
			final Client client, 
			final Request request,
			final Response response, 
			final KeyboardAttributeState keyboardAttributeState) throws IOException {
		
		final XInputStream inputStream = request.getInputStream();
		final int keyMode = inputStream.readUnsignedByte();
		final int keycode = keyboardAttributeState._key;
		final Keyboard keyboard = server.getKeyboard();
		if(keycode == -1){
			// No key was specified
			switch(keyMode) {
			case 0: keyboard.setGlobalAutoRepeatEnabled(false);
			case 1: keyboard.setGlobalAutoRepeatEnabled(true);
			default:keyboard.setGlobalAutoRepeatDefault();
			}
		}
		else {
			// A single LED was specified
			switch(keyMode) {
			case 0: keyboard.setAutoRepeatOff(keycode);
			case 1: keyboard.setAutoRepeatOn(keycode);
			default:keyboard.setAutoRepeatDefault(keycode);
			}
		}
	}
}
