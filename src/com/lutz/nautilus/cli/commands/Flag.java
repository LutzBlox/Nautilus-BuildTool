package com.lutz.nautilus.cli.commands;

import java.util.ArrayList;
import java.util.List;

public class Flag {

	private String name;
	
	private List<String> args = new ArrayList<String>();
	
	public Flag(String name){
		
		this.name = name;
	}
	
	public String getName(){
		
		return name;
	}
	
	public void addArgument(String arg){
		
		args.add(arg);
	}
	
	public String[] getArguments(){
		
		return args.toArray(new String[]{});
	}
}
