package com.lutz.nautilus.loggers;

import java.io.PrintStream;
import java.util.Calendar;

public class Logger {

	private PrintStream printStream;

	private boolean useTimestamp = true, useLevel = true;

	private ClockType clockType = ClockType.TWELVE_HOUR;

	public Logger(PrintStream printStream) {

		this(printStream, true, true);
	}

	public Logger(PrintStream printStream, boolean useLevel) {

		this(printStream, useLevel, true);
	}

	public Logger(PrintStream printStream, boolean useLevel,
			boolean useTimestamp) {

		this.printStream = printStream;
		setUseLevel(useLevel);
		setUseTimestamp(useTimestamp);
	}

	public void setUseTimestamp(boolean useTimestamp) {

		this.useTimestamp = useTimestamp;
	}

	public boolean getUseTimestamp() {

		return useTimestamp;
	}

	public void setUseLevel(boolean useLevel) {

		this.useLevel = useLevel;
	}

	public boolean getUseLevel() {

		return useLevel;
	}

	public void setClockType(ClockType type) {

		clockType = type;
	}

	public void log(String toLog, String... substitutions) {

		log(toLog, Level.INFO, substitutions);
	}

	public void log(String toLog, Level level, String... substitutions) {

		log(toLog, level, Verbosity.NORMAL, substitutions);
	}

	public void log(String toLog, Level level, Verbosity verbosity,
			String... substitutions) {

		if (LoggingUtils.canLog()) {

			for (int i = 0; i < substitutions.length; i++) {

				toLog = toLog.replaceAll("\\{" + i + "\\}",
						substitutions[i].replaceAll("\\\\", "\\\\\\\\"));
			}

			String timestampStr = "[" + getTimestamp().toString() + "]";

			String levelStr = "[" + level.toString() + "]";

			String begin = "";

			if (useTimestamp || useLevel) {

				if (useTimestamp) {

					begin += timestampStr;
				}

				if (useLevel) {

					begin += levelStr;
				}

				begin += "\t";
			}

			printStream.println(begin + toLog);
		}
	}

	public void debug(String toLog, String... substitutions) {

		log(toLog, Level.DEBUG, Verbosity.VERBOSE, substitutions);
	}

	public void info(String toLog, String... substitutions) {

		log(toLog, Level.INFO, substitutions);
	}

	public void info(String toLog, Verbosity verbosity, String... substitutions) {

		log(toLog, Level.INFO, verbosity, substitutions);
	}

	public void warn(String toLog, String... substitutions) {

		log(toLog, Level.WARN, substitutions);
	}

	public void warn(String toLog, Verbosity verbosity, String... substitutions) {

		log(toLog, Level.WARN, verbosity, substitutions);
	}

	public void severe(String toLog, String... substitutions) {

		log(toLog, Level.SEVERE, substitutions);
	}

	public void severe(String toLog, Verbosity verbosity,
			String... substitutions) {

		log(toLog, Level.SEVERE, verbosity, substitutions);
	}

	public void fatal(String toLog, String... substitutions) {

		log(toLog, Level.FATAL, Verbosity.MINIMUM, substitutions);
	}

	public void logException(Throwable exception) {

		log(exception.getClass().getName() + ": " + exception.getMessage(),
				Level.SEVERE);

		for (StackTraceElement ste : exception.getStackTrace()) {

			log("\tin " + ste.getClassName() + "." + ste.getMethodName()
					+ "() at line " + ste.getLineNumber(), Level.SEVERE);
		}
	}

	public void logCrash(Throwable crash) {

		log("A fatal " + crash.getClass().getName() + " occurred: "
				+ crash.getMessage(), Level.FATAL);

		for (StackTraceElement ste : crash.getStackTrace()) {

			log("\tin " + ste.getClassName() + "." + ste.getMethodName()
					+ "() at line " + ste.getLineNumber(), Level.FATAL);
		}
	}

	public Timestamp getTimestamp() {

		Calendar c = Calendar.getInstance();

		boolean twelveHour = false;

		if (clockType.equals(ClockType.TWELVE_HOUR)) {

			twelveHour = true;
		}

		int hour = twelveHour ? c.get(Calendar.HOUR) : c
				.get(Calendar.HOUR_OF_DAY);

		return new Timestamp(hour, c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND), c.get(Calendar.DATE),
				c.get(Calendar.MONTH), c.get(Calendar.YEAR));
	}
}
