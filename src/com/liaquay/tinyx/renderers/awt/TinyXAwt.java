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
package com.liaquay.tinyx.renderers.awt;

import java.io.IOException;

import com.liaquay.tinyx.ConnectionFactory;
import com.liaquay.tinyx.TinyXServer;
import com.liaquay.tinyx.events.EventFactoriesImpl;
import com.liaquay.tinyx.model.ColorMap;
import com.liaquay.tinyx.model.Depths;
import com.liaquay.tinyx.model.Screen;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Server.ResourceFactory;
import com.liaquay.tinyx.model.TrueColorMap;
import com.liaquay.tinyx.model.Visual;
import com.liaquay.tinyx.model.Visual.BackingStoreSupport;
import com.liaquay.tinyx.model.Visual.VisualClass;
import com.liaquay.tinyx.model.eventfactories.EventFactories;
import com.liaquay.tinyx.renderers.awt.XawtScreen.Listener;

public class TinyXAwt {

	/**
	 * Just an example of how to configure a server.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {

		final EventFactories eventFactories = new EventFactoriesImpl();
		
		// Create a new server
		final Server server = new Server(eventFactories);

		// Configure the new server

		final Visual visual = server.createVisual(new ResourceFactory<Visual>() {
			@Override
			public Visual create(final int resourceId) {
				return new Visual(
						resourceId, 
						32,
						BackingStoreSupport.BackingStoreAlways,
						VisualClass.TrueColor, 
						8,  // Bits Per RGB
						256, // TODO How do colour maps relate to visuals
						0x000000ff, // Red mask
						0x0000ff00, // Green mask
						0x00ff0000  // Blue mask
						);
			}
		});

		final Depths depths = new Depths();
		depths.add(visual);   

		final ColorMap defaultColorMap = server.createColorMap(new ResourceFactory<ColorMap>() {
			@Override
			public ColorMap create(final int resourceId) {
				return new TrueColorMap(resourceId);
			}
		});

		final Screen screen = server.addScreen(new ResourceFactory<Screen>() {
			@Override
			public Screen create(final int resourceId) {
				return new Screen(
						resourceId, 
						defaultColorMap,
						visual,
						32,
						1280,
						800,
						1280,
						800,
						depths,
						eventFactories);
			}
		});

		final TinyXServer tinyXServer = new TinyXServer(6001, new ConnectionFactory(server));
		
		final XawtScreen xawtScreen = new XawtScreen(screen);
		xawtScreen.getListeners().add(new Listener() {
			@Override
			public void closed() {
				tinyXServer.close();
			}
		});
		
		tinyXServer.listen();
	}
}
