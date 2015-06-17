package org.usfirst.frc.team1619;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public abstract class UGenericLogger {

	private static final int MAX_LOGS = 50;
	protected static final String LOG_FOLDER_PATH = "/home/lvuser/log/";
	private static SimpleDateFormat sDateFormat;
	protected static String sLogFolder = getDateString(); 

	protected FileWriter fFileWriter;
	private String fLogName;

	static {
		sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
		sDateFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
	}

	public UGenericLogger(String logName) {
		this.fLogName = new String(logName);
	}
	
	static void deleteFile(File folder) {
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				deleteFile(file);
			}
		}
		try {
			folder.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static void cleanUp() {
		try {
			File logFolder = new File(LOG_FOLDER_PATH);
			File[] logPaths = logFolder.listFiles();
			Arrays.sort(logPaths);
			for (int i = 0; i < (logPaths.length - MAX_LOGS); i++)
				deleteFile(logPaths[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static String getDateString() {
		return sDateFormat.format(new Date());
	}

	protected void nextLog() {
		try {
			if (fFileWriter != null)
				fFileWriter.close();
			File logDir = new File(LOG_FOLDER_PATH + sLogFolder);
			logDir.mkdir();
			if (logDir.exists()) {
				fFileWriter = new FileWriter(LOG_FOLDER_PATH + sLogFolder + "/" + fLogName);
				initLog();
			}
			else {
				System.err.println("Cannot create log folder " + fLogName);
				fFileWriter = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void log(String... values);
	
	protected abstract void initLog();
}
