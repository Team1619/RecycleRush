package org.usfirst.frc.team1619;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

//Lumberjacks are made for logging
//Use Lumberjacks for logging
//Helpful comments by TimE wing

//To get logs from a Lumberjack, first initialize it,
//then call lumberjack.log("values") to push those
//values to the log file
//lumberjack.changeLog() will close the files and make
//new ones.

public class Lumberjack {
	
	private static final String logFolderPath = "/home/lvuser/log/";
	private static final int maxLogs = 50;
	private static final ArrayList<Lumberjack> lumberjacks = new ArrayList<Lumberjack>();
	
	private static String logGroup = getDateString();
	
	private FileWriter fileWriter;
	private String logName;
	private String[] headers;
	private long start;
	
	public Lumberjack(String logName, String ... headers) {
		this.logName = new String(logName);
		this.headers = new String[headers.length + 1];
		this.headers[0] = "Timestamp [s]";
		for(int i = 0; i < headers.length; i++) {
			this.headers[i + 1] = headers[i];
		}
		lumberjacks.add(this);
		nextLog();
	}
	
	public void log(String ... values) {
		printCSV(values);
	}
	
	static void deleteFile (File folder) {
		if (folder.isDirectory()) {
			 File[] files = folder.listFiles();
			 for(File file : files) {
				 deleteFile(file);
			 }
		}
		try {
			folder.delete();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void cleanUp() {
		try {
			File logFolder = new File(logFolderPath);
			File[] logPaths = logFolder.listFiles();
			Arrays.sort(logPaths);
			for (int i = 0; i < (logPaths.length-maxLogs); i++)
				deleteFile(logPaths[i]);
		}
		catch (Exception e) {
			
		}
	}
	
	private static String getDateString() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
		date.setTimeZone(TimeZone.getTimeZone("UTC"));
		return date.format(new Date());
	}
	
	private String getTimestamp() {
		//SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
		//date.setTimeZone(TimeZone.getTimeZone("UTC"));
		//return date.format(new Date());
		return String.format("%.3f", (double)(System.currentTimeMillis()-start) / 1000);
	}
	
	public static void changeLogs() {
		logGroup = getDateString();
		for (Lumberjack l : lumberjacks)
			l.nextLog();
		cleanUp();
	}
	
	private void nextLog() {
		try {
			if (fileWriter != null)
					fileWriter.close();
			File logDir = new File(logFolderPath + logGroup);
			logDir.mkdir();
			if(logDir.exists()) {
				fileWriter = new FileWriter(logFolderPath + logGroup + "/" + logName);
				printHeaders(headers);
				start = System.currentTimeMillis();
			}
			else {
				System.err.println("Cannot create log folder " + logName);
				fileWriter = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printCSV(String[] values) {
		if(fileWriter == null)
			return;
		try {
			fileWriter.append(getTimestamp());
			for(String s : values) {
				fileWriter.append(',');
				fileWriter.append(s);
			}
			fileWriter.append('\n');
			fileWriter.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void printHeaders(String[] values) {
		if(fileWriter == null)
			return;
		try {
			boolean firstCall = true;
			for(String s : values) {
				if(firstCall)
					firstCall = false;
				else
					fileWriter.append(',');
				fileWriter.append(s);
			}
			fileWriter.append('\n');
			fileWriter.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
