package com.liaquay.tinyx.renderers.awt;

import java.awt.Insets;
import java.awt.Panel;

public class XawtWindow extends Panel {
	
    private static final Insets insets = new Insets(10,10,10,10);
    
    @Override
    public Insets getInsets() {return insets;}

	public XawtWindow() {
		
	}
	
	
}
