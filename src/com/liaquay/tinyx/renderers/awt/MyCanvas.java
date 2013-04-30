package com.liaquay.tinyx.renderers.awt;

import java.awt.Canvas;
import java.awt.Graphics;

import com.liaquay.tinyx.model.Window;

public class MyCanvas extends Canvas {

	Window rootWindow;
	
//	@Override
//	public void paint(final Graphics graphics) {
//		
//		final XawtWindow awtRootWindow = (XawtWindow)rootWindow.getWindowListener();
//		
//		graphics.drawImage(
//				awtRootWindow.getImage(), 
//				rootWindow.getAbsX(), 
//				rootWindow.getAbsY(),
//				rootWindow.getWidth(),
//				rootWindow.getHeight(), 
//				null);
//	}

	public void setRootWindow(final Window rootWindow) {
		this.rootWindow = rootWindow;
	}

//	@Override
//	public void repaint(final int x, final int y, final int width, final int height) {
//		//		getGraphics().setClip(x, y, width, height);
//
//		final int topX = rootWindow.getX();
//		final int topY = rootWindow.getY();
//		final int bottomX = rootWindow.getX() + rootWindow.getWidth();
//		final int bottomY = rootWindow.getY() + rootWindow.getHeight();
//		
//		final XawtWindow awtRootWindow = (XawtWindow)rootWindow.getWindowListener();
//
//		getGraphics().drawImage(awtRootWindow.getImage(),  topX, topY, bottomX, bottomY, topX, topY, bottomX, bottomY, this);
//
//		//		getGraphics().drawImage(rootWindow.getListener().getImage(),  
//		//				rootWindow.getAbsX()+ x, rootWindow.getAbsY() + y, rootWindow.getAbsX() + x+width, rootWindow.getAbsY() + y+height, 
//		//				rootWindow.getAbsX()+ x, rootWindow.getAbsY() + y, rootWindow.getAbsX() + x+width, rootWindow.getAbsY() + y+height, 
//		////				x, y, x+width, y+height,
//		////				,  rootWindow.getAbsY(), rootWindow.getAbsX()+ rootWindow.getWidth(), rootWindow.getAbsY()+rootWindow.getHeight()
//		//				null);
//	}

//	@Override
//	public void update(final Graphics g) {
//		repaint();
//	}
}
