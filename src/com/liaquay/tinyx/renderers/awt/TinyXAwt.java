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

import com.liaquay.tinyx.TinyXServer;
import com.liaquay.tinyx.events.EventFactoriesImpl;
import com.liaquay.tinyx.font.CompoundFontFactory;
import com.liaquay.tinyx.font.DealiasingFontFactory;
import com.liaquay.tinyx.font.FontManager;
import com.liaquay.tinyx.model.ColorMap;
import com.liaquay.tinyx.model.Depths;
import com.liaquay.tinyx.model.FontDetail;
import com.liaquay.tinyx.model.FontFactory;
import com.liaquay.tinyx.model.Keyboard;
import com.liaquay.tinyx.model.KeyboardMapping;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.model.Screen;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.model.Server.ResourceFactory;
import com.liaquay.tinyx.model.TrueColorMap;
import com.liaquay.tinyx.model.Visual;
import com.liaquay.tinyx.model.Visual.BackingStoreSupport;
import com.liaquay.tinyx.model.Visual.VisualClass;
import com.liaquay.tinyx.model.Window;
import com.liaquay.tinyx.model.eventfactories.EventFactories;
import com.liaquay.tinyx.renderers.awt.XawtScreen.Listener;
import com.liaquay.tinyx.x11font.EncodingFontFactory;
import com.liaquay.tinyx.x11font.FileFontEncodingFactory;
import com.liaquay.tinyx.x11font.FontDirReader;

public class TinyXAwt {

	private final Server _server;
	
	public TinyXAwt(final Server server) {
		_server = server;
	}
	
	public Server getServer() {
		return _server;
	}
	
	private static FontFactory buildPcfFontFactory(final FileFontEncodingFactory fontEncodingFactory) throws IOException {
		final String folder = "/usr/share/fonts/X11/misc/";
		
		final XawtPcfFontFactory xawtPcfFontFactory = new XawtPcfFontFactory();
		final FontManager fontManager = new FontManager(xawtPcfFontFactory, 50);
		final EncodingFontFactory encodingFontFactory = new EncodingFontFactory(fontManager, fontEncodingFactory);
		final DealiasingFontFactory dealiasingFontFactory = new DealiasingFontFactory(encodingFontFactory);
		
		FontDirReader.read(folder, new FontDirReader.Listener() {
			
			@Override
			public void font(final String fileName, final String fontName) {
				xawtPcfFontFactory.addFont(fileName, fontName);
			}
			
			@Override
			public void alias(final String alias, final String pattern) {
				dealiasingFontFactory.addFontAlias(alias, pattern);
			}
		});		
		
		return dealiasingFontFactory;
	}
	
