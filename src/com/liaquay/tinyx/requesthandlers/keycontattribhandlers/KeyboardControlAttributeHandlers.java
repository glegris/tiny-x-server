package com.liaquay.tinyx.requesthandlers.keycontattribhandlers;

import com.liaquay.tinyx.requesthandlers.AttributeHandler;
import com.liaquay.tinyx.requesthandlers.AttributeHandlers;

public class KeyboardControlAttributeHandlers extends AttributeHandlers<KeyboardAttributeState> {

	@SuppressWarnings("unchecked")
	public KeyboardControlAttributeHandlers() {
		super(new AttributeHandler[]{
			new KeyClickPercent(),
			new BellPercent(),
			new BellPitch(),
			new BellDuration(),
			new Led(),
			new LedMode(),
			new Key(),
			new KeyMode()
		});
	}
}
