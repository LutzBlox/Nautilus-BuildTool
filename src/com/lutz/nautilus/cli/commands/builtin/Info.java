package com.lutz.nautilus.cli.commands.builtin;

import java.util.HashMap;
import java.util.Map;

import com.lutz.nautilus.cli.commands.Command;
import com.lutz.nautilus.cli.commands.FlagList;


public class Info extends Command {

	@Override
	public String getName() {

		return "info";
	}

	@Override
	public String getDescription() {
		
		return "Gives information on the current Nautilus installation";
	}

	@Override
	public String getUsage() {

		return "info";
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
		
		
	}
}
