package org.usfirst.frc.team1619;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Lumberjacks are made for logging
//Use Lumberjacks for logging
//Helpful comments by TimE wing

//To get logs from a Lumberjack, first initialize it,
//then call lumberjack.log("values") to push those
//values to the log file
//lumberjack.changeLog() will close the files and make
//new ones.

public class UDataCollector extends UGenericLogger{

	private String[] fHeaders;
	private static final List<UDataCollector> sDataLoggers = new ArrayList<>();
	
	public UDataCollector(String logName, String... headers) {
		super(logName);
		this.fHeaders = new String[headers.length + 1];
		this.fHeaders[0] = "Timestamp [s]";
		for (int i = 0; i < headers.length; i++) {
			this.fHeaders[i + 1] = headers[i];
		}
		sDataLoggers.add(this);

		nextLog();
	}
	
	protected void initLog() {
		printHeaders(fHeaders);
	}

	private void printHeaders(String[] values) {
		if (fFileWriter == null)
			return;
		try {
			boolean firstCall = true;
			for (String s : values) {
				if (firstCall)
					firstCall = false;
				else
					fFileWriter.append(',');
				fFileWriter.append(s);
			}
			fFileWriter.append('\n');
			fFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void changeLogs() {
		sLogFolder = getDateString();
		for (UDataCollector l : sDataLoggers)
			l.nextLog();
		cleanUp();
	}

	@Override
	public void log(String... values) {
		printCSV(values);
	}
	
	private void printCSV(String[] values) {
		if (fFileWriter == null)
			return;
		try {
			fFileWriter.append(getDateString());
			for (String s : values) {
				fFileWriter.append(',');
				fFileWriter.append(s);
			}
			fFileWriter.append('\n');
			fFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
