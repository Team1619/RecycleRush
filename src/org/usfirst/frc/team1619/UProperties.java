package org.usfirst.frc.team1619;

import java.util.Properties;

public class UProperties extends Properties {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -660698254584286808L;
	private static final UProperties sProperties = new UProperties();
	
	private UProperties() {
		put("TEST_MODE", Boolean.FALSE);
		put("COMPETITION_MODE", Boolean.FALSE);
		put("LOGGING_LEVEL", LoggingLevels.ERROR);
	}

	public static UProperties getProperties() {
		return sProperties;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> type) {
		return (T) get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key, T defaultValue) {
		T val = (T) get(key);
		return (val == null) ? defaultValue : val;
	}

	public static boolean isTestMode() {
		return sProperties.get("TEST_MODE", Boolean.class);
	}
	
	public static LoggingLevels getLoggingLevel() {
		return sProperties.get("LOGGING_LEVEL", LoggingLevels.class);
	}
}