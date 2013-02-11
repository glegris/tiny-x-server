/*
 *  Tiny X server - A Java X server
 *
 *   Copyright (C) 2012  Phil Scull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.events;

import com.liaquay.tinyx.model.eventfactories.ButtonFactory;
import com.liaquay.tinyx.model.eventfactories.DestroyNotifyFactory;
import com.liaquay.tinyx.model.eventfactories.EventFactories;
import com.liaquay.tinyx.model.eventfactories.ExposureFactory;
import com.liaquay.tinyx.model.eventfactories.KeyFactory;
import com.liaquay.tinyx.model.eventfactories.MapNotifyFactory;
import com.liaquay.tinyx.model.eventfactories.MapRequestFactory;
import com.liaquay.tinyx.model.eventfactories.MappingNotifyFactory;

public final class EventFactoriesImpl implements EventFactories {

	@Override
	public final MapNotifyFactory getMapNotifyFactory() {
		return MapNotifyFactoryImpl.FACTORY;
	}

	@Override
	public final MapRequestFactory getMapRequestFactory() {
		return MapRequestFactoryImpl.FACTORY;
	}

	@Override
	public ButtonFactory getButtonPressFactory() {
		return ButtonPressFactoryImpl.FACTORY;
	}	
	
	@Override
	public ButtonFactory getButtonReleaseFactory() {
		return ButtonReleaseFactoryImpl.FACTORY;
	}

	@Override
	public MappingNotifyFactory getMappingNotifyFactory() {
		return MappingNotifyFactoryImpl.FACTORY;
	}
	
	@Override
	public ExposureFactory getExposureFactory() {
		return ExposureFactoryImpl.FACTORY;
	}

	@Override
	public KeyFactory getKeyPressFactory() {
		return KeyPressFactoryImpl.FACTORY;
	}

	@Override
	public KeyFactory getKeyReleaseFactory() {
		return KeyReleaseFactoryImpl.FACTORY;
	}

	@Override
	public DestroyNotifyFactory getDestroyNotifyFactory() {
		return DestroyNotifyFactoryImpl.FACTORY;
	}
}
