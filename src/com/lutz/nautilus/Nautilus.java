package com.lutz.nautilus;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.lutz.nautilus.cli.ArrayHelper;
import com.lutz.nautilus.cli.CLIResponse;
import com.lutz.nautilus.cli.NautilusCLI;

public class Nautilus {

	public static final String VERSION = "0.1.0", DEVELOPER = "LutzBlox";

	private static String sourceDir = "src/";

	private static String nautilusLocation = "";

	private static String nautilusDir = ".nautilus/";

	private static String nautilusLocDir = "nautilus/";

	private static String defaultDir = "defaults/";

	private static String buildDir = "builds/";

	private static String buildTempDir = "temp/";

	private static String dependencyDir = "libs/";

	private static String projectDir = "";

	private static final String PROJECT_XML_FILENAME = "project.xml";

	private static final String PROJECT_BUILD_FILENAME = "project.build";

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		if (args.length >= 1) {

			projectDir = args[0];
		}

		if (args.length >= 2) {

			nautilusLocation = args[1];
		}

		System.setProperty("java.home", getNautilusLocation() + "nautilus-jdk/jre");

		NautilusCLI.registerDefaultCommands();

		if (args.length > 2) {

			List<String> commandArgs = new ArrayList<String>();

			for (int i = 2; i < args.length; i++) {

				commandArgs.add(args[i]);
			}

			NautilusCLI.runCommand(ArrayHelper.getArrayAsString(commandArgs
					.toArray(new String[] {})));

		} else {

			CLIResponse.invalidCommand();
		}
	}

	public static String getProjectDirectory() {

		String projDir = new File(projectDir).getAbsolutePath();

		if (!projDir.endsWith("/")) {

			projDir += "/";
		}

		return projDir;
	}

	public static String getCurrentJavaVersion() {

		String version = System.getProperty("java.version");

		if (version.contains(".")) {

			String[] versionParts = version.split("\\.", 2);

			if (versionParts[1].contains(".")) {

				versionParts[1] = versionParts[1].substring(0,
						versionParts[1].indexOf("."));
			}

			version = versionParts[0] + "." + versionParts[1];
		}

		return version;
	}

	public static String getSourceDirectory() {

		sourceDir = sourceDir.replace("\\", "/");

		if (!sourceDir.endsWith("/")) {

			sourceDir += "/";
		}

		if (sourceDir.startsWith("/")) {

			sourceDir = sourceDir.substring(1);
		}

		return sourceDir;
	}

	public static String getBuildDirectory() {

		buildDir = buildDir.replace("\\", "/");

		if (!buildDir.endsWith("/")) {

			buildDir += "/";
		}

		if (buildDir.startsWith("/")) {

			buildDir = buildDir.substring(1);
		}

		return buildDir;
	}

	public static String getTempBuildDirectory() {

		buildTempDir = buildTempDir.replace("\\", "/");

		if (!buildTempDir.endsWith("/")) {

			buildTempDir += "/";
		}

		if (buildTempDir.startsWith("/")) {

			buildTempDir = buildTempDir.substring(1);
		}

		return buildTempDir;
	}

	public static String getNautilusLocation() {

		nautilusLocation = nautilusLocation.replace("\\", "/");

		if (!nautilusLocation.endsWith("/")) {

			nautilusLocation += "/";
		}

		if (nautilusLocation.startsWith("/")) {

			nautilusLocation = nautilusLocation.substring(1);
		}

		return nautilusLocation;
	}

	public static String getNautilusDirectory() {

		nautilusDir = nautilusDir.replace("\\", "/");

		if (!nautilusDir.endsWith("/")) {

			nautilusDir += "/";
		}

		if (nautilusDir.startsWith("/")) {

			nautilusDir = nautilusDir.substring(1);
		}

		return nautilusDir;
	}

	public static String getNautilusLocationDirectory() {

		nautilusLocDir = nautilusLocDir.replace("\\", "/");

		if (!nautilusLocDir.endsWith("/")) {

			nautilusLocDir += "/";
		}

		if (nautilusLocDir.startsWith("/")) {

			nautilusLocDir = nautilusLocDir.substring(1);
		}

		return nautilusLocDir;
	}

	public static String getNautilusDefaultsDirectory() {

		defaultDir = defaultDir.replace("\\", "/");

		if (!defaultDir.endsWith("/")) {

			defaultDir += "/";
		}

		if (defaultDir.startsWith("/")) {

			defaultDir = defaultDir.substring(1);
		}

		return defaultDir;
	}

	public static String getDependencyDirectory() {

		dependencyDir = dependencyDir.replace("\\", "/");

		if (!dependencyDir.endsWith("/")) {

			dependencyDir += "/";
		}

		if (dependencyDir.startsWith("/")) {

			dependencyDir = dependencyDir.substring(1);
		}

		return dependencyDir;
	}

	public static String getProjectXMLFilename() {

		return PROJECT_XML_FILENAME;
	}

	public static String getProjectBuildFilename() {

		return PROJECT_BUILD_FILENAME;
	}
}
