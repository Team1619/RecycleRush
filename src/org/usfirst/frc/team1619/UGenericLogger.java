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

	private static SimpleDateFormat sDateFormat;
	private static final String logFolderPath = "/home/lvuser/log/";
	private static final int maxLogs = 50;
	private static final List<UGenericLogger> lumberjacks = new ArrayList<>();

	private static String logGroup = getDateString();

	private FileWriter fileWriter;
	private String logName;

	static {
		sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
		sDateFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
	}

	public UGenericLogger(String logName) {
		this.logName = new String(logName);
		lumberjacks.add(this);
	}
	
	protected abstract void initLog();

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

	private static void cleanUp() {
		try {
			File logFolder = new File(logFolderPath);
			File[] logPaths = logFolder.listFiles();
			Arrays.sort(logPaths);
			for (int i = 0; i < (logPaths.length - maxLogs); i++)
				deleteFile(logPaths[i]);
		} catch (Exception e) {

		}
	}

	private static String getDateString() {
		return sDateFormat.format(new Date());
	}

	protected String getTimestamp() {
		return sDateFormat.format(new Date());
		// return String.format("%.3f",
		// (double)(System.currentTimeMillis()-start) / 1000);
	}

	public static void changeLogs() {
		logGroup = getDateString();
		for (UGenericLogger l : lumberjacks)
			l.nextLog();
		cleanUp();
	}

	protected void nextLog() {
		try {
			if (fileWriter != null)
				fileWriter.close();
			File logDir = new File(logFolderPath + logGroup);
			logDir.mkdir();
			if (logDir.exists()) {
				fileWriter = new FileWriter(logFolderPath + logGroup + "/"
						+ logName);
				initLog();
			}
			else {
				System.err.println("Cannot create log folder " + logName);
				fileWriter = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected FileWriter getFileWriter() {
		return fileWriter;
	}
	
}
