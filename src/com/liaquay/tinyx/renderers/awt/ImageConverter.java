package com.liaquay.tinyx.renderers.awt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import com.liaquay.tinyx.renderers.generic.ByteImage;

public class ImageConverter {

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

			retImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			retImage.setData(raster);
		} else if (image.getPlanes() == 24) {
			WritableRaster raster = Raster.createInterleavedRaster(db, // dataBuffer
					image.getWidth(), // width
					image.getHeight(), // height
					image.getWidth() * 3, // scanlineStride
					3, // pixelStride
					new int[]{ 2, 1, 0}, // bandOffsets
					null); // location

			retImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			retImage.setData(raster);

		} else if (image.getPlanes() == 1) {

			WritableRaster raster = Raster.createPackedRaster(db, image.getWidth(), image.getHeight(), 1, null);

			byte[] arr = {(byte)0x00, (byte)0xff};
			IndexColorModel colorModel = new IndexColorModel(1, 2, arr, arr, arr);
			retImage = new BufferedImage(colorModel, raster, false, null);
//		} else if (image.getPlanes() == 4) {
//			WritableRaster raster = Raster.createPackedRaster(db, image.getWidth(), image.getHeight(), 4, null);
//
//			byte[] r = {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0xC0, 
//					(byte)0x08, (byte)0x80, (byte)0x80, (byte)0xA0,
//					(byte)0x0F, (byte)0xF0, (byte)0xF0, (byte)0x00,
//					(byte)0x00, (byte)0x00, (byte)0xF0,(byte)0xA0};
//
//			byte[] g = {(byte)0x0F, (byte)0xff, (byte)0x50, (byte)0x00, 
//					(byte)0x00, (byte)0x00, (byte)0x10, (byte)0x0E,
//					(byte)0x00, (byte)0xF0, (byte)0x0A, (byte)0x00,
//					(byte)0x00, (byte)0x80, (byte)0x0F,(byte)0x0C};
//
//			byte[] b = {(byte)0x0F, (byte)0xff, (byte)0x00, (byte)0x00, 
//					(byte)0x00, (byte)0x00, (byte)0x0A, (byte)0x00,
//					(byte)0x0F, (byte)0x0F, (byte)0x00, (byte)0x00,
//					(byte)0x0a, (byte)0xA0, (byte)0x00,(byte)0x00};
//
//
//			IndexColorModel colorModel = new IndexColorModel(4, 16, r, g, b);
//			retImage = new BufferedImage(colorModel, raster, false, null);
		}

		XawtDrawableListener.writeImage(retImage,  "testImage31231");
		//		Raster r = new IntegerComponentRaster(arg0, arg1)
		//		retImage.setData(r)


		return retImage;
	}
}
