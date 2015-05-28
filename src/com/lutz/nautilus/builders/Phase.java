package com.lutz.nautilus.builders;

import com.lutz.nautilus.builders.script.BuildValues;

public enum Phase {

	FINISH_SUCCESS(null, "finish-success"), FINISH_FAIL(null, "finish-fail"), CLEANUP(
			FINISH_SUCCESS, "clean", "cleanup", "clean-up"), GEN_SOURCE(
			CLEANUP, "gen-src", "generate-src", "gen-source", "generate-source"), PACKAGE(
			GEN_SOURCE, "package", "package-jar"), COMPILE(PACKAGE, "compile"), MOVE_SOURCES(
			COMPILE, "move-src", "move-source", "move-sources");

	private String[] strings;
	private Phase nextPhase;

	private Phase(Phase nextPhase, String... strings) {

		this.nextPhase = nextPhase;
		this.strings = strings;
	}

	public String[] getStrings() {

		return strings;
	}

	public Phase getNextPhase() {

		if (this == CLEANUP) {

			return BuildValues.hasErrored() ? FINISH_FAIL : FINISH_SUCCESS;

		} else {

			return nextPhase;
		}
	}

	public static Phase getPhaseForString(String string) {

		for (Phase phase : Phase.values()) {

			for (String str : phase.getStrings()) {

				if (str.equalsIgnoreCase(string)) {

					return phase;
				}
			}
		}

		return CLEANUP;
	}
}