	private static FontFactory buildNativeFontFactory(final FileFontEncodingFactory fontEncodingFactory) throws IOException {
		
		final XawtNativeFontFactory xawtPcfFontFactory = new XawtNativeFontFactory();
		final FontManager fontManager = new FontManager(xawtPcfFontFactory, 50);
		final EncodingFontFactory encodingFontFactory = new EncodingFontFactory(fontManager, fontEncodingFactory);
		final DealiasingFontFactory dealiasingFontFactory = new DealiasingFontFactory(encodingFontFactory);
		
		dealiasingFontFactory.addFontAlias("variable", "-*-Arial-medium-r-normal-*-*-120-*-*-*-*-iso8859-1");
		
		for(final FontDetail.Slant slant : FontDetail.Slant.values()) {
			for(final FontDetail.Weight weight :FontDetail. Weight.values()) {
				dealiasingFontFactory.addFontAlias(
						"-adobe-symbol-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-adobe-fontspecific", 
						"-adobe-symbol-"+weight.name()+"-"+slant.name()+"-normal--0-0-0-0-p-0-adobe-symbol");
			}
		}
		return dealiasingFontFactory;
	}
	
	
	/**
	 * Just an example of how to configure a server.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {

		final EventFactories eventFactories = new EventFactoriesImpl();
		
		final FileFontEncodingFactory fontEncodingFactory = new FileFontEncodingFactory();
		fontEncodingFactory.load("/usr/share/fonts/X11/misc/");
		fontEncodingFactory.loadBuiltIns();
		
		final FontFactory fontFactory = new CompoundFontFactory(
				new FontFactory[]{
					buildPcfFontFactory(fontEncodingFactory),
					buildNativeFontFactory(fontEncodingFactory)});
		
		final KeyboardMapping keyboardMapping = XawtKeyboardMappingFactory.createKeyboardMapping();
		
		final Keyboard keyboard = new Keyboard(keyboardMapping);

		// Create a new server

		final Server server = new Server(eventFactories, keyboard, fontFactory);

		// Configure the new server
		server.setListener(new Server.Listener() {
			@Override
			public void windowCreated(final Window window) {
//				window.setListener( new XawtWindow(window));
			}
			
			@Override
			public void pixmapCreated(final Pixmap pixmap) {
				pixmap.setListener(new XawtPixmap(pixmap));
			}
		});
		
		final Visual trueColor24 = server.createVisual(new ResourceFactory<Visual>() {
			@Override
			public Visual create(final int resourceId) {
				return new Visual(
						resourceId, 
						24,
						BackingStoreSupport.BackingStoreNever,
						VisualClass.TrueColor, 
						8,  // Bits Per RGB
						256, // TODO How do colour maps relate to visuals
						0x00ff0000, // Red mask
						0x0000ff00, // Green mask
						0x000000ff  // Blue mask
						);
			}
		});
		
		final Visual trueColor32 = server.createVisual(new ResourceFactory<Visual>() {
			@Override
			public Visual create(final int resourceId) {
				return new Visual(
						resourceId, 
						32,
						BackingStoreSupport.BackingStoreNever,
						VisualClass.TrueColor, 
						8,  // Bits Per RGB
						256, // TODO How do colour maps relate to visuals
						0x00ff0000, // Red mask
						0x0000ff00, // Green mask
						0x000000ff  // Blue mask
						);
			}
		});
		
		// Note Direct color is not yet supported.
		// Direct color required a dynamically modifiable colour-map
		final Visual directColor24 = server.createVisual(new ResourceFactory<Visual>() {
			@Override
			public Visual create(final int resourceId) {
				return new Visual(
						resourceId, 
						24,
						BackingStoreSupport.BackingStoreNever,
						VisualClass.DirectColor, 
						8,  // Bits Per RGB
						256, // TODO How do colour maps relate to visuals
						0x00ff0000, // Red mask
						0x0000ff00, // Green mask
						0x000000ff  // Blue mask
						);
			}
		});


		// Note Direct color is not yet supported
		// Direct color required a dynamically modifiable colour-map
		final Visual directColor32 = server.createVisual(new ResourceFactory<Visual>() {
			@Override
			public Visual create(final int resourceId) {
				return new Visual(
						resourceId, 
						32,
						BackingStoreSupport.BackingStoreNever,
						VisualClass.TrueColor, 
						8,  // Bits Per RGB
						256, // TODO How do colour maps relate to visuals
						0x00ff0000, // Red mask
						0x0000ff00, // Green mask
						0x000000ff  // Blue mask
						);
			}
		});

	
		final Depths depths = new Depths();
		
		depths.add(new Depths.Depth(1));
		depths.add(new Depths.Depth(4));
		depths.add(new Depths.Depth(8));
		depths.add(new Depths.Depth(15));
		depths.add(new Depths.Depth(16));
		depths.add(trueColor24);
		depths.add(trueColor32);
// Direct color required a dynamically modifiable colour-map
//		depths.add(directColor24);
//		depths.add(directColor32);

		
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
						trueColor24,
						24,
						1024,
						800,
						1024,
						800,
						depths,
						eventFactories);
			}
		});

		final TinyXServer tinyXServer = new TinyXServer(6001, server);
		
		final XawtScreen xawtScreen = new XawtScreen(new TinyXAwt(server), screen);
		xawtScreen.getListeners().add(new Listener() {
			@Override
			public void closed() { 
				tinyXServer.close();
			}
		});
		
		tinyXServer.listen();
	}
}
