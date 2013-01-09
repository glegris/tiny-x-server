package com.liaquay.tinyx.model;

public class Image {

	public enum ImageType {
		BitMap,
		XYPixmap,
		ZPixmap;
		
		public static ImageType getFromIndex(final int index) {
			final ImageType[] imageTypes = values();
			if(index >= 0 && index < imageTypes.length) return imageTypes[index];
			return null;
		}
	}

	interface Listener {
		Image createImage(Pixmap p);
	}
	
	
	
}
