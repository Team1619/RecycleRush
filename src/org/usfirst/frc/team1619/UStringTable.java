package org.usfirst.frc.team1619;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class UStringTable implements ITable {
	protected final NetworkTable fTable;

	public UStringTable(String name) {
		fTable = NetworkTable.getTable(name);
	}

	@Override
	public boolean containsKey(String key) {
		return fTable.containsKey(key);
	}

	@Override
	public Object getValue(String key) throws TableKeyNotDefinedException {
		return fTable.getValue(key);
	}

	@Override
	public void putValue(String key, Object value)
			throws IllegalArgumentException {
		fTable.putString(key, value.toString());
	}

	@Override
	public void retrieveValue(String key, Object externalValue) {
		fTable.retrieveValue(key, externalValue);
	}

	@Override
	public void putNumber(String key, double value) {
		fTable.putString(key, Double.toString(value));
	}

	@Override
	public double getNumber(String key) throws TableKeyNotDefinedException {
		return Double.parseDouble(fTable.getString(key));
	}

	@Override
	public double getNumber(String key, double defaultValue) {
		if(fTable.containsKey(key))
			return Double.parseDouble(fTable.getString(key));
		else {
			fTable.putString(key, Double.toString(defaultValue));
			return defaultValue;
		}
	}

	@Override
	public void putString(String key, String value) {
		fTable.putString(key, value);
	}

	@Override
	public String getString(String key) throws TableKeyNotDefinedException {
		return fTable.getString(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		if(fTable.containsKey(key))
			return fTable.getString(key);
		else {
			fTable.putString(key, defaultValue);
			return defaultValue;
		}
	}

	@Override
	public void putBoolean(String key, boolean value) {
		fTable.putString(key, Boolean.toString(value));
	}

	@Override
	public boolean getBoolean(String key) throws TableKeyNotDefinedException {
		return Boolean.parseBoolean(fTable.getString(key));
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		if(fTable.containsKey(key))
			return Boolean.parseBoolean(fTable.getString(key));
		else {
			fTable.putString(key, Boolean.toString(defaultValue));
			return defaultValue;
		}
	}

	@Override
	public void addTableListener(ITableListener listener) {
		fTable.addSubTableListener(listener);
	}

	@Override
	public void addTableListener(ITableListener listener,
			boolean immediateNotify) {
		fTable.addTableListener(listener, immediateNotify);
	}

	@Override
	public void addTableListener(String key, ITableListener listener,
			boolean immediateNotify) {
		fTable.addTableListener(key, listener, immediateNotify);
	}

	@Override
	public void addSubTableListener(ITableListener listener) {
		fTable.addSubTableListener(listener);
	}

	@Override
	public void removeTableListener(ITableListener listener) {
		fTable.removeTableListener(listener);
	}

	@Override
	public void putInt(String key, int value) {
		fTable.putString(key, Integer.toString(value));
	}

	@Override
	public int getInt(String key) throws TableKeyNotDefinedException {
		return Integer.parseInt(fTable.getString(key));
	}

	@Override
	public int getInt(String key, int defaultValue)
			throws TableKeyNotDefinedException {
		if(fTable.containsKey(key))
			return Integer.parseInt(fTable.getString(key));
		else {
			fTable.putString(key, Integer.toString(defaultValue));
			return defaultValue;
		}
	}

	@Override
	public void putDouble(String key, double value) {
		fTable.putString(key, Double.toString(value));
	}

	@Override
	public double getDouble(String key) throws TableKeyNotDefinedException {
		return Double.parseDouble(fTable.getString(key));
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		if(fTable.containsKey(key))
			return Double.parseDouble(fTable.getString(key));
		else {
			fTable.putString(key, Double.toString(defaultValue));
			return defaultValue;
		}
	}

	@Override
	public boolean containsSubTable(String key) {
		return fTable.containsSubTable(key);
	}

	@Override
	public ITable getSubTable(String key) {
		return fTable.getSubTable(key);
	}
}