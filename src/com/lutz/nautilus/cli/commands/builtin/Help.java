package com.lutz.nautilus.cli.commands.builtin;

import java.util.HashMap;
import java.util.Map;

import com.lutz.nautilus.cli.CLIResponse;
import com.lutz.nautilus.cli.HelpFormatter;
import com.lutz.nautilus.cli.NautilusCLI;
import com.lutz.nautilus.cli.commands.Command;
import com.lutz.nautilus.cli.commands.FlagList;
import com.lutz.nautilus.loggers.LoggingUtils;


public class Help extends Command {

	@Override
	public String getName() {

		return "help";
	}

	@Override
	public String getDescription() {

		return "Shows help for Nautilus commands";
	}

	@Override
	public String getUsage() {

		return "help [command]";
	}
	
	public int getMinimumArgumentNumber(){
		
		return 0;
	}

	@Override
	public Map<String, String> getArgDescriptions() {

		Map<String, String> args = new HashMap<String, String>();

		args.put("command",
				"The name of the command to give help for. (Optional)");

		return args;
	}

	@Override
	public Map<String, String> getFlagDescriptions() {

		return new HashMap<String, String>();
	}

	@Override
	public void onExecute(FlagList flags, String[] args) {

		if (args.length == 0) {

			CLIResponse.printHelp();

		} else {

			if (NautilusCLI.hasCommand(args[0])) {

				LoggingUtils.SYSTEM_OUT.log(HelpFormatter
						.getFormattedHelp(NautilusCLI.getCommand(args[0])));

			} else {

				LoggingUtils.SYSTEM_OUT.log("'" + args[0]
						+ "' is not a valid command!");
			}
		}
	}
}
