package org.usfirst.frc.team1619;

import java.io.IOException;

public class ULogger extends UGenericLogger {
	
	public ULogger() {
		super("UACRRobot" + UProperties.getLoggingLevel().toString() + "Log");
	}
	
	public void Error(String error) {
		if (UProperties.getLoggingLevel().compareTo(LoggingLevels.OFF) > 0) {
			log(error);
		}
	}
	
	public void Warning(String warning) {
		if (UProperties.getLoggingLevel().compareTo(LoggingLevels.ERROR) > 0) {
			log(warning);
		}
	}
	
	public void Info(String info) {
		if (UProperties.getLoggingLevel().compareTo(LoggingLevels.WARNING) > 0) {
			log(info);
		}
	}
	
	public void Debug(String debug) {
		if (UProperties.getLoggingLevel().compareTo(LoggingLevels.INFO) > 0) {
			log(debug);
		}
	}

	@Override
	protected void initLog() {
		return;
	}

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