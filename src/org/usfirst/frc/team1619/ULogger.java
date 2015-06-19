package org.usfirst.frc.team1619;

import java.io.IOException;

public class ULogger extends UGenericLogger {
	
	public ULogger() {
		super("UACRRobot" + UProperties.getLoggingLevel().toString() + "Log");
		nextLog();
	}
	
	/**
	 * Logs an error message to the log file. The logging level must be greater than OFF.
	 * @param error The error message
	 */
	public void Error(String error) {
		if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.OFF) > 0) {
			log(error);
		}
	}
	
	/**
	 * Logs a warning to the log file. The logging level must be greater than ERROR.
	 * @param warning The warning message
	 */
	public void Warning(String warning) {
		if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.ERROR) > 0) {
			log(warning);
		}
	}
	
	/**
	 * Logs the info message to the log file. The logging level must be greater than WARNING.
	 * @param info The info message
	 */
	public void Info(String info) {
		if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.WARNING) > 0) {
			log(info);
		}
	}
	
	/**
	 * Logs the debug message to the log file. The logging level must be greater than INFO.
	 * @param debug The debug message
	 */
	public void Debug(String debug) {
		if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.INFO) > 0) {
			log(debug);
		}
	}

	/**
	 * Does LITERALLY nothing in this implementation of UGenericLogger
	 */
	@Override
	protected void initLog() {
		return;
	}

	/**
	 * Writes to the log file.
	 * 
	 * @param values The string(s) to be written to the log file. Only one string should be passed for this class.
	 */
	@Override
	protected void log(String... values) {
		if (fFileWriter == null || values.length != 1)
			return;
		try {
			String s = values[0];
//			fFileWriter.append(getDateString());
			fFileWriter.append(s);
			fFileWriter.append('\n');
			fFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}