package org.usfirst.frc.team1619;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Lumberjack {
	
	private static final String logFolderPath = "/home/lvuser/log/";
	private static final int maxLogs = 10;
	private static final ArrayList<Lumberjack> lumberjacks = new ArrayList<Lumberjack>();
	
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
	
	public static String getDateString() {
		
		return "today";
	}
	
	private static void changeLogs() {
		for (Lumberjack l : lumberjacks)
			l.nextLog();
	}
	
	private FileWriter fileWriter;
	private String logName;
	
	private void nextLog() {
		try {
			if (fileWriter != null)
					fileWriter.close();
			fileWriter = new FileWriter(logFolderPath + getDateString() + "/" + logName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Lumberjack(String logName) {
		this.logName = logName;
		lumberjacks.add(this);
	}
	
}
