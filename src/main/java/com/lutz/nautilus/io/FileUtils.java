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

	public static String getRelativePath(String projectDir, File file) {

		String rootDirectory = projectDir.replace("\\", "/");

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

	public static void removeDirectory(File file) {

		if (file.isDirectory()) {

			for (File f : file.listFiles()) {

				removeDirectory(f);
			}
		}

		file.delete();
	}

	public static File[] getFilesFromProjectXML(ProjectXML xml) {

		List<File> files = new ArrayList<File>();

		readFilesFromProjectXML(new File(Nautilus.getSourceDirectory()), files);

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

		File[] files = getFilesFromProjectXML(xml);

		List<String> classes = new ArrayList<String>();

		for (File f : files) {

			if (getFileExtension(f).equalsIgnoreCase("java")) {

				String fileNoExtension = removeExtension(f);

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
}
