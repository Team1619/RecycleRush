package org.usfirst.frc.team1619;

import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class Preferences {
	static protected final StringTable table = new StringTable("Preferences");

	static public boolean containsKey(String key) {
		return table.containsKey(key);
	}
	
	static public Object getValue(String key) throws TableKeyNotDefinedException {
		return table.getValue(key);
	}

	static public void putValue(String key, Object value)
			throws IllegalArgumentException {
		table.putString(key, value.toString());
	}

	static public void retrieveValue(String key, Object externalValue) {
		table.retrieveValue(key, externalValue);
	}

	static public void putNumber(String key, double value) {
		table.putString(key, Double.toString(value));
	}

	static public double getNumber(String key) throws TableKeyNotDefinedException {
		return table.getNumber(key);
	}

	static public double getNumber(String key, double defaultValue) {
		return table.getNumber(key, defaultValue);
	}

	static public void putString(String key, String value) {
		table.putString(key, value);
	}

	static public String getString(String key) throws TableKeyNotDefinedException {
		return table.getString(key);
	}

	static public String getString(String key, String defaultValue) {
		return table.getString(key, defaultValue);
	}

	static public void putBoolean(String key, boolean value) {
		table.putBoolean(key, value);
	}

	static public boolean getBoolean(String key) throws TableKeyNotDefinedException {
		return table.getBoolean(key);
	}

	static public boolean getBoolean(String key, boolean defaultValue) {
		return table.getBoolean(key, defaultValue);
	}

	static public void addTableListener(ITableListener listener) {
		table.addTableListener(listener);
	}

	static public void addTableListener(ITableListener listener,
			boolean immediateNotify) {
		table.addTableListener(listener, immediateNotify);
	}

	static public void addTableListener(String key, ITableListener listener,
			boolean immediateNotify) {
		table.addTableListener(key, listener, immediateNotify);
	}

	static public void addSubTableListener(ITableListener listener) {
		table.addSubTableListener(listener);
	}

	static public void removeTableListener(ITableListener listener) {
		table.removeTableListener(listener);
	}

	@Deprecated
	static public void putInt(String key, int value) {
		table.putInt(key, value);
	}

	@Deprecated
	static public int getInt(String key) throws TableKeyNotDefinedException {
		return table.getInt(key);
	}

	@Deprecated
	static public int getInt(String key, int defaultValue)
			throws TableKeyNotDefinedException {
		return table.getInt(key, defaultValue);
	}

	@Deprecated
	static public void putDouble(String key, double value) {
		table.putDouble(key, value);
	}

	@Deprecated
	static public double getDouble(String key) throws TableKeyNotDefinedException {
		return table.getDouble(key);
	}

	@Deprecated
	static public double getDouble(String key, double defaultValue) {
		return table.getDouble(key, defaultValue);

	}
}