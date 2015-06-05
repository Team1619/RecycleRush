package org.usfirst.frc.team1619;

import java.util.Properties;

public class UPreferences extends Properties {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -660698254584286808L;
	private static final UPreferences sPreferences = new UPreferences();

	private UPreferences() {
		put("TEST_MODE", Boolean.FALSE);
		put("COMPETITION_MODE", Boolean.FALSE);
	}
	
	public static UPreferences getPreferences() {
		return sPreferences;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get( String key, Class <T> type ) {
		return (T)get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get( String key, T defaultValue ) {
		T val = (T)get(key);
		return (val == null) ? defaultValue : val;
	}
	
	public static boolean isTestMode() {
		return sPreferences.get("TEST_MODE", Boolean.class);
	}
}