package main.java.com.lutz.nautilus;

import java.io.File;

import main.java.com.lutz.nautilus.builders.NautilusBuilder;
import main.java.com.lutz.nautilus.xml.ProjectXML;
import main.java.com.lutz.nautilus.xml.XMLUtils;

public class Nautilus {

	private static String sourceDir = "src/";

	private static String nautilusDir = ".nautilus/";

	private static String nautilusLocDir = "nautilus/";

	private static String buildDir = "builds/";

	private static String buildTempDir = "temp/";

	private static String dependencyDir = "libs/";

	private static String projectDir = "";

	private static final String PROJECT_XML_FILENAME = "project.xml";

	public static void main(String[] args) {

		if (args.length >= 1) {

			projectDir = args[0];
		}

		System.setProperty("java.home", getProjectDirectory()
				+ getNautilusDirectory() + getNautilusLocationDirectory()
				+ "java/nautilus-jdk-1.7/jre");

		ProjectXML xml = XMLUtils.updateFileReferences();

		NautilusBuilder.runDefaultBuildSequence(xml);
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
}
