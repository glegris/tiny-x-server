package com.liaquay.tinyx.renderers.awt;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.liaquay.tinyx.model.FontInfo;
import com.liaquay.tinyx.model.font.FontDetail;
import com.liaquay.tinyx.model.font.FontFactory;

public class AwtFontFactory implements FontFactory {

	List<FontInfo> _fontNames;
	
	public AwtFontFactory() {
		_fontNames = initFontNames();
	}
	
	private List<FontInfo> initFontNames() {
		final List<FontInfo> fontList = new ArrayList<FontInfo>();
		
		final GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Font[] fonts = e.getAllFonts(); // Get the fonts
		for (final Font f : fonts) {
			final String familyName = f.getFamily();
			//System.out.println(f);
			
			String foundryName = "*";
			String charSet = "ISO8859";
			String pointSize = "*";
			String weightName = "*";
			
			// TODO PS - Add new constructor
			final FontInfo fontString = new FontInfo("-" + foundryName + "-" + familyName + "-"+ weightName + "-" + "r" + "-*-*-*-" + pointSize + "-*-*-*-*-" + charSet + "-*");
			fontList.add(fontString);
			
			System.out.println(fontString);
		}
		return fontList;
	}
	
	@Override
	public List<FontInfo> getFontNames() {
		return _fontNames;
	}

	@Override
	public FontInfo getFirstMatchingFont(final FontInfo requestedFont) {
		for (int i = 0; i < _fontNames.size(); i++) {
			if (_fontNames.get(i).matches(requestedFont)) {
				return _fontNames.get(i);
			}
		}
		
		//TODO: Nasty hack until I have proper matching code in place
		return _fontNames.get(0);
	}
	
	@Override
	public List<FontInfo> getMatchingFonts(final FontInfo requestedFont) {

		List<FontInfo> matchingFonts = new ArrayList<FontInfo>();
		
		for (int i = 0; i < _fontNames.size(); i++) {
			matchingFonts.add(_fontNames.get(i));
		}
		
		return matchingFonts;
	}

	@Override
	public FontDetail getFontDetail(String name, int size) {
		if (size==0)
				size = 8;
		Font f = new Font(name, Font.PLAIN, size);
		
		FontMetrics fm = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(f);
		FontRenderContext frc = new FontRenderContext(null, true, true);
		
		FontDetail fd = new FontDetail();
		fd.setMaxAscent(fm.getMaxAscent());
		fd.setMaxDescent(fm.getMaxDescent());
		fd.setHeight(fm.getHeight());
		fd.setLeading(fm.getLeading());
		fd.setDefaultChar(f.getMissingGlyphCode());
		fd.setMaxWidth(fm.getMaxAdvance());

		char[] chr = new char[1];
		for (int i = fd.getFirstChar(); i <= fd.getLastChar(); i++) {
			chr[0] = (char) i;
			Rectangle2D bounds = f.getStringBounds(chr, 0, 1, frc);
			LineMetrics lm = f.getLineMetrics(chr, 0, 1, frc);
			
			GlyphDetail gd = new GlyphDetail(chr[0]);
			gd.setAscent((int) lm.getAscent());
			gd.setDescent((int) lm.getDescent());
			gd.setWidth((int) bounds.getWidth());
			fd.addGlyph(gd);
		}
		
		return fd;
	}

}
