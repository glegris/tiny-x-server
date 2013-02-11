package com.liaquay.tinyx.renderers.awt;

import java.awt.Canvas;
import java.awt.Graphics;

import com.liaquay.tinyx.model.Window;

public class MyCanvas extends Canvas {

	Window rootWindow;

	@Override
	public void paint(Graphics graphics) {
		graphics.drawImage(rootWindow.getListener().getImage(),  rootWindow.getAbsX(),  rootWindow.getAbsY(), rootWindow.getWidth(), rootWindow.getHeight(), null);
	}

	public void setRootWindow(Window rootWindow) {
		this.rootWindow = rootWindow;
	}

	@Override
	public void repaint(int x, int y, int width, int height) {
		//		getGraphics().setClip(x, y, width, height);

		int topX = rootWindow.getX();
		int topY = rootWindow.getY();
		int bottomX = rootWindow.getX() + rootWindow.getWidth();
		int bottomY = rootWindow.getY() + rootWindow.getHeight();

		getGraphics().drawImage(rootWindow.getListener().getImage(),  topX, topY, bottomX, bottomY, topX, topY, bottomX, bottomY, this);

		//		getGraphics().drawImage(rootWindow.getListener().getImage(),  
		//				rootWindow.getAbsX()+ x, rootWindow.getAbsY() + y, rootWindow.getAbsX() + x+width, rootWindow.getAbsY() + y+height, 
		//				rootWindow.getAbsX()+ x, rootWindow.getAbsY() + y, rootWindow.getAbsX() + x+width, rootWindow.getAbsY() + y+height, 
		////				x, y, x+width, y+height,
		////				,  rootWindow.getAbsY(), rootWindow.getAbsX()+ rootWindow.getWidth(), rootWindow.getAbsY()+rootWindow.getHeight()
		//				null);
	}

	@Override
	public void update(Graphics g) {
		repaint();
	}
}
