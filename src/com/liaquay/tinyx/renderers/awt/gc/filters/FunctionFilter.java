package com.liaquay.tinyx.renderers.awt.gc.filters;

import com.liaquay.tinyx.renderers.awt.gc.GraphicsContextComposite;

public class FunctionFilter {

	public static int processPixel(int srcPixel, int destPixel, int x, int y, GraphicsContextComposite gc) {
		int outPixel = 0;

		switch (gc.getGC().getFunction()) {
		case Clear:
			outPixel = 0;
			break;
		case And:
			outPixel = srcPixel & destPixel;
			break;
		case AndReverse:
			outPixel = srcPixel & ~destPixel;
			break;
		case Copy:
			outPixel = srcPixel;
			break;
		case AndInverted:
			outPixel = (~srcPixel) & destPixel;
			break;
		case NoOp:
			outPixel = destPixel;
			break;
		case Xor:
			outPixel = srcPixel ^ destPixel;
			break;
		case Or:
			outPixel = srcPixel | destPixel;
			break;
		case Nor:
			outPixel = ~srcPixel & ~destPixel;
			break;
		case Equiv:
			outPixel = ~srcPixel ^ destPixel;
			break;
		case Invert:
			outPixel = ~destPixel;
			break;
		case OrReverse:
			outPixel = srcPixel | ~destPixel;
			break;
		case CopyInverted:
			outPixel = ~srcPixel;
			break;
		case OrInverted:
			outPixel = ~srcPixel | destPixel;
			break;
		case Nand:
			outPixel = ~srcPixel | ~destPixel;
			break;
		case Set:
			outPixel = 0xffffffff;
			break;
		}
		return outPixel;
	}

}
