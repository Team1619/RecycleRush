package org.usfirst.frc.team1619;

import java.io.IOException;

//Lumberjacks are made for logging
//Use Lumberjacks for logging
//Helpful comments by TimE wing

//To get logs from a Lumberjack, first initialize it,
//then call lumberjack.log("values") to push those
//values to the log file
//lumberjack.changeLog() will close the files and make
//new ones.

public class UDataCollector extends UGenericLogger{

	private String[] headers;

	public UDataCollector(String logName, String... headers) {
		super(logName);
		this.headers = new String[headers.length + 1];
		this.headers[0] = "Timestamp [s]";
		for (int i = 0; i < headers.length; i++) {
			this.headers[i + 1] = headers[i];
		}
		nextLog();
	}
	
	protected void initLog() {
		printHeaders(headers);
	}
	
	private void printCSV(String[] values) {
		if (getFileWriter() == null)
			return;
		try {
			getFileWriter().append(getTimestamp());
			for (String s : values) {
				getFileWriter().append(',');
				getFileWriter().append(s);
			}
			getFileWriter().append('\n');
			getFileWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void printHeaders(String[] values) {
		if (getFileWriter() == null)
			return;
		try {
			boolean firstCall = true;
			for (String s : values) {
				if (firstCall)
					firstCall = false;
				else
					getFileWriter().append(',');
				getFileWriter().append(s);
			}
			getFileWriter().append('\n');
			getFileWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void log(String... values) {
		printCSV(values);
	}

}