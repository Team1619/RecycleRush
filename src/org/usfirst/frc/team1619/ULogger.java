package org.usfirst.frc.team1619;

public class ULogger extends UGenericLogger {
	
	public ULogger() {
		super("UACRRobotLog");
	}
	
	public void Error(String error) {
		if (UProperties.getLoggingLevel().compareTo(LoggingLevels.OFF) > 0) {
			// print stuff here
		}
	}
	
	public void Warning(String warning) {
		if (UProperties.getLoggingLevel().compareTo(LoggingLevels.ERROR) > 0) {
			// print stuff here
		}
	}
	
	public void Info(String info) {
		if (UProperties.getLoggingLevel().compareTo(LoggingLevels.WARNING) > 0) {
			// print stuff here
		}
	}
	
	public void Debug(String debug) {
		if (UProperties.getLoggingLevel().compareTo(LoggingLevels.INFO) > 0) {
			// print stuff here
		}
	}

	@Override
	protected void initLog() {
		
	}
}