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

import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.requesthandlers.AttributeHandler;
import com.liaquay.tinyx.requesthandlers.AttributeHandlers;

public class GraphicsContextAttributeHandlers extends AttributeHandlers<GraphicsContext> {
	
	@SuppressWarnings("unchecked")
	public GraphicsContextAttributeHandlers() {
		super(new AttributeHandler[]{
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
		});
	}
	
	public final void copy(
			final GraphicsContext source, 
			final GraphicsContext destination, 
			final int attributeMask) throws IOException {
		
		for(int i = 0; i < 32; ++i) {
			final GraphicsAttributeHandler gah = (GraphicsAttributeHandler)getHandler(i, attributeMask);
			if(gah != null) gah.copy(source, destination);
		}
	}
}
