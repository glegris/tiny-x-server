package com.liaquay.tinyx.renderers.awt;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.logging.Logger;

import com.liaquay.tinyx.renderers.generic.ByteImage;

public class ImageConverter {
	private final static Logger LOGGER = Logger.getLogger(ImageConverter.class.getName());

	public static BufferedImage convertByteImage(ByteImage image) {
		//		BufferedImage retImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		byte[] imageData = image.getData();
		DataBufferByte db = new DataBufferByte(imageData, imageData.length);
		BufferedImage retImage = null;

		if (image.getPlanes() == 32) {
			WritableRaster raster = Raster.createInterleavedRaster(db, // dataBuffer
					image.getWidth(), // width
					image.getHeight(), // height
					image.getWidth() * 4, // scanlineStride
					4, // pixelStride
					new int[]{3, 2, 1, 0}, // bandOffsets
					null); // location

			retImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			retImage.setData(raster);
		} else if (image.getPlanes() == 24) {
			WritableRaster raster = Raster.createInterleavedRaster(db, // dataBuffer
					image.getWidth(), // width
					image.getHeight(), // height
					image.getWidth() * 3, // scanlineStride
					3, // pixelStride
					new int[]{ 2, 1, 0}, // bandOffsets
					null); // location

			retImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			retImage.setData(raster);

		} else if (image.getPlanes() == 1) {

			WritableRaster raster = Raster.createPackedRaster(db, image.getWidth(), image.getHeight(), 1, null);

			byte[] arr = {(byte)0x00, (byte)0xff};
			IndexColorModel colorModel = new IndexColorModel(1, 2, arr, arr, arr);
			retImage = new BufferedImage(colorModel, raster, false, null);
		} else {
			LOGGER.severe("Unsupported depth for ImageConverter.convertByteImage: " + image.getPlanes());
		}

//		XawtDrawableListener.writeImage(retImage,  "testImage31231");
		//		Raster r = new IntegerComponentRaster(arg0, arg1)
		//		retImage.setData(r)


		return retImage;
	}
}
