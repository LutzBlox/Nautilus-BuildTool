package com.lutz.nautilus.builders.script;

import java.util.ArrayList;
import java.util.List;

import com.lutz.easyxml.XMLTag;
import com.lutz.nautilus.builders.Phase;
import com.lutz.nautilus.loggers.LoggingUtils;
import com.lutz.nautilus.xml.ProjectXML;

public class BuildScript {

	public static class BuildPhase {

		public static class Success {

			private XMLTag[] tags;

			public Success(XMLTag[] tags) {

				this.tags = tags;
			}

			public XMLTag[] getTags() {

				return tags;
			}
		}

		public static class Fail {

			private XMLTag[] tags;

			public Fail(XMLTag[] tags) {

				this.tags = tags;
			}

			public XMLTag[] getTags() {

				return tags;
			}
		}

		private Phase phase;
		private Success success;
		private Fail fail;
		private XMLTag[] miscTags;

		public BuildPhase(Phase phase, XMLTag[] miscTags) {

			this(phase, null, null, miscTags);
		}

		public BuildPhase(Phase phase, Success success, XMLTag[] miscTags) {

			this(phase, success, null, miscTags);
		}

		public BuildPhase(Phase phase, Fail fail, XMLTag[] miscTags) {

			this(phase, null, fail, miscTags);
		}

		public BuildPhase(Phase phase, Success success, Fail fail,
				XMLTag[] miscTags) {

			this.phase = phase;
			this.success = success;
			this.fail = fail;
			this.miscTags = miscTags;
		}

		public Phase getPhase() {

			return phase;
		}

		public Success getSuccess() {

			return success;
		}

		public Fail getFail() {

			return fail;
		}

		public XMLTag[] getMiscTags() {

			return miscTags;
		}

		public static BuildPhase parsePhase(XMLTag tag) {

			if (tag.hasAttribute("phase")) {

				String ph = tag.getAttribute("phase");

				Phase phase = Phase.getPhaseForString(ph);

				Success success = null;
				Fail fail = null;

				if (phase != null) {

					List<XMLTag> miscTags = new ArrayList<XMLTag>();

					for (XMLTag child : tag.getChildren()) {

						if (child.getName().equalsIgnoreCase("success")) {

							success = new Success(child.getChildren());

						} else if (child.getName().equalsIgnoreCase("fail")) {

							fail = new Fail(child.getChildren());

						} else {

							miscTags.add(child);
						}
					}

					return new BuildPhase(phase, success, fail,
							miscTags.toArray(new XMLTag[] {}));
				}
			}

			LoggingUtils.SYSTEM_OUT
					.log("One of the build phases was unable to be recognized!");

			return null;
		}
	}

	public static void run(ProjectXML xml, XMLTag root, String[] miscKeys,
			String[] miscValues) {

		if (root.getName().equals("build")) {

			BuildPhase moveSource = null;
			BuildPhase compile = null;
			BuildPhase packageJar = null;
			BuildPhase generateSrc = null;
			BuildPhase cleanup = null;
			BuildPhase finishS = null;
			BuildPhase finishF = null;

			for (XMLTag tag : root.getChildren()) {

				if (tag.getName().equals("build-phase")) {

					BuildPhase phase = BuildPhase.parsePhase(tag);

					if (phase != null) {

						switch (phase.getPhase()) {

						case CLEANUP:

							cleanup = phase;

							break;

						case COMPILE:

							compile = phase;

							break;

						case FINISH_FAIL:

							finishF = phase;

							break;

						case FINISH_SUCCESS:

							finishS = phase;

							break;

						case GEN_SOURCE:

							generateSrc = phase;

							break;

						case MOVE_SOURCES:

							moveSource = phase;

							break;

						case PACKAGE:

							packageJar = phase;

							break;
						}
					}
				}
			}

			runPhase(Phase.MOVE_SOURCES, xml, moveSource, compile, packageJar,
					generateSrc, cleanup, finishS, finishF, miscKeys,
					miscValues);

		} else {

			LoggingUtils.SYSTEM_OUT
					.warn("The build script's root tag must be 'build'!");
		}
	}

	private static void runPhase(Phase phase, ProjectXML xml,
			BuildPhase moveSrc, BuildPhase compile, BuildPhase packageJar,
			BuildPhase genSrc, BuildPhase cleanup, BuildPhase finishS,
			BuildPhase finishF, String[] miscKeys, String[] miscValues) {

		BuildPhase buildPhase = getPhase(phase, moveSrc, compile, packageJar,
				genSrc, cleanup, finishS, finishF);

		if (buildPhase != null) {

			Phase p = ScriptUtils
					.parseScript(xml, buildPhase, buildPhase.getMiscTags(),
							true, false, miscKeys, miscValues);
			
			if (p != null) {
				
				runPhase(p, xml, moveSrc, compile, packageJar, genSrc, cleanup,
						finishS, finishF, miscKeys, miscValues);

			} else {

				Phase next = phase.getNextPhase();

				if (next != null) {

					runPhase(next, xml, moveSrc, compile, packageJar, genSrc,
							cleanup, finishS, finishF, miscKeys, miscValues);
				}
			}
		}
	}

	private static BuildPhase getPhase(Phase phase, BuildPhase moveSrc,
			BuildPhase compile, BuildPhase packageJar, BuildPhase genSrc,
			BuildPhase cleanup, BuildPhase finishS, BuildPhase finishF) {

		switch (phase) {

		case CLEANUP:

			return cleanup;

		case COMPILE:

			return compile;

		case FINISH_FAIL:

			return finishF;

		case FINISH_SUCCESS:

			return finishS;

		case GEN_SOURCE:

			return genSrc;

		case MOVE_SOURCES:

			return moveSrc;

		case PACKAGE:

			return packageJar;

		default:

			return null;
		}
	}
}
