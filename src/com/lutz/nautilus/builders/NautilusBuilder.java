package com.lutz.nautilus.builders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.lutz.easyxml.XMLParser;
import com.lutz.easyxml.XMLTag;
import com.lutz.easyxml.XMLTree;
import com.lutz.nautilus.Nautilus;
import com.lutz.nautilus.builders.exceptions.BuildException;
import com.lutz.nautilus.builders.script.BuildScript;
import com.lutz.nautilus.builders.script.BuildValues;
import com.lutz.nautilus.io.FileUtils;
import com.lutz.nautilus.loggers.LoggingUtils;
import com.lutz.nautilus.xml.ProjectXML;
import com.lutz.nautilus.xml.data.DependencyEntry;
import com.lutz.nautilus.xml.data.DependencyPackageType;

public class NautilusBuilder {

	public static void runBuildSequence(ProjectXML xml) {

		File buildFile = new File(Nautilus.getProjectDirectory()
				+ Nautilus.getProjectBuildFilename());

		if (!buildFile.exists()) {

			buildFile = new File(Nautilus.getNautilusLocation()
					+ Nautilus.getNautilusDefaultsDirectory()
					+ Nautilus.getProjectBuildFilename());

			if (!buildFile.exists()) {

				LoggingUtils.SYSTEM_OUT
						.fatal("No default build file was located!");

				return;
			}
		}

		XMLTree tree = new XMLParser().parseXML(buildFile);

		XMLTag root = tree.getRoot();

		BuildScript.run(xml, root, new String[] {}, new String[] {});
	}

	public static boolean runPhase(Phase phase, ProjectXML xml) {

		switch (phase) {

		case MOVE_SOURCES:

			return moveSources(xml);

		case COMPILE:

			return compile(xml);

		case PACKAGE:

			return packageJar(xml);

		case GEN_SOURCE:

			return generateSource(xml);

		case CLEANUP:

			return cleanup(xml);

		default:

			return false;
		}
	}

	public static boolean moveSources(ProjectXML xml) {

		File buildTempFolder = new File(Nautilus.getProjectDirectory()
				+ Nautilus.getNautilusDirectory()
				+ Nautilus.getTempBuildDirectory());
		buildTempFolder.mkdirs();

		try {

			copySources(
					new File(Nautilus.getProjectDirectory()
							+ Nautilus.getSourceDirectory()), buildTempFolder);

			return true;

		} catch (Exception e) {

			BuildValues.addThrowable(e);

			return false;
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

	public static boolean compile(ProjectXML xml) {

		File[] files = getFilesToCompile(FileUtils.getFilesFromProjectXML(xml));

		for (int i = 0; i < files.length; i++) {

			files[i] = FileUtils.changeSourceDirectory(
					files[i],
					Nautilus.getProjectDirectory()
							+ Nautilus.getSourceDirectory(),
					Nautilus.getProjectDirectory()
							+ Nautilus.getNautilusDirectory()
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

			CompilationTask task = compiler
					.getTask(null, fileManager, diagnostics,
							getClasspathArray(xml), null, compilationUnits);

			boolean result = task.call();

			if (result) {

				return true;

			} else {

				for (Diagnostic<? extends JavaFileObject> d : diagnostics
						.getDiagnostics()) {

					BuildException ex = new BuildException(
							"Compilation error on line " + d.getLineNumber()
									+ " in " + getClassFromFile(d.getSource()));

					BuildValues.addThrowable(ex);
				}

				return false;
			}

		} else {

			BuildException e = new BuildException(
					"The platform's Java compiler could not be found!  Make sure a JDK is installed.");

			BuildValues.addThrowable(e);

			return false;
		}
	}

	private static String getClassFromFile(JavaFileObject javaFile) {

		File file = new File(javaFile.toUri());

		String fileSub = new File(Nautilus.getNautilusDirectory()
				+ Nautilus.getTempBuildDirectory()
				+ Nautilus.getSourceDirectory()).getAbsolutePath();

		String relPath = FileUtils.getRelativePath(fileSub, file)
				.replace("\\", ".").replace("/", ".");
		relPath = relPath.substring(0, relPath.lastIndexOf('.'));

		return relPath;
	}

	private static List<String> getClasspathArray(ProjectXML xml) {

		List<String> args = new ArrayList<String>();

		args.add("-cp");

		String arg = ".";

		for (DependencyEntry dependency : xml.getProjectModel()
				.getDependencyEntries()) {

			arg += ";" + dependency.getPath();
		}

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

	public static boolean packageJar(ProjectXML xml) {

		try {

			new File(Nautilus.getProjectDirectory()
					+ Nautilus.getNautilusDirectory()
					+ Nautilus.getBuildDirectory()).mkdirs();

			FileOutputStream fileOutput = new FileOutputStream(new File(
					Nautilus.getProjectDirectory()
							+ Nautilus.getNautilusDirectory()
							+ Nautilus.getBuildDirectory()
							+ xml.getDistributionName() + ".jar"));

			JarOutputStream jarOutput;

			Manifest manifest = getManifest(xml);

			if (manifest != null) {

				List<DependencyEntry> dep = new ArrayList<DependencyEntry>();

				for (DependencyEntry d : xml.getProjectModel()
						.getDependencyEntries()) {

					if (d.getPackageType() == DependencyPackageType.INTERNAL) {

						dep.add(d);
					}
				}

				String contents = "";

				for (int i = 0; i < dep.size(); i++) {

					contents +=  new File(dep.get(i).getPath()).getName();

					if (i < dep.size() - 1) {

						contents += ", ";
					}
				}

				manifest.getMainAttributes().put(
						new Attributes.Name("Internal-Dependencies"), contents);
			}

			if (manifest != null) {

				jarOutput = new JarOutputStream(fileOutput, manifest);

			} else {

				jarOutput = new JarOutputStream(fileOutput);
			}

			Map<String, List<String>> files = BuildUtils
					.formatFileListForJar(FileUtils
							.getMovedSourceFilesForJar(xml));

			List<JarEntry> jarEntries = new ArrayList<JarEntry>();

			for (String dir : files.keySet()) {

				if (!dir.contentEquals("")) {

					JarEntry entry = new JarEntry(dir);

					jarOutput.putNextEntry(entry);

					jarEntries.add(entry);
				}

				for (String name : files.get(dir)) {

					JarEntry entry = new JarEntry(dir + name);

					jarOutput.putNextEntry(entry);

					jarEntries.add(entry);

					try {

						jarOutput.write(Files.readAllBytes(new File(Nautilus
								.getProjectDirectory()
								+ Nautilus.getNautilusDirectory()
								+ Nautilus.getTempBuildDirectory()
								+ Nautilus.getSourceDirectory() + dir + name)
								.toPath()));

					} catch (Exception e) {

						jarOutput.close();

						BuildValues.addThrowable(e);

						return false;
					}
				}
			}

			for (DependencyEntry d : xml.getProjectModel()
					.getDependencyEntries()) {

				if (d.getPackageType() == DependencyPackageType.INTERNAL) {

					File jar = new File(d.getPath());

					JarFile jarFile = new JarFile(jar);

					byte[] buffer = new byte[1024];
					int bytesRead;

					for (Enumeration<JarEntry> entries = jarFile.entries(); entries
							.hasMoreElements();) {

						JarEntry next = entries.nextElement();

						if (!nameExists(jarEntries, next)) {

							jarOutput.putNextEntry(next);

							InputStream input = jarFile.getInputStream(next);

							while ((bytesRead = input.read(buffer)) != -1) {

								jarOutput.write(buffer, 0, bytesRead);
							}

							input.close();
						}
					}

					jarFile.close();
				}
			}

			jarOutput.close();

			fileOutput.close();

			return true;

		} catch (Exception e) {

			BuildValues.addThrowable(e);

			return false;
		}
	}

	private static boolean nameExists(List<JarEntry> jarEntries, JarEntry next) {

		if (next.getName().equalsIgnoreCase("meta-inf/manifest.mf")) {

			return true;
		}

		for (JarEntry entry : jarEntries) {

			if (entry.getName().equalsIgnoreCase(next.getName())) {

				return true;
			}
		}

		return false;
	}

	private static Manifest getManifest(ProjectXML xml) {

		Manifest manifest = new Manifest();

		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION,
				"1.0");

		manifest.getMainAttributes().put(new Attributes.Name("Project-Name"),
				xml.getProjectName());

		manifest.getMainAttributes()
				.put(new Attributes.Name("Project-Version"),
						xml.getProjectVersion());

		manifest.getMainAttributes().put(
				new Attributes.Name("Project-Developer"),
				xml.getProjectDeveloper());

		manifest.getMainAttributes().put(
				new Attributes.Name("Project-Description"),
				xml.getProjectDescription());

		manifest.getMainAttributes().put(new Attributes.Name("Created-By"),
				"Nautilus");

		List<String> classpath = new ArrayList<String>();

		for (DependencyEntry dependency : xml.getProjectModel()
				.getDependencyEntries()) {

			if (dependency.getPackageType() == DependencyPackageType.EXTERNAL) {

				String classpathEntry = BuildUtils.addJarDependency(dependency
						.getPath());

				if (classpathEntry != null) {

					classpath.add(classpathEntry);
				}
			}
		}

		String classpathStr = ".";

		for (String c : classpath) {

			classpathStr += " " + c;
		}

		manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH,
				classpathStr);

		String mainClass = BuildUtils.getMainClass(
				new File(Nautilus.getProjectDirectory()
						+ Nautilus.getNautilusDirectory()
						+ Nautilus.getTempBuildDirectory()
						+ Nautilus.getSourceDirectory()),
				FileUtils.getClassPathsWithPackage(xml));

		if (mainClass != null) {

			manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS,
					mainClass);

		} else {

			LoggingUtils.SYSTEM_OUT
					.warn("The automated main class finder was unable to locate a main class!  If this is expected, you may ignore this message.");
		}

		return manifest;
	}

	public static boolean generateSource(ProjectXML xml) {

		try {

			new File(Nautilus.getProjectDirectory()
					+ Nautilus.getNautilusDirectory()
					+ Nautilus.getBuildDirectory()).mkdirs();

			FileOutputStream fileOutput = new FileOutputStream(new File(
					Nautilus.getProjectDirectory()
							+ Nautilus.getNautilusDirectory()
							+ Nautilus.getBuildDirectory()
							+ xml.getDistributionSourceName() + ".jar"));

			JarOutputStream jarOutput = new JarOutputStream(fileOutput);

			Map<String, List<String>> files = BuildUtils
					.formatFileListForJar(FileUtils
							.getSourceFilesForGenSrc(xml));

			for (String dir : files.keySet()) {

				if (!dir.contentEquals("")) {

					jarOutput.putNextEntry(new JarEntry(dir));
				}

				for (String name : files.get(dir)) {

					jarOutput.putNextEntry(new JarEntry(dir + name));

					try {

						jarOutput.write(Files.readAllBytes(new File(Nautilus
								.getProjectDirectory()
								+ Nautilus.getNautilusDirectory()
								+ Nautilus.getTempBuildDirectory()
								+ Nautilus.getSourceDirectory() + dir + name)
								.toPath()));

					} catch (Exception e) {

						jarOutput.close();

						BuildValues.addThrowable(e);

						return false;
					}
				}
			}

			jarOutput.close();

			fileOutput.close();

			return true;

		} catch (Exception e) {

			BuildValues.addThrowable(e);

			return false;
		}
	}

	public static boolean cleanup(ProjectXML xml) {

		File buildTempFolder = new File(Nautilus.getProjectDirectory()
				+ Nautilus.getNautilusDirectory()
				+ Nautilus.getTempBuildDirectory());

		if (buildTempFolder.exists()) {

			FileUtils.removeDirectory(buildTempFolder);
		}

		return true;
	}
}
