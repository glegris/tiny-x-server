package com.liaquay.tinyx.renderers.generic;

import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

import com.liaquay.tinyx.model.Format;

public class ByteImage extends ImageBase implements TinyXImage {

	private final static Logger LOGGER = Logger.getLogger(ByteImage.class.getName());

	byte[] image = null;

	public ByteImage(int width, int height, Format format) {
		super(width, height, format);
		
		// How many bytes needed to represent this image
		int bytes = (width * height * format.getBpp())/8;
		image = new byte[bytes];
	}

	// Depth, bpp, scanline pad
//	private static Format[] FORMATS = new Format[] {
//		new Format (1, 1, 32),
//		new Format (4, 8, 32),
//		new Format (8, 8, 32),
//		new Format (15, 16, 32),
//		new Format (16, 16, 32),
//		new Format (24, 32, 32),
//		new Format (32, 32, 32)
//	};
	public void setData(byte[] imageData) {
		int pos = 0;

		if (format.getBpp() == 32) {
			ByteArrayInputStream bais = new ByteArrayInputStream(imageData);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					byte a = (byte) bais.read();
					byte r = (byte) bais.read();
					byte g = (byte) bais.read();
					byte b = (byte) bais.read();
					image[pos++] = (byte) (255 - a);		// Alpha
					image[pos++] = (byte) b;		// Blue
					image[pos++] = (byte) g;		// Green
					image[pos++] = (byte) r;

					//					LOGGER.warning("Blue: " + b + " Green: " + g + " Red: " + r + " Alpha: " + a);
				}
			}
		} else if (format.getBpp() == 24) {
			ByteArrayInputStream bais = new ByteArrayInputStream(imageData);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					byte r = (byte) bais.read();
					byte g = (byte) bais.read();
					byte b = (byte) bais.read();
					image[pos++] = (byte) b;		// Blue
					image[pos++] = (byte) g;		// Green
					image[pos++] = (byte) r;

					//						LOGGER.warning("Blue: " + b + " Green: " + g + " Red: " + r + " Alpha: " + a);
				}
			}
			
		} else if (format.getDepth() == 1) {
			image = imageData;
//			BitArray bs = new BitArray(imageData.length*8, imageData);
//			
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
		} else if (format.getDepth() == 4) {
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
			LOGGER.severe("Unsupported plane size of " + format.getDepth() + " in ByteImage.setData()");
		}
	}

	public byte[] getData() {
		return image;
	}
}
