package com.lutz.nautilus.cli.commands.builtin;

import java.util.HashMap;
import java.util.Map;

import com.lutz.nautilus.cli.ArrayHelper;
import com.lutz.nautilus.cli.commands.Command;
import com.lutz.nautilus.cli.commands.FlagList;
import com.lutz.nautilus.xml.XMLUtils;

public class Update extends Command {

	@Override
	public String getName() {

		return "update";
	}

	@Override
	public String getDescription() {

		return "Allows changes to certain parts of the project";
	}

	@Override
	public String getUsage() {

		return "update -r -i [id] -n [name] -v [version] -e [developer] -c [description] -d [dist jar file] -s [dist src jar file]";
	}

	@Override
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

		flags.put(
				"r",
				"Refreshes the file references in the project's project.xml. Requires no arguments. (Optional)");

		flags.put("i", "Updates the id of the project");

		flags.put("n", "Updates the name of the project");

		flags.put("v", "Updates the version of the project");

		flags.put("e", "Updates the developer of the project");

		flags.put("c", "Updates the description of the project");

		flags.put("d", "Updates the name of the distributed jar file");

		flags.put("s", "Updates the name of the distributed source jar file");

		return flags;
	}

	@Override
	public void onExecute(FlagList flags, String[] args) {

		boolean updateId = false, updateName = false, updateVersion = false, updateDeveloper = false, updateDescription = false, updateDistName = false, updateDistSrcName = false, updateFileRef = false;

		String id = "", name = "", version = "", developer = "", description = "", distName = "", distSrcName = "";

		if (flags.hasFlag("r")) {

			updateFileRef = true;
		}

		if (flags.hasFlag("i")) {

			updateId = true;

			id = ArrayHelper
					.getArrayAsString(flags.getFlag("i").getArguments());
		}

		if (flags.hasFlag("n")) {

			updateName = true;

			name = ArrayHelper.getArrayAsString(flags.getFlag("n")
					.getArguments());
		}

		if (flags.hasFlag("v")) {

			updateVersion = true;

			version = ArrayHelper.getArrayAsString(flags.getFlag("v")
					.getArguments());
		}

		if (flags.hasFlag("d")) {

			updateDistName = true;

			distName = ArrayHelper.getArrayAsString(flags.getFlag("d")
					.getArguments());
		}

		if (flags.hasFlag("s")) {

			updateDistSrcName = true;

			distSrcName = ArrayHelper.getArrayAsString(flags.getFlag("s")
					.getArguments());
		}

		if (flags.hasFlag("e")) {

			updateDeveloper = true;

			developer = ArrayHelper.getArrayAsString(flags.getFlag("e")
					.getArguments());
		}

		if (flags.hasFlag("c")) {

			updateDescription = true;

			description = ArrayHelper.getArrayAsString(flags.getFlag("c")
					.getArguments());
		}

		XMLUtils.updateProjectXML(updateId, id, updateName, name,
				updateVersion, version, updateDeveloper, developer,
				updateDescription, description, updateDistName, distName,
				updateDistSrcName, distSrcName, updateFileRef);
	}
}
