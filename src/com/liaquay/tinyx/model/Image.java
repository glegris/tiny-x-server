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

	public interface Listener {
		
	}
	
	private static final class NullListener implements Listener {
	}

	private static final Listener NULL_LISTENER = new NullListener();

	private Listener _listener = NULL_LISTENER;

	public void setListener(final Listener listener) {
		_listener = listener;
	}

	public Listener getListener() {
		return _listener;
	}
}
