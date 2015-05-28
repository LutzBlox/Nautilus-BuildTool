package com.lutz.nautilus.cli.commands.builtin;

import java.util.HashMap;
import java.util.Map;

import com.lutz.nautilus.cli.ArrayHelper;
import com.lutz.nautilus.cli.CLIResponse;
import com.lutz.nautilus.cli.commands.Command;
import com.lutz.nautilus.cli.commands.FlagList;
import com.lutz.nautilus.xml.XMLUtils;

public class Setup extends Command {

	@Override
	public String getName() {

		return "setup";
	}

	@Override
	public String getDescription() {

		return "Generates a project.xml for the project and sets it up for Nautilus";
	}

	@Override
	public String getUsage() {

		return "setup -i [id] -n [name] -v [version] -e [developer] -c [description] -d [dist jar file] -s [dist src jar file]";
	}

	public int getMinimumArgumentNumber() {

		return 0;
	}

	@Override
	public Map<String, String> getArgDescriptions() {

		return new HashMap<String, String>();
	}

	@Override
	public Map<String, String> getFlagDescriptions() {

		Map<String, String> flags = new HashMap<String, String>();

		flags.put("i", "The id of the project");

		flags.put("n", "The name of the project");

		flags.put("v", "The version of the project");

		flags.put("e", "Updates the developer of the project");

		flags.put("c", "Updates the description of the project");

		flags.put("d", "The name of the distributed jar file");

		flags.put("s", "The name of the distributed source jar file");

		return flags;
	}

	@Override
	public void onExecute(FlagList flags, String[] args) {

		String id = null, name = null, version = null, developer = null, description = null, distName = null, distSrcName = null;

		if (flags.hasFlag("i")) {

			id = ArrayHelper
					.getArrayAsString(flags.getFlag("i").getArguments());

		} else {

			CLIResponse.missingFlag("i");

			return;
		}

		if (flags.hasFlag("n")) {

			name = ArrayHelper.getArrayAsString(flags.getFlag("n")
					.getArguments());

		} else {

			CLIResponse.missingFlag("n");

			return;
		}

		if (flags.hasFlag("v")) {

			version = ArrayHelper.getArrayAsString(flags.getFlag("v")
					.getArguments());

		} else {

			CLIResponse.missingFlag("v");

			return;
		}

		if (flags.hasFlag("v")) {

			version = ArrayHelper.getArrayAsString(flags.getFlag("v")
					.getArguments());

		} else {

			CLIResponse.missingFlag("v");

			return;
		}

		if (flags.hasFlag("d")) {

			distName = ArrayHelper.getArrayAsString(flags.getFlag("d")
					.getArguments());

		} else {

			CLIResponse.missingFlag("d");

			return;
		}

		if (flags.hasFlag("s")) {

			distSrcName = ArrayHelper.getArrayAsString(flags.getFlag("s")
					.getArguments());

		} else {

			CLIResponse.missingFlag("s");

			return;
		}

		if (flags.hasFlag("e")) {

			developer = ArrayHelper.getArrayAsString(flags.getFlag("e")
					.getArguments());

		} else {

			CLIResponse.missingFlag("e");

			return;
		}

		if (flags.hasFlag("c")) {

			description = ArrayHelper.getArrayAsString(flags.getFlag("c")
					.getArguments());

		} else {

			CLIResponse.missingFlag("c");

			return;
		}

		XMLUtils.generateAndWriteProjectXML(name, id, version, developer, description, distName,
				distSrcName);
	}
}
