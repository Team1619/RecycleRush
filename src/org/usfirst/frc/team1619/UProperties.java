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
		put("LOGGING_LEVEL", ULoggingLevels.ERROR);
		put("LOGGING_FOLDER", "/home/lvuser/log/");
	}

	/**
	 * Gets a value from the hash table.
	 * 
	 * @param key The String the value is being mapped to.
	 * @param type The data type of the desired value.
	 * @return
	 */
	public static <T> T getProperty(String key, Class<T> type) {
		return (T) sProperties.get(key, type);
	}
	
	/**
	 * Gets a value from the hash table.
	 * 
	 * @param key The String the value is being mapped to.
	 * @param type The data type of the desired value.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> type) {
		return (T) get(key);
	}

	/**
	 * Gets a value from the hash table. If no value is set it is set to defaultValue.
	 * 
	 * @param key The String the value is being mapped to.
	 * @param defaultValue If no value is assigned to the key, sets the value to this.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T defaultValue) {
		T val = (T) get(key);
		return (val == null) ? defaultValue : val;
	}

	/**
	 * Checks to see if in TEST_MODE.
	 * 
	 * @return Whether or not is in TEST_MODE.
	 */
	public static boolean isTestMode() {
		return sProperties.get("TEST_MODE", Boolean.class);
	}
	
	/**
	 * Gets the current logging level.
	 * 
	 * @return The logging level
	 */
	public static ULoggingLevels getLoggingLevel() {
		return sProperties.get("LOGGING_LEVEL", ULoggingLevels.class);
	}
}