package com.liaquay.tinyx.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

import javax.swing.JFrame;

import sun.font.StandardGlyphVector;

public class FontProcessor  {

	public FontProcessor() {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font fonts[] = e.getAllFonts();
		
		for (Font f : fonts) {
			int numGlyphs = f.getNumGlyphs();
			int defaultGlyph = f.getMissingGlyphCode();
			
//			Attribute atts[] = f.getAvailableAttributes();


			Font newFont = f.deriveFont(120);
			
			float posture = newFont.getItalicAngle();
			byte baseline = newFont.getBaselineFor('a');

			FontRenderContext frc = new FontRenderContext(null, true, true);
			Rectangle2D rect = newFont.getMaxCharBounds(frc);
			double maxX = rect.getMaxX()*72;
			double maxY = rect.getMaxY()*72;
			double minX = rect.getMinX()*72;
			double minY = rect.getMinY()*72;
			
			System.out.println("Posture: " + posture + "  Baseline: " + baseline + "  X: " + maxX + " Y: " + maxY + "  x1: " + minX + "  y1: " + minY);
			
			
			int defaultChar = f.getMissingGlyphCode();
			
			boolean a = f.canDisplay('a');
			System.out.println("Can display a: " + a);
//			GlyphVector v = new StandardGlyphVector();
			for (TextAttribute att : f.getAttributes().keySet()) {
				
			}
			
			System.out.println("Font: " + f.getFontName() + " Glyphs:" + numGlyphs + " Default: " + defaultGlyph);
		}
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FontProcessor fp = new FontProcessor();

	}

}
