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
package com.liaquay.tinyx.requesthandlers.gcattribhandlers;

import java.io.IOException;

import com.liaquay.tinyx.io.XInputStream;
import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.GraphicsContext;


public class GraphicsContextAttributeHandlers {
	
	private final GraphicsContextAttributeHandler[] _handlers = new GraphicsContextAttributeHandler[]{
			new Function(),
			new PlaneMask(),
			new ForegroundColour(),
			new BackgroundColour(),
			new LineWidth(),
			new LineStyle(),
			new CapStyle(),
			new JoinStyle(),
			new FillStyle(),
			new FillRule(),
			new Tile(),
			new Stipple(),
			new TileStippleXOrigin(),
			new TileStippleYOrigin(),
			new Font(),
			new SubWindowMode(),
			new GraphicsExposures(),
			new ClipXOrigin(),
			new ClipYOrigin(),
			new ClipMask(),
			new DashOffset(),
			new DashList(),
			new ArcMode()
	};
	
	public void read(final XInputStream inputStream, final GraphicsContext graphicsContext, final int attributeMask) throws IOException {
		for(int i = 0; i < _handlers.length; ++i) {
			if(((1<<i) & attributeMask) != 0) {
				_handlers[i].read(inputStream, graphicsContext);
			}
		}
	}
	
	public void write(final XOutputStream outputStream, final GraphicsContext graphicsContext, final int attributeMask) throws IOException {
		for(int i = 0; i < _handlers.length; ++i) {
			if(((1<<i) & attributeMask) != 0) {
				_handlers[i].write(outputStream, graphicsContext);
			}
		}
	}
	
}
