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
package com.liaquay.tinyx.model;

public class Bell {
	
	private final static int DEFAULT_VOLUME_PERCENT = 50;
	private final static int DEFAULT_PITCH = 440;
	private final static int DEFAULT_DURATION = 500;
	
	private int _volumePercent = 50;
	
	public int getVolumePercent() {
		return _volumePercent;
	}
	
	public void setVolumePercent(final int volume) {
		_volumePercent = volume < 0 ? DEFAULT_VOLUME_PERCENT : volume;
	}
	
	private int _pitch = 5000;
	
	public int getPitch() {
		return _pitch;
	}
	
	public void setPitch(final int pitch) {
		_pitch = pitch < 0 ? DEFAULT_PITCH : pitch;
	}
	
	private int _duration = 500;
	
	public int getDuration() {
		return _duration;
	}
	
	public void setDuration(final int duration) {
		_duration = duration < 0 ? DEFAULT_DURATION :duration;
	}
}
