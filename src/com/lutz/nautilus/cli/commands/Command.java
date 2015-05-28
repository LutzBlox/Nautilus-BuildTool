package com.lutz.nautilus.cli.commands;

import java.util.Map;

public abstract class Command {

	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract String getUsage();
	
	public abstract int getMinimumArgumentNumber();
	
	public abstract Map<String, String> getArgDescriptions();
	
	public abstract Map<String, String> getFlagDescriptions();
	
	public abstract void onExecute(FlagList flags, String[] args);
}
