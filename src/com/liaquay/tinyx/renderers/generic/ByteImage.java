package com.liaquay.tinyx.renderers.generic;

import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

public class ByteImage extends ImageBase implements TinyXImage {

	private final static Logger LOGGER = Logger.getLogger(ByteImage.class.getName());

	byte[] image = null;

	public ByteImage(int width, int height, byte planes) {
		super(width, height, planes);
		LOGGER.warning("Plane size: " + planes );

		// Depth of 32 bits (4 bytes) to start with (Simple case!)
		int bytes = (width * height * planes)/8;
		image = new byte[bytes];
	}

	public void setData(byte[] imageData) {
		int pos = 0;

		if (planes == 32) {
			ByteArrayInputStream bais = new ByteArrayInputStream(imageData);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					byte r = (byte) bais.read();
					byte g = (byte) bais.read();
					byte b = (byte) bais.read();
					byte a = (byte) bais.read();
					image[pos++] = (byte) (255 - a);		// Alpha
					image[pos++] = (byte) b;		// Blue
					image[pos++] = (byte) g;		// Green
					image[pos++] = (byte) r;
					
//					LOGGER.warning("Blue: " + b + " Green: " + g + " Red: " + r + " Alpha: " + a);
				}
			}
		} else if (planes == 1) {
			image = imageData;
//			for (int i = 0; i < imageData.length; i++) {
//				
//			}
//			BitArray bs = new BitArray(imageData.length*8, imageData);
//			int bitpos = 0;
//			for (int x = 0; x < width; x++) {
//				for (int y = 0; y < height; y++) {
//					boolean bit = bs.get(bitpos++);
//					image[pos++] = (byte) (0x00 | (bit ? 0xff : 0x00));
//					image[pos++] = (byte) (0x00 | (bit ? 0xff : 0x00));
//					image[pos++] = (byte) (0x00 | (bit ? 0xff : 0x00));
//					image[pos++] = -1;
//				}
//			}
		} else if (planes == 4) {
			image = imageData;
//			for (int x = 0; x < width; x++) {
//				for (int y = 0; y < height; y++) {
////					byte r = (byte) bais.read();
////					byte g = (byte) bais.read();
////					byte b = (byte) bais.read();
//					image[pos++] = r;
//					image[pos++] = g;
//					image[pos++] = b;
//					image[pos++] = -1;
//				}
//			}
		} else {
			LOGGER.severe("Unsupported plane size");
		}
	}

	public byte[] getData() {
		return image;
	}
}
