package com.lutz.nautilus.builders.script;

import java.util.Map;

import javax.tools.ToolProvider;

import com.lutz.easyxml.XMLTag;
import com.lutz.nautilus.builders.NautilusBuilder;
import com.lutz.nautilus.builders.Phase;
import com.lutz.nautilus.builders.script.BuildScript.BuildPhase;
import com.lutz.nautilus.loggers.LoggingUtils;
import com.lutz.nautilus.xml.ProjectXML;

public class ScriptUtils {

	public static Phase parseScript(ProjectXML xml, BuildPhase phase,
			XMLTag[] tags, boolean allowPhaseRun, boolean allowErrorLog,
			String[] miscKeys, String[] miscValues) {

		for (XMLTag tag : tags) {

			switch (tag.getName()) {

			case "echo":

				if (tag.getValue() != null) {

					LoggingUtils.SYSTEM_OUT.info(parseString(tag.getValue(),
							xml.getStringKeys(), miscKeys, miscValues));
				}

				break;

			case "if":

				if (tag.hasAttribute("eval")) {

					if (evaluate(tag.getAttribute("eval"))) {

						Phase p = parseScript(xml, phase, tag.getChildren(),
								allowPhaseRun, allowErrorLog, miscKeys,
								miscValues);

						if (p != null) {

							return p;
						}
					}
				}

				break;

			case "if-not":

				if (tag.hasAttribute("eval")) {

					if (!evaluate(tag.getAttribute("eval"))) {

						Phase p = parseScript(xml, phase, tag.getChildren(),
								allowPhaseRun, allowErrorLog, miscKeys,
								miscValues);

						if (p != null) {

							return p;
						}
					}
				}

				break;

			case "exec-phase":

				if (allowPhaseRun) {

					if (tag.hasAttribute("phase")) {

						Phase p = Phase.getPhaseForString(tag
								.getAttribute("phase"));

						if (p != null) {

							boolean result = NautilusBuilder.runPhase(p, xml);

							if (result) {

								if (phase.getSuccess() != null) {

									XMLTag[] successTags = phase.getSuccess()
											.getTags();

									Phase rPhase = parseScript(xml, phase,
											successTags, false, false,
											miscKeys, miscValues);

									if (rPhase != null) {

										return rPhase;
									}
								}

							} else {

								if (phase.getFail() != null) {

									XMLTag[] failTags = phase.getFail()
											.getTags();

									Phase rPhase = parseScript(xml, phase,
											failTags, false, true, miscKeys,
											miscValues);

									if (rPhase != null) {

										return rPhase;
									}
								}
							}
						}
					}
				}

				break;

			case "goto":

				if (tag.hasAttribute("phase")) {

					Phase p = Phase
							.getPhaseForString(tag.getAttribute("phase"));

					if (p != null) {

						return p;
					}
				}

				break;

			case "log-errors":

				if (allowErrorLog) {

					for (Throwable t : BuildValues.getThrowables()) {

						if (t != null) {

							LoggingUtils.SYSTEM_OUT.logCrash(t);
						}
					}
				}

				break;

			case "exit":

				System.exit(0);

				break;
			}
		}

		return null;
	}

	public static String parseString(String string, Map<String, String> values,
			String[] miscKeys, String[] miscValues) {

		for (String key : values.keySet()) {

			string = string.replace("${" + key + "}", values.get(key));
		}

		for (int i = 0; i < miscKeys.length; i++) {

			if (miscValues.length >= i) {

				string = string
						.replace("${" + miscKeys[i] + "}", miscValues[i]);
			}
		}

		return string;
	}

	public static boolean evaluate(String eval) {

		switch (eval) {

		case "has-compiler":

			return ToolProvider.getSystemJavaCompiler() != null;
		}

		return false;
	}
}
