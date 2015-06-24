package org.usfirst.frc.team1619;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class UGenericLogger {

	private static final int MAX_LOGS = 50;
	protected static final String LOG_FOLDER_PATH = UProperties.getProperty("LOGGING_FOLDER", String.class);
	private static SimpleDateFormat sDateFormat;
	protected static String sLogFolder = getDateString(); 
	protected static final List<UGenericLogger> sLoggers = new ArrayList<>();


	protected FileWriter fFileWriter;
	private String fLogName;

	/*
	 * Specifies the format for the date and assigns it a time zone.
	 */
	static {
		sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
		sDateFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
	}

	public UGenericLogger(String logName) {
		fLogName = logName;
		sLoggers.add(this);
	}
	
	/**
	 * Recursive delete that deletes either the file argument, 
	 * or all the files in the directory argument, plus the 
	 * directory itself.
	 * 
	 * @param folder (can either be a single file, or a directory)
	 */
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

	/**
	 * Resets the date and creates the new folder that corresponds 
	 * to the directory that the log files are stored in, then calls
	 * the nextLog() method on all loggers.
	 */
	public static void changeLogs() {
		sLogFolder = getDateString();
		for (UGenericLogger l : sLoggers)
			l.nextLog();
		cleanUp();
	}
	
	/**
	 * Accesses all of the directories stored under the LOG_FOLDER_PATH
	 * directory, and sorts them by date, then deletes the oldest ones 
	 * until only the newest 50 remain.
	 */
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

	/**
	 * @return String form of the current date formatted as specified in sDateFormat.
	 * "yyyy-MM-dd-HH-mm-ss-SSSZ"
	 */
	protected static String getDateString() {
		return sDateFormat.format(new Date());
	}

	/**
	 * If the directory for the current date doesn't exist 
	 * it creates the directory (named with the date string).
	 * Then creates a file and a fileWriter for the name of 
	 * the log this method is called on.
	 */
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
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Prints the specified values to a file
	 * 
	 * @param values
	 */
	protected abstract void log(String... values);
	
	/**
	 *Makes the initial print in the log file
	 */
	protected abstract void initLog();
}
