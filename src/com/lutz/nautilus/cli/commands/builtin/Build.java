package com.lutz.nautilus.cli.commands.builtin;

import java.util.HashMap;
import java.util.Map;

import com.lutz.nautilus.builders.NautilusBuilder;
import com.lutz.nautilus.cli.commands.Command;
import com.lutz.nautilus.cli.commands.FlagList;
import com.lutz.nautilus.xml.ProjectXML;
import com.lutz.nautilus.xml.XMLUtils;


public class Build extends Command {

	@Override
	public String getName() {

		return "build";
	}

	@Override
	public String getDescription() {

		return "Builds the project into a jar file";
	}

	@Override
	public String getUsage() {

		return "build";
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

		return new HashMap<String, String>();
	}

	@Override
	public void onExecute(FlagList flags, String[] args) {

		ProjectXML xml = XMLUtils.parseProjectXML();

		if (xml != null) {

			NautilusBuilder.runBuildSequence(xml);
		}
	}
}
