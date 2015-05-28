package com.lutz.nautilus.settings;

public class SettingsEntry {

	private String name, value;
	
	public SettingsEntry(String name, String value){
		
		this.name = name;
		this.value = value;
	}
	
	public String getName(){
		
		return name;
	}
	
	public String getValue(){
		
		return value;
	}
}
