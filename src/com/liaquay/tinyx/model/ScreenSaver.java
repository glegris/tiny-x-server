package com.liaquay.tinyx.model;


public class ScreenSaver {

	public enum TriState {
        No,
        Yes,
        Default;
		
		public static TriState getFromIndex(final int index) {
			final TriState[] triStates = values();
			if(index >= 0 && index < triStates.length) return triStates[index];
			return null;
		}
	}
	
	public enum Mode {
        Activate,
        Reset;
		
		public static Mode getFromIndex(final int index) {
			final Mode[] modes = values();
			if(index >= 0 && index < modes.length) return modes[index];
			return null;
		}
	}
	
	Mode _mode;
	
	boolean _enabled = true;

	/** The default timeout before the screensaver kicks in (In seconds) */
	private int DEFAULT_TIMEOUT = 60; 

	/** The default interval before switching between screensavers (Prevented phosphor burn on old monitors) */
	private int DEFAULT_INTERVAL = 600; 

	/** The current timeout before the screensaver kicks in (In seconds) */
	private int _timeout = 0;

	private int _interval = 0;

	private TriState _preferBlanking = TriState.Default;

	private TriState _allowExposures = TriState.Default;

	public void setMode(int mode) {
		_mode = Mode.getFromIndex(mode);
	}

	public void setDefaultTimeout() {
		_timeout = DEFAULT_TIMEOUT;
	}

	public void setDefaultInterval() {
		_interval = DEFAULT_INTERVAL;
	}

	public void setEnabled(boolean bool) {
		_enabled = bool;
	}

	public void setBlanking(int blanking) {
		_preferBlanking = TriState.getFromIndex(blanking);
	}

	public void setAllowExposures(int allowExposures) {
		_allowExposures = TriState.getFromIndex(allowExposures);
	}

	public int getTimeout() {
		return _timeout;
	}

	public int getInterval() {
		return _interval;
	}

	public byte getPreferBlankingIndex() {
		return (byte) _preferBlanking.ordinal();
	}

	public byte getAllowExposuresIndex() {
		return (byte) _allowExposures.ordinal();
	}
	
}
