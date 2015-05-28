package com.lutz.nautilus.cli;

import java.util.ArrayList;
import java.util.List;

import com.lutz.nautilus.cli.commands.Command;
import com.lutz.nautilus.cli.commands.Flag;
import com.lutz.nautilus.cli.commands.FlagList;
import com.lutz.nautilus.cli.commands.builtin.Build;
import com.lutz.nautilus.cli.commands.builtin.Help;
import com.lutz.nautilus.cli.commands.builtin.Setup;
import com.lutz.nautilus.cli.commands.builtin.Update;


public class NautilusCLI {

	private static List<Command> commands = new ArrayList<Command>();

	public static void registerDefaultCommands() {

		registerCommand(new Help());
		registerCommand(new Setup());
		registerCommand(new Update());
		registerCommand(new Build());
	}

	public static void registerCommand(Command command) {

		commands.add(command);
	}

	public static boolean hasCommand(String name) {

		for (Command c : commands) {

			if (c.getName().equalsIgnoreCase(name)) {

				return true;
			}
		}

		return false;
	}

	public static Command getCommand(String name) {

		for (Command c : commands) {

			if (c.getName().equalsIgnoreCase(name)) {

				return c;
			}
		}

		return null;
	}

	public static Command[] getCommands() {

		return commands.toArray(new Command[] {});
	}

	public static void runCommand(String command) {

		String[] comParts = command.split(" ");

		if (comParts.length > 0) {

			String commandName = comParts[0];

			List<String> args = new ArrayList<String>();

			FlagList flags = new FlagList();

			Flag currentFlag = null;

			for (int i = 1; i < comParts.length; i++) {

				String part = comParts[i];

				if (part.startsWith("-")) {

					if (part.length() > 1) {

						Flag flag = new Flag(part.substring(1));

						flags.addFlag(flag);

						currentFlag = flag;

					} else {

						if (currentFlag != null) {

							currentFlag.addArgument(part);

						} else {

							args.add(part);
						}
					}

				} else {

					if (currentFlag != null) {

						currentFlag.addArgument(part);

					} else {

						args.add(part);
					}
				}
			}

			if (hasCommand(commandName)) {

				Command c = getCommand(commandName);

				if (args.size() >= c.getMinimumArgumentNumber()) {

					if (flagsMatch(flags, c)) {

						c.onExecute(flags,
								args.toArray(new String[] {}));

					}

				} else {

					CLIResponse.invalidNumArgs();
				}

			} else {

				CLIResponse.invalidCommand();
			}

		} else {

			CLIResponse.invalidCommand();
		}
	}

	private static boolean flagsMatch(FlagList flags, Command c) {

		for (Flag flag : flags) {

			if (!c.getFlagDescriptions().containsKey(flag.getName())) {

				CLIResponse.invalidFlag(flag.getName());

				return false;
			}
		}

		return true;
	}
}
