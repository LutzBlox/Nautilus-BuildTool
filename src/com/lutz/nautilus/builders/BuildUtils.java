package com.lutz.nautilus.builders;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lutz.nautilus.Nautilus;
import com.lutz.nautilus.io.FileUtils;
import com.lutz.nautilus.loggers.LoggingUtils;


public class BuildUtils {

	public static String getMainClass(File dir, String[] classes) {

		try {

			URLClassLoader loader = new URLClassLoader(new URL[] { dir.toURI()
					.toURL() });

			for (String c : classes) {

				Class<?> cl = loader.loadClass(c);
 
				try {

					Method main = cl.getMethod("main", String[].class);

					if (Modifier.isStatic(main.getModifiers())) {

						if (Modifier.isPublic(main.getModifiers())) {

							if (main.getReturnType() == Void.TYPE) {

								return c;
							}
						}
					}

				} catch (Exception e) {
				}
			}

			loader.close();

		} catch (Exception e) {

			LoggingUtils.SYSTEM_OUT
					.warn("An error occurred while searching for the main class!");

			LoggingUtils.SYSTEM_OUT.logException(e);
		}

		return null;
	}

	public static Map<String, List<String>> formatFileListForJar(File[] files) {

		Map<String, List<String>> jarMap = new HashMap<String, List<String>>();

		for (File f : files) {

			String filename = FileUtils.getRelativePath(
					Nautilus.getProjectDirectory()
							+ Nautilus.getNautilusDirectory()
							+ Nautilus.getTempBuildDirectory()
							+ Nautilus.getSourceDirectory(), f).replace("\\",
					"/");

			String dir = "", name;

			if (filename.contains("/")) {

				dir = filename.substring(0, filename.lastIndexOf("/")) + "/";

				name = filename.substring(filename.lastIndexOf("/") + 1);

			} else {

				name = filename;
			}

			if (!jarMap.containsKey(dir)) {

				jarMap.put(dir, new ArrayList<String>());
			}

			jarMap.get(dir).add(name);
		}

		return jarMap;
	}

	public static String addJarDependency(String path) {

		try {

			File jar = new File(path);

			if (!jar.exists()) {

				jar = new File(Nautilus.getProjectDirectory() + path);
			}

			if (jar.exists()) {

				File depDir = new File(Nautilus.getProjectDirectory()
						+ Nautilus.getNautilusDirectory()
						+ Nautilus.getBuildDirectory()
						+ Nautilus.getDependencyDirectory());

				depDir.mkdirs();

				File target = new File(depDir, jar.getName());

				Files.copy(jar.toPath(), target.toPath(),
						StandardCopyOption.REPLACE_EXISTING);

				return FileUtils.getStringURL(Nautilus.getDependencyDirectory()
						+ jar.getName());
			}

		} catch (Exception e) {

			LoggingUtils.SYSTEM_OUT
					.warn("An error occurred while copying dependencies!");

			LoggingUtils.SYSTEM_OUT.logException(e);
		}

		return null;
	}
}
