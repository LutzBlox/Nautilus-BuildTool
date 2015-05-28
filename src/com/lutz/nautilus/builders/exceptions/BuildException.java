package com.lutz.nautilus.builders.exceptions;

public class BuildException extends RuntimeException{

	private static final long serialVersionUID = -3623395638398029519L;

	public BuildException(String message){
		
		super(message);
		
		this.setStackTrace(new StackTraceElement[]{});
	}
}
