package com.lutz.nautilus.cli;

public class ArrayHelper {

	public static String getArrayAsString(String[] array) {

		String full = "";

		for (int i = 0; i < array.length; i++) {

			full += array[i];

			if (i < array.length - 1) {

				full += " ";
			}
		}

		return full;
	}
}
