package com.liaquay.tinyx.requesthandlers;

import java.io.IOException;

import com.liaquay.tinyx.io.XOutputStream;
import com.liaquay.tinyx.model.Font;
import com.liaquay.tinyx.model.Server;
import com.liaquay.tinyx.renderers.awt.GlyphDetail;

public class FontHandler {

	public static void writeFontInfo(Server server, Font f, XOutputStream outputStream) throws IOException {
		int[] prop=new int[2];
		prop[0] = server.getAtoms().allocate("FONT").getId();
		prop[1] = server.getAtoms().allocate(f.getFontName().toString()).getId();
		
		// Min-bounds
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeShort(f.getMinWidth());
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeShort(0);

		// Unused
		outputStream.writePad(4);

		// Max-bounds
		outputStream.writeShort(0);						// left-side-bearing
		outputStream.writeShort(f.getMaxWidth());		// right-side-bearing
		outputStream.writeShort(f.getMaxWidth());       // character-width
		outputStream.writeShort(f.getMaxAscent());		// ascent
		outputStream.writeShort(f.getMaxDescent());		// descent
		outputStream.writeShort(0);						// attribute

		outputStream.writePad(4);


		outputStream.writeShort(f.getFirstChar());		// min-char-or-byte2
		outputStream.writeShort(f.getLastChar());		// max-char-or-byte2

		outputStream.writeShort(f.getDefaultChar());	// default-char
		outputStream.writeShort(prop!=null ? prop.length/2 : 0); // m
		outputStream.writeByte(0);  // draw-direction
		//	         0     LeftToRight
		//	         1     RightToLeft

		outputStream.writeByte(0);						// min-byte1
		outputStream.writeByte(0);						// max-byte1
		outputStream.writeByte(0);						// all-char-exists
		outputStream.writeShort(f.getMaxAscent());		// font-ascent
		outputStream.writeShort(f.getMaxDescent());		// font-descent

		outputStream.writeInt(f.getLastChar()-f.getFirstChar()+1); // m 

		//		outputStream.writeInt(0);//fontName.length()); // reply-hint

		if(prop!=null){
			for(int j=0; j<prop.length; j++){
				outputStream.writeInt(prop[j]);
			}
		}

		for(int i=f.getFirstChar(); i<=f.getLastChar(); i++){
			GlyphDetail gd = f.getGlyphDetail(i);
			if (gd != null) {
				outputStream.writeShort(0);//gd.leftSideBearing());	// left-side-bearing
				outputStream.writeShort(0);//gd.rightSideBearing());	// right-side-bearing
				outputStream.writeShort(gd.getWidth());			// character-width
				outputStream.writeShort(gd.getAscent());		// ascent
				outputStream.writeShort(gd.getDescent());		// descent
				outputStream.writeShort(0);                    // attribute
			} else {
				System.out.println("No Glyph details found for char: " + i);
			}
		}	
	}

}
