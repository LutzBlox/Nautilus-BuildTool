package com.lutz.nautilus.loggers;

public class BoundLogger extends Logger {

	private Logger[] loggers;

	public BoundLogger(Logger... loggers) {

		super(System.out);

		this.loggers = loggers;
	}

	public Logger[] getBoundLoggers() {

		return loggers;
	}

	@Override
	public void setClockType(ClockType type) {

		for (Logger l : loggers) {

			l.setClockType(type);
		}
	}

	@Override
	public void log(String toLog, String... substitutions) {

		for (Logger l : loggers) {

			l.log(toLog, substitutions);
		}
	}

	@Override
	public void log(String toLog, Level level, String... substitutions) {

		for (Logger l : loggers) {

			l.log(toLog, level, substitutions);
		}
	}

	@Override
	public void log(String toLog, Level level, Verbosity verbosity,
			String... substitutions) {

		for (Logger l : loggers) {

			l.log(toLog, level, verbosity, substitutions);
		}
	}

	@Override
	public void debug(String toLog, String... substitutions) {

		for (Logger l : loggers) {

			l.debug(toLog, substitutions);
		}
	}

	@Override
	public void info(String toLog, String... substitutions) {

		for (Logger l : loggers) {

			l.info(toLog, substitutions);
		}
	}

	@Override
	public void info(String toLog, Verbosity verbosity, String... substitutions) {

		for (Logger l : loggers) {

			l.info(toLog, verbosity, substitutions);
		}
	}

	@Override
	public void warn(String toLog, String... substitutions) {

		for (Logger l : loggers) {

			l.warn(toLog, substitutions);
		}
	}

	@Override
	public void warn(String toLog, Verbosity verbosity, String... substitutions) {

		for (Logger l : loggers) {

			l.warn(toLog, verbosity, substitutions);
		}
	}

	@Override
	public void severe(String toLog, String... substitutions) {

		for (Logger l : loggers) {

			l.severe(toLog, substitutions);
		}
	}

	@Override
	public void severe(String toLog, Verbosity verbosity,
			String... substitutions) {

		for (Logger l : loggers) {

			l.severe(toLog, verbosity, substitutions);
		}
	}

	@Override
	public void fatal(String toLog, String... substitutions) {

		for (Logger l : loggers) {

			l.fatal(toLog, substitutions);
		}
	}

	@Override
	public void logException(Throwable exception) {

		for (Logger l : loggers) {

			l.logException(exception);
		}
	}

	@Override
	public void logCrash(Throwable crash) {

		for (Logger l : loggers) {

			l.logCrash(crash);
		}
	}

	@Override
	public Timestamp getTimestamp() {

		if (loggers.length > 0) {

			return loggers[0].getTimestamp();

		} else {

			return null;
		}
	}
}
