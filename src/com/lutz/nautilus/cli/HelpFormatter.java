package com.lutz.nautilus.cli;

import com.lutz.nautilus.cli.commands.Command;

public class HelpFormatter {

	public static String getFormattedHelp() {

		String help = "";

		for (Command c : NautilusCLI.getCommands()) {

			help += "=====[ " + c.getName().toLowerCase() + " ]=====\n";

			help += "\n" + c.getDescription() + "\n";

			help += "\nUsage: " + c.getUsage() + "\n";

			help += "\nArguments:\n";

			if (c.getArgDescriptions().keySet().size() > 0) {

				for (String argName : c.getArgDescriptions().keySet()) {

					help += "\t"
							+ argName
							+ ": "
							+ c.getArgDescriptions().get(argName)
									.replace("\n", "\n\t") + "\n";
				}

			} else {

				help += "\tNo arguments.\n";
			}

			help += "\nPossible Flags:\n";

			if (c.getFlagDescriptions().keySet().size() > 0) {

				for (String flagName : c.getFlagDescriptions().keySet()) {

					help += "\t-"
							+ flagName
							+ ": "
							+ c.getFlagDescriptions().get(flagName)
									.replace("\n", "\n\t") + "\n";
				}

			} else {

				help += "\tNo possible flags.\n";
			}

			help += "\n";
		}

		return help;
	}

	public static String getFormattedHelp(Command c) {

		String help = "";

		help += "=====[ " + c.getName().toLowerCase() + " ]=====\n";

		help += "\n" + c.getDescription() + "\n";

		help += "\nUsage: " + c.getUsage() + "\n";

		help += "\nArguments:\n";

		if (c.getArgDescriptions().keySet().size() > 0) {

			for (String argName : c.getArgDescriptions().keySet()) {

				help += "\t"
						+ argName
						+ ": "
						+ c.getArgDescriptions().get(argName)
								.replace("\n", "\n\t") + "\n";
			}

		} else {

			help += "\tNo arguments.\n";
		}

		help += "\nPossible Flags:\n";

		if (c.getFlagDescriptions().keySet().size() > 0) {

			for (String flagName : c.getFlagDescriptions().keySet()) {

				help += "\t-"
						+ flagName
						+ ": "
						+ c.getFlagDescriptions().get(flagName)
								.replace("\n", "\n\t") + "\n";
			}

		} else {

			help += "\tNo possible flags.\n";
		}

		help += "\n";

		return help;
	}
}
