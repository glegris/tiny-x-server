package com.liaquay.tinyx.renderers.awt;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.util.List;

import com.liaquay.tinyx.model.GraphicsContext;
import com.liaquay.tinyx.model.Pixmap;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.CapStyle.CapStyleType;
import com.liaquay.tinyx.requesthandlers.gcattribhandlers.JoinStyle.JoinStyleType;

public class XAwtGraphicsContext {

	public static void tile(Graphics2D graphics, GraphicsContext graphicsContext) {
		Pixmap p = graphicsContext.getTile();
		if (p != null) {
			TexturePaint tp = new TexturePaint(p.getDrawableListener().getImage(), new Rectangle(0, 0, p.getWidth(), p.getHeight()));
			graphics.setPaint(tp);
		}
	}

	public static void stipple(Graphics2D graphics, GraphicsContext graphicsContext) {
		Pixmap s = graphicsContext.getStipple();
	}

	public static Stroke lineSetup(Graphics2D graphics, GraphicsContext graphicsContext) {

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
			s = new BasicStroke(graphicsContext.getLineWidth(), awtCapStyle, awtJoinStyle, 1.0f, toFloat(graphicsContext.getDashes()), graphicsContext.getDashOffset());
		} else {
			s = new BasicStroke(graphicsContext.getLineWidth(), awtCapStyle, awtJoinStyle, 1.0f);
		}

		return s;
	}

	private static float[] toFloat(List<Integer> dashes) {
		float[] f = new float[dashes.size()];

		for (int i = 0; i < dashes.size(); i++) {
			f[i] = (float) dashes.get(i);
		}

		return f;
	}
}
