package com.lutz.nautilus.builders.script;

import java.util.ArrayList;
import java.util.List;

public class BuildValues {

	private static List<Throwable> throwables = new ArrayList<Throwable>();
	private static boolean hasErrored = false;

	public static void addThrowable(Throwable t) {

		throwables.add(t);
		hasErrored = true;
	}

	public static Throwable[] getThrowables() {

		return throwables.toArray(new Throwable[] {});
	}
	
	public static boolean hasErrored(){
		
		return hasErrored;
	}
	
	public static void clearThrowables(){
		
		throwables.clear();
	}
}
