package org.usfirst.frc.team1619;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class StringTable implements ITable {
	protected final NetworkTable table;

	public StringTable(String name) {
		table = NetworkTable.getTable(name);
	}

	@Override
	public boolean containsKey(String key) {
		return table.containsKey(key);
	}

	@Override
	public Object getValue(String key) throws TableKeyNotDefinedException {
		return table.getValue(key);
	}

	@Override
	public void putValue(String key, Object value)
			throws IllegalArgumentException {
		table.putString(key, value.toString());
	}

	@Override
	public void retrieveValue(String key, Object externalValue) {
		table.retrieveValue(key, externalValue);
	}

	@Override
	public void putNumber(String key, double value) {
		table.putString(key, Double.toString(value));
	}

	@Override
	public double getNumber(String key) throws TableKeyNotDefinedException {
		return Double.parseDouble(table.getString(key));
	}

	@Override
	public double getNumber(String key, double defaultValue) {
		if(table.containsKey(key))
			return Double.parseDouble(table.getString(key));
		else {
			table.putString(key, Double.toString(defaultValue));
			return defaultValue;
		}
	}

	@Override
	public void putString(String key, String value) {
		table.putString(key, value);
	}

	@Override
	public String getString(String key) throws TableKeyNotDefinedException {
		return table.getString(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		if(table.containsKey(key))
			return table.getString(key);
		else {
			table.putString(key, defaultValue);
			return defaultValue;
		}
	}

	@Override
	public void putBoolean(String key, boolean value) {
		table.putString(key, Boolean.toString(value));
	}

	@Override
	public boolean getBoolean(String key) throws TableKeyNotDefinedException {
		return Boolean.parseBoolean(table.getString(key));
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		if(table.containsKey(key))
			return Boolean.parseBoolean(table.getString(key));
		else {
			table.putString(key, Boolean.toString(defaultValue));
			return defaultValue;
		}
	}

	@Override
	public void addTableListener(ITableListener listener) {
		table.addSubTableListener(listener);
	}

	@Override
	public void addTableListener(ITableListener listener,
			boolean immediateNotify) {
		table.addTableListener(listener, immediateNotify);
	}

	@Override
	public void addTableListener(String key, ITableListener listener,
			boolean immediateNotify) {
		table.addTableListener(key, listener, immediateNotify);
	}

	@Override
	public void addSubTableListener(ITableListener listener) {
		table.addSubTableListener(listener);
	}

	@Override
	public void removeTableListener(ITableListener listener) {
		table.removeTableListener(listener);
	}

	@Override
	public void putInt(String key, int value) {
		table.putString(key, Integer.toString(value));
	}

	@Override
	public int getInt(String key) throws TableKeyNotDefinedException {
		return Integer.parseInt(table.getString(key));
	}

	@Override
	public int getInt(String key, int defaultValue)
			throws TableKeyNotDefinedException {
		if(table.containsKey(key))
			return Integer.parseInt(table.getString(key));
		else {
			table.putString(key, Integer.toString(defaultValue));
			return defaultValue;
		}
	}

	@Override
	public void putDouble(String key, double value) {
		table.putString(key, Double.toString(value));
	}

	@Override
	public double getDouble(String key) throws TableKeyNotDefinedException {
		return Double.parseDouble(table.getString(key));
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		if(table.containsKey(key))
			return Double.parseDouble(table.getString(key));
		else {
			table.putString(key, Double.toString(defaultValue));
			return defaultValue;
		}
	}

	@Override
	public boolean containsSubTable(String key) {
		return table.containsSubTable(key);
	}

	@Override
	public ITable getSubTable(String key) {
		return table.getSubTable(key);
	}
}
