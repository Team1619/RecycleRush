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

	private String[] fHeaders;
	
	/**
	 * @param logName is the name of the file that the data will be written to
	 * @param headers are printed at the top of each column of data
	 */
	public UDataCollector(String logName, String... headers) {
		super(logName);
		
		this.fHeaders = new String[headers.length + 1];
		this.fHeaders[0] = "Timestamp [s]";
		for (int i = 0; i < headers.length; i++) {
			this.fHeaders[i + 1] = headers[i];
		}

		nextLog();
	}
	
	/**
	 * Prints the headers at the top of each column
	 */
	protected void initLog() {
		printHeaders(fHeaders);
	}

	/**
	 * Prints the series of headers separated by columns to head 
	 * each column in the comma separated values.
	 * 
	 * @param values the set of headers
	 */
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

	/**
	 * Prints the specified values separated by commas into the columns under each header.
	 */
	public void log(String... values) {
		printCSV(values);
	}
	
	/**
	 * @param values are printed to the file with a comma between each one
	 */
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
