package com.liaquay.tinyx.renderers.awt;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.util.List;

import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.CapStyle.CapStyleType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.JoinStyle.JoinStyleType;

public class XAwtGraphicsContext {

	public static void tile(final Graphics2D graphics, final GraphicsContext graphicsContext) {
		final Pixmap p = graphicsContext.getTile();
		if (p != null) {
			final XawtPixmap awtPixmap = (XawtPixmap)p.getListener();
			TexturePaint tp = new TexturePaint(awtPixmap.getImage(), new Rectangle(0, 0, p.getWidth(), p.getHeight()));
			graphics.setPaint(tp);
		}
	}

	public static void stipple(final Graphics2D graphics, final GraphicsContext graphicsContext) {
		
		final Pixmap stipplePixmap = graphicsContext.getStipple();
		final XawtPixmap awtStipplePixmap = (XawtPixmap)stipplePixmap.getListener();

		if (stipplePixmap != null) {
			final AlphaFilter filter = new AlphaFilter(graphicsContext);
			final BufferedImage srcImage = createCompatibleImage(awtStipplePixmap.getImage());

			XawtDrawableListener.writeImage(srcImage, "test-src.png");

			final FilteredImageSource filteredSrc = new FilteredImageSource(srcImage.getSource(), filter);
			final Image newImage = Toolkit.getDefaultToolkit().createImage(filteredSrc);

			
			final BufferedImage bufImage = imageToBufferedImage(newImage,  stipplePixmap.getWidth(), stipplePixmap.getHeight());
			XawtDrawableListener.writeImage(bufImage, "test-filter.png");
			
			final TexturePaint tp = new TexturePaint(bufImage, new Rectangle(graphicsContext.getTileStippleXOrigin(), graphicsContext.getTileStippleYOrigin(), stipplePixmap.getWidth(), stipplePixmap.getHeight()));
			graphics.setPaint(tp);
		}
	}
	
	static BufferedImage createCompatibleImage(final BufferedImage image)
	{
		final GraphicsConfiguration gc = GraphicsEnvironment.
				getLocalGraphicsEnvironment().
				getDefaultScreenDevice().
				getDefaultConfiguration();

		final BufferedImage newImage = gc.createCompatibleImage(
				image.getWidth(), 
				image.getHeight(), 
				Transparency.TRANSLUCENT);

		final Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return newImage;
	}

	private static BufferedImage imageToBufferedImage(final Image image, final int width, final int height)
	{
		final BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2 = dest.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return dest;
	}

	public static Stroke lineSetup(final Graphics2D graphics, final GraphicsContext graphicsContext) {

		int capStyle = graphicsContext.getCapStyle();
		int awtCapStyle = -1;
		switch (CapStyleType.getFromIndex(capStyle)) {
		case Butt:
			awtCapStyle = BasicStroke.CAP_BUTT;
			break;
		case Round:
			awtCapStyle = BasicStroke.CAP_ROUND;
			break;
		default:
			awtCapStyle = BasicStroke.CAP_SQUARE;
			break;
		}

		int joinStyle = graphicsContext.getJoinStyle();
		int awtJoinStyle = -1;
		switch (JoinStyleType.getFromIndex(joinStyle)) {
		case Bevel:
			awtJoinStyle = BasicStroke.JOIN_BEVEL;
			break;
		case Miter:
			awtJoinStyle = BasicStroke.JOIN_MITER;
			break;
		case Round:
			awtJoinStyle = BasicStroke.JOIN_ROUND;
			break;
		}

		//TODO: Find out how line style fits into this.

		Stroke s = null;
		if (graphicsContext.getDashes() != null && graphicsContext.getDashes().size() > 0) {
			s = new BasicStroke();//graphicsContext.getLineWidth(), awtCapStyle, awtJoinStyle, 1.0f, toFloat(graphicsContext.getDashes()), graphicsContext.getDashOffset());
		} else {
			s = new BasicStroke();//, awtCapStyle, awtJoinStyle, 1.0f);
		}

		return s;
	}

	private static float[] toFloat(final List<Integer> dashes) {
		float[] f = new float[dashes.size()];

		for (int i = 0; i < dashes.size(); i++) {
			f[i] = (float) dashes.get(i);
		}

		return f;
	}
}
