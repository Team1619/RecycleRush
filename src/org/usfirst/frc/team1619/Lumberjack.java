package org.usfirst.frc.team1619;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

//Lumberjacks are made for logging
//Use Lumberjacks for logging
//Helpful comments by TimE wing

public class Lumberjack {
	
	private static final String logFolderPath = "/home/lvuser/log/";
	private static final int maxLogs = 10;
	private static final ArrayList<Lumberjack> lumberjacks = new ArrayList<Lumberjack>();
	
	private static String logGroup = getDateString();
	
	private FileWriter fileWriter;
	private String logName;
	private String[] headers;
	
	public Lumberjack(String logName, String[] headers) {
		this.logName = new String(logName);
		this.headers = new String[headers.length + 1];
		this.headers[0] = "Timestamp";
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
	
	public static void cleanUp() {
		File logFolder = new File(logFolderPath);
		File[] logPaths = logFolder.listFiles();
		Arrays.sort(logPaths);
		for (int i = 0; i < (logPaths.length-maxLogs); i++)
			deleteFile(logPaths[i]);
	}
	
	private static String getDateString() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss-SSS");
		return date.format(new Date());
	}
	
	public static void changeLogs() {
		logGroup = getDateString();
		new File(logFolderPath + logGroup).mkdir();
		for (Lumberjack l : lumberjacks)
			l.nextLog();
		cleanUp();
	}
	
	private void nextLog() {
		try {
			if (fileWriter != null)
					fileWriter.close();
			fileWriter = new FileWriter(logFolderPath + logGroup + "/" + logName);
			printHeaders(headers);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printCSV(String[] values) {
		try {
			fileWriter.append(getDateString());
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
