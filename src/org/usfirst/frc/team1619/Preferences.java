package org.usfirst.frc.team1619;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class Preferences {

	static final NetworkTable table = NetworkTable.getTable("Preferences");
	
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
		return Double.parseDouble(table.getString(key));
	}

	static public double getNumber(String key, double defaultValue) {
		if(table.containsKey(key))
			return Double.parseDouble(table.getString(key));
		else {
			table.putString(key, Double.toString(defaultValue));
			return defaultValue;
		}
	}

	static public void putString(String key, String value) {
		table.putString(key, value);
	}

	static public String getString(String key) throws TableKeyNotDefinedException {
		return table.getString(key);
	}

	static public String getString(String key, String defaultValue) {
		if(table.containsKey(key))
			return table.getString(key);
		else {
			table.putString(key, defaultValue);
			return defaultValue;
		}
	}

	static public void putBoolean(String key, boolean value) {
		table.putString(key, Boolean.toString(value));
	}

	static public boolean getBoolean(String key) throws TableKeyNotDefinedException {
		return Boolean.parseBoolean(table.getString(key));
	}

	static public boolean getBoolean(String key, boolean defaultValue) {
		if(table.containsKey(key))
			return Boolean.parseBoolean(table.getString(key));
		else {
			table.putString(key, Boolean.toString(defaultValue));
			return defaultValue;
		}
	}

	static public void addTableListener(ITableListener listener) {
		table.addSubTableListener(listener);
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

	static public void putInt(String key, int value) {
		table.putString(key, Integer.toString(value));
	}

	static public int getInt(String key) throws TableKeyNotDefinedException {
		return Integer.parseInt(table.getString(key));
	}

	static public int getInt(String key, int defaultValue)
			throws TableKeyNotDefinedException {
		if(table.containsKey(key))
			return Integer.parseInt(table.getString(key));
		else {
			table.putString(key, Integer.toString(defaultValue));
			return defaultValue;
		}
	}
	
	static public void putDouble(String key, double value) {
		table.putString(key, Double.toString(value));
	}

	static public double getDouble(String key) throws TableKeyNotDefinedException {
		return Double.parseDouble(table.getString(key));
	}

	static public double getDouble(String key, double defaultValue) {
		if(table.containsKey(key))
			return Double.parseDouble(table.getString(key));
		else {
			table.putString(key, Double.toString(defaultValue));
			return defaultValue;
		}
	}

}
