package org.usfirst.frc.team1619;

import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class UDynamicPreferences {
	static protected final UStringTable sTable = new UStringTable("Preferences");

	static public boolean containsKey(String key) {
		return sTable.containsKey(key);
	}
	
	static public Object getValue(String key) throws TableKeyNotDefinedException {
		return sTable.getValue(key);
	}

	static public void putValue(String key, Object value)
			throws IllegalArgumentException {
		sTable.putString(key, value.toString());
	}

	static public void retrieveValue(String key, Object externalValue) {
		sTable.retrieveValue(key, externalValue);
	}

	static public void putNumber(String key, double value) {
		sTable.putString(key, Double.toString(value));
	}

	static public double getNumber(String key) throws TableKeyNotDefinedException {
		return sTable.getNumber(key);
	}

	static public double getNumber(String key, double defaultValue) {
		return sTable.getNumber(key, defaultValue);
	}

	static public void putString(String key, String value) {
		sTable.putString(key, value);
	}

	static public String getString(String key) throws TableKeyNotDefinedException {
		return sTable.getString(key);
	}

	static public String getString(String key, String defaultValue) {
		return sTable.getString(key, defaultValue);
	}

	static public void putBoolean(String key, boolean value) {
		sTable.putBoolean(key, value);
	}

	static public boolean getBoolean(String key) throws TableKeyNotDefinedException {
		return sTable.getBoolean(key);
	}

	static public boolean getBoolean(String key, boolean defaultValue) {
		return sTable.getBoolean(key, defaultValue);
	}

	static public void addTableListener(ITableListener listener) {
		sTable.addTableListener(listener);
	}

	static public void addTableListener(ITableListener listener,
			boolean immediateNotify) {
		sTable.addTableListener(listener, immediateNotify);
	}

	static public void addTableListener(String key, ITableListener listener,
			boolean immediateNotify) {
		sTable.addTableListener(key, listener, immediateNotify);
	}

	static public void addSubTableListener(ITableListener listener) {
		sTable.addSubTableListener(listener);
	}

	static public void removeTableListener(ITableListener listener) {
		sTable.removeTableListener(listener);
	}
}