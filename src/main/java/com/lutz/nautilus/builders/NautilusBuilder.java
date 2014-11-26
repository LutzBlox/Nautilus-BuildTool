package main.java.com.lutz.nautilus.builders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import main.java.com.lutz.nautilus.Nautilus;
import main.java.com.lutz.nautilus.io.FileUtils;
import main.java.com.lutz.nautilus.loggers.LoggingUtils;
import main.java.com.lutz.nautilus.xml.ProjectXML;

public class NautilusBuilder {

	public static void runDefaultBuildSequence(ProjectXML xml) {

		runPhase(BuildPhase.MOVE_SOURCES, xml);
		runPhase(BuildPhase.COMPILE, xml);
		runPhase(BuildPhase.PACKAGE, xml);
		runPhase(BuildPhase.GEN_SOURCE, xml);
		runPhase(BuildPhase.CLEANUP, xml);
	}

	public static void runPhase(BuildPhase phase, ProjectXML xml,
			String... args) {

		switch (phase) {

		case MOVE_SOURCES:

			moveSources(xml, args);

			break;

		case COMPILE:

			compile(xml, args);

			break;

		case PACKAGE:

			packageJar(xml, args);

			break;

		case GEN_SOURCE:

			generateSource(xml, args);

			break;

		case CLEANUP:

			cleanup(xml, args);

			break;
		}
	}

	public static void moveSources(ProjectXML xml, String... args) {

		LoggingUtils.DEFAULT_LOGGER.info("Creating temporary build folder...");

		File buildTempFolder = new File(Nautilus.getNautilusDirectory()
				+ Nautilus.getTempBuildDirectory());
		buildTempFolder.mkdirs();

		LoggingUtils.DEFAULT_LOGGER.info("Moving sources...");

		try {

			copySources(new File(Nautilus.getSourceDirectory()),
					buildTempFolder);

		} catch (Exception e) {

			LoggingUtils.DEFAULT_LOGGER
					.warn("An error occured while moving sources!");

			LoggingUtils.DEFAULT_LOGGER.logException(e);
		}
	}

	private static void copySources(File currentDir, File buildFolder)
			throws IOException {

		if (currentDir.isDirectory()) {

			new File(buildFolder, FileUtils.getRelativePath(
					Nautilus.getProjectDirectory(), currentDir)).mkdirs();

			for (File f : currentDir.listFiles()) {

				if (f.isDirectory()) {

					copySources(f, buildFolder);

				} else {

					Files.copy(
							f.toPath(),
							new File(buildFolder, FileUtils.getRelativePath(
									Nautilus.getProjectDirectory(), f))
									.toPath(),
							StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	public static void compile(ProjectXML xml, String... args) {

		File[] files = getFilesToCompile(FileUtils.getFilesFromProjectXML(xml));

		for (int i = 0; i < files.length; i++) {

			files[i] = FileUtils.changeSourceDirectory(
					files[i],
					Nautilus.getSourceDirectory(),
					Nautilus.getNautilusDirectory()
							+ Nautilus.getTempBuildDirectory()
							+ Nautilus.getSourceDirectory());
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		if (compiler != null) {

			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

			StandardJavaFileManager fileManager = compiler
					.getStandardFileManager(diagnostics, null, null);

			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjects(files);

			CompilationTask task = compiler.getTask(null, fileManager,
					diagnostics, getClasspathArray(files), null,
					compilationUnits);

			boolean result = task.call();

			if (result) {

				LoggingUtils.DEFAULT_LOGGER.info("Compilation successful!");

			} else {

				LoggingUtils.DEFAULT_LOGGER
						.warn("Uh, oh!  Compilation failed!");
			}

		} else {

			LoggingUtils.DEFAULT_LOGGER
					.warn("The platform's Java compiler could not be found!  Make sure a JDK is installed.");
		}
	}

	private static List<String> getClasspathArray(File[] files) {

		List<String> args = new ArrayList<String>();

		args.add("-cp");

		String arg = ".;C:\\Users\\Krealutz\\Desktop\\Java\\Build Tool\\Nautilus-BuildTool\\libs\\Nautilus EasyXML.jar";

		args.add(arg);

		return args;
	}

	private static File[] getFilesToCompile(File[] files) {

		List<File> file = new ArrayList<File>();

		for (File f : files) {

			if (FileUtils.getFileExtension(f).equalsIgnoreCase("java")) {

				file.add(f);
			}
		}

		return file.toArray(new File[] {});
	}

	public static void packageJar(ProjectXML xml, String... args) {

		try {

			new File(Nautilus.getNautilusDirectory()
					+ Nautilus.getBuildDirectory()).mkdirs();

			FileOutputStream fileOutput = new FileOutputStream(new File(
					Nautilus.getNautilusDirectory()
							+ Nautilus.getBuildDirectory()
							+ xml.getProjectName() + "-"
							+ xml.getProjectVersion() + ".jar"));

			JarOutputStream jarOutput;

			if (xml.getProjectModel().getMainClass() != null) {

				Manifest manifest = new Manifest();

				manifest.getMainAttributes().put(
						Attributes.Name.MANIFEST_VERSION, "1.0");

				manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS,
						xml.getProjectModel().getMainClass());

				jarOutput = new JarOutputStream(fileOutput, manifest);

			} else {

				LoggingUtils.DEFAULT_LOGGER
						.info("ERROR: The project being built does not have a main class specified (you must do this manually in the project.xml file).  If this is acceptable, you may ignore this message.");

				jarOutput = new JarOutputStream(fileOutput);
			}

			jarOutput.close();

			fileOutput.close();

		} catch (Exception e) {

			LoggingUtils.DEFAULT_LOGGER
					.warn("An error occurred while packaging the jar file!");

			LoggingUtils.DEFAULT_LOGGER.logException(e);
		}
	}

	public static void generateSource(ProjectXML xml, String... args) {

	}

	public static void cleanup(ProjectXML xml, String... args) {

		LoggingUtils.DEFAULT_LOGGER.info("Cleaning up build environment...");

		File buildTempFolder = new File(Nautilus.getNautilusDirectory()
				+ Nautilus.getTempBuildDirectory());

		if (buildTempFolder.exists()) {

			FileUtils.removeDirectory(buildTempFolder);
		}
	}
}
