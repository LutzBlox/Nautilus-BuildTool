package main.java.com.lutz.nautilus.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.java.com.lutz.nautilus.Nautilus;
import main.java.com.lutz.nautilus.xml.ProjectXML;

public final class FileUtils {

	public static String getFileExtension(File file) {

		String filename = file.getName();

		if (filename.contains(".") && !filename.endsWith(".")) {

			return filename.substring(filename.lastIndexOf(".") + 1);

		} else {

			return "";
		}
	}

	public static String getRelativePath(String relativeDir, File file) {

		String rootDirectory = relativeDir.replace("\\", "/");

		if (!rootDirectory.endsWith("/")) {

			rootDirectory += "/";
		}

		String filename = file.getAbsolutePath().replace("\\", "/");

		if (filename.contains(rootDirectory)) {

			return filename.substring(filename.indexOf(rootDirectory)
					+ rootDirectory.length());

		} else {

			return filename;
		}
	}

	public static String removeExtension(File file) {

		String filename = file.getName();

		if (filename.contains(".")) {

			return filename.substring(0, filename.lastIndexOf("."));

		} else {

			return filename;
		}
	}

	public static String removeExtensionKeepPath(File file) {

		String filename = file.getName();

		String dir = file.getAbsolutePath().replace("\\", "/");

		if (dir.contains("/")) {

			dir = dir.substring(0, dir.lastIndexOf("/")) + "/";

		} else {

			dir = "";
		}

		if (filename.contains(".")) {

			filename = filename.substring(0, filename.lastIndexOf("."));

		}

		return dir + filename;
	}

	public static void removeDirectory(File file) {

		if (file.isDirectory()) {

			for (File f : file.listFiles()) {

				removeDirectory(f);
			}
		}

		file.delete();
	}

	public static File[] getMovedSourceFilesForJar(ProjectXML xml) {

		List<File> files = new ArrayList<File>();

		readFilesFromProjectXML(
				new File(Nautilus.getProjectDirectory()+Nautilus.getNautilusDirectory()
						+ Nautilus.getTempBuildDirectory()
						+ Nautilus.getSourceDirectory()), files);

		for (int i = 0; i < files.size(); i++) {

			File f = files.get(i);

			if (getFileExtension(f).equalsIgnoreCase("java")) {

				files.remove(i);
			}
		}

		return files.toArray(new File[] {});
	}

	public static File[] getFilesFromProjectXML(ProjectXML xml) {

		List<File> files = new ArrayList<File>();
		
		readFilesFromProjectXML(new File(Nautilus.getProjectDirectory()+Nautilus.getSourceDirectory()), files);

		return files.toArray(new File[] {});
	}

	private static void readFilesFromProjectXML(File currentDir, List<File> list) {

		for (File f : currentDir.listFiles()) {

			if (f.isDirectory()) {

				readFilesFromProjectXML(f, list);

			} else {

				list.add(new File(currentDir, f.getName()));
			}
		}
	}

	public static String[] getClassPathsWithPackage(ProjectXML xml) {

		File[] files = getMovedSourceFilesForJar(xml);

		List<String> classes = new ArrayList<String>();

		for (File f : files) {

			if (getFileExtension(f).equalsIgnoreCase("class")) {

				String fileNoExtension = removeExtensionKeepPath(f);

				String relativePath = getRelativePath(
						Nautilus.getProjectDirectory()
								+ Nautilus.getNautilusDirectory()
								+ Nautilus.getTempBuildDirectory()
								+ Nautilus.getSourceDirectory(), new File(
								fileNoExtension));

				String classPackage = relativePath.replace("\\", "/").replace(
						"/", ".");

				classes.add(classPackage);
			}
		}

		return classes.toArray(new String[] {});
	}

	public static File changeSourceDirectory(File f, String oldSource,
			String newSource) {

		String oldSourceFull = new File(oldSource).getAbsolutePath();

		String newSourceFull = new File(newSource).getAbsolutePath();

		String filename = f.getAbsolutePath();

		if (filename.contains(oldSourceFull)) {

			filename = filename.replace(oldSourceFull, newSourceFull);
		}

		return new File(filename);
	}

	public static String getStringURL(String string) {

		try {

			String url = string.replace("\\", "/").replace(" ", "%20");

			if (url.startsWith("/")) {

				if (url.length() > 1) {

					url = url.substring(1);

				} else {

					url = "";
				}
			}

			return url;

		} catch (Exception e) {

			return null;
		}
	}
}
