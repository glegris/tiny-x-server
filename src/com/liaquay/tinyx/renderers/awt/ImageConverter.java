package com.liaquay.tinyx.renderers.awt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.logging.Logger;

import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Image.ImageType;
import com.liaquay.tinyx.renderers.generic.ByteImage;

public class ImageConverter {
	private final static Logger LOGGER = Logger.getLogger(ImageConverter.class.getName());

	public static BufferedImage convertByteImage(ByteImage image, ImageType imageType, GraphicsContext gc) {
		//		BufferedImage retImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		byte[] imageData = image.getData();
		DataBufferByte db = new DataBufferByte(imageData, imageData.length);
		BufferedImage retImage = null;

		if (image.getFormat().getBpp() == 32) {
			WritableRaster raster = Raster.createInterleavedRaster(db, // dataBuffer
					image.getWidth(), // width
					image.getHeight(), // height
					image.getWidth() * 4, // scanlineStride
					4, // pixelStride
					new int[]{3, 2, 1, 0}, // bandOffsets
					null); // location

			retImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			retImage.setData(raster);
		} else if (image.getFormat().getBpp() == 24) {
			WritableRaster raster = Raster.createInterleavedRaster(db, // dataBuffer
					image.getWidth(), // width
					image.getHeight(), // height
					image.getWidth() * 3, // scanlineStride
					3, // pixelStride
					new int[]{ 2, 1, 0}, // bandOffsets
					null); // location

			retImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			retImage.setData(raster);

		} else if (image.getFormat().getBpp() == 1) {
			// We are a bitmap (Probably a clip mask or a stipple mask)
			byte arr[] = { (byte) 0x0, (byte) 0xff};
			IndexColorModel cm = new IndexColorModel(1, 2, arr, arr, arr, arr);
			
			retImage = new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_BYTE_BINARY, cm);
			WritableRaster raster = retImage.getRaster();


			int imagePad = (image.getWidth() % 8) == 0 ? 0 : 8 - (image.getWidth() % 8);   // (8 - 3) = 5
			int imageWidth = image.getWidth() + imagePad; // 3 + 5 = 8;


			int height = image.getHeight();
			int width = image.getWidth();

			int pos = 0;

			for(int y=0;y<height;y++) {
				for(int byteNum=0;byteNum<imageWidth/8;byteNum++) {
					// 
					byte toProcess = image.getData()[pos + byteNum];

					for (int x = 0; x<8;x++) {
						if (byteNum*8+x < width) {
							raster.setSample(byteNum*8 + x, y, 0, toProcess >> 7-x); // 0,1 on last pixel
						}
					}
				}
				pos+=imageWidth/8;
			}
		} else {
			LOGGER.severe("Unsupported depth for ImageConverter.convertByteImage: " + image.getFormat().getBpp());
		}

		return retImage;
	}
}
