package com.lutz.nautilus.xml.data;

public enum DependencyPackageType {

	EXTERNAL("e", "ext", "external"), INTERNAL("i", "int", "internal");
	
	private String[] strs;
	
	private DependencyPackageType(String... strs){
		
		this.strs = strs;
	}
	
	public String[] getStrings(){
		
		return strs;
	}
	
	public static DependencyPackageType parse(String str){
		
		for(DependencyPackageType type : values()){
			
			for(String string : type.getStrings()){
				
				if(string.equalsIgnoreCase(str)){
					
					return type;
				}
			}
		}
		
		return EXTERNAL;
	}
}
