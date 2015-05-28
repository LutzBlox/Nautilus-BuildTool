package com.lutz.nautilus.cli;

import com.lutz.nautilus.loggers.LoggingUtils;

public class CLIResponse {

	public static void invalidCommand() {

		LoggingUtils.SYSTEM_OUT.log("Invalid command!");
	}

	public static void printHelp() {

		LoggingUtils.SYSTEM_OUT.log(HelpFormatter.getFormattedHelp());
	}

	public static void invalidNumArgs() {

		LoggingUtils.SYSTEM_OUT.log("Invalid number of arguments!");
	}

	public static void invalidFlag(String name) {

		LoggingUtils.SYSTEM_OUT.log("The flag '-" + name + "' is not valid!");
	}

	public static void missingFlag(String name) {

		LoggingUtils.SYSTEM_OUT.log("The flag '-" + name
				+ "' is missing and is required!");
	}
}
