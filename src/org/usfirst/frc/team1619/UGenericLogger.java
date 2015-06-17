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
	private static final String LOG_FOLDER_PATH = "/home/lvuser/log/";
	private static SimpleDateFormat sDateFormat;

	private FileWriter fFileWriter;
	private String fLogName;

	static {
		sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
		sDateFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
	}

	protected abstract void initLog();

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
			File logDir = new File(LOG_FOLDER_PATH + sLogGroup);
			logDir.mkdir();
			if (logDir.exists()) {
				fFileWriter = new FileWriter(LOG_FOLDER_PATH + sLogGroup + "/"
						+ fLogName);
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

	protected FileWriter getFileWriter() {
		return fFileWriter;
	}
}
