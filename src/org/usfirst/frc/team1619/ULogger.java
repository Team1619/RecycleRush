package org.usfirst.frc.team1619;

import java.io.IOException;

public class ULogger extends UGenericLogger {
	
	public ULogger() {
		super("UACRRobot" + UProperties.getLoggingLevel() + "Log");
		nextLog();
	}
	
	/**
	 * Logs an error message to the log file.
	 * 
	 * @param message The error message
	 */
	public void error(String message) {
		log("ERROR", message);
	}
	
	/**
	 * Logs a warning to the log file. The logging level must be greater than or equal to WARNING
	 * 
	 * @param message The warning message
	 */
	public void warning(String message) {
		if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.WARNING) >= 0) {
			log("WARNING", message);
		}
	}
	
	/**
	 * Logs the info message to the log file. The logging level must be greater than or equal to INFO
	 * 
	 * @param message The info message
	 */
	public void info(String message) {
		if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.INFO) >= 0) {
			log("INFO", message);
		}
	}
	
	/**
	 * Logs the debug message to the log file. The logging level must be greater than or equal to DEBUG
	 * 
	 * @param message The debug message
	 */
	public void debug(String message) {
		if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.DEBUG) >= 0) {
			log("DEBUG", message);
		}
	}

	/**
	 * Writes to the log file.
	 * 
	 * @param level The text version of the logging level
	 * @param message The log message
	 */
	protected void log(String level, String message) {
		if (fFileWriter == null)
			return;
		try {
			fFileWriter.append(getDateString());
			fFileWriter.append(' ');
			fFileWriter.append(level);
			fFileWriter.append(' ');
			fFileWriter.append(message);
			fFileWriter.append('\n');
			fFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}