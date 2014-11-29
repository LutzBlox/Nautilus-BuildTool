package main.java.com.lutz.nautilus.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.com.lutz.nautilus.Nautilus;
import main.java.com.lutz.nautilus.io.FileUtils;
import main.java.com.lutz.nautilus.loggers.LoggingUtils;
import main.java.com.lutz.nautilus.settings.ProjectSettings;
import main.java.com.lutz.nautilus.settings.SettingsEntry;
import main.java.com.lutz.nautilus.xml.data.ClassEntry;
import main.java.com.lutz.nautilus.xml.data.DependencyEntry;
import main.java.com.lutz.nautilus.xml.data.FileEntry;
import main.java.com.lutz.nautilus.xml.data.ModelEntry;
import main.java.com.lutz.nautilus.xml.data.PackageEntry;
import main.java.com.lutz.nautilus.xml.data.ProjectModel;

import com.lutz.easyxml.XMLParser;
import com.lutz.easyxml.XMLTag;
import com.lutz.easyxml.XMLTree;
import com.lutz.easyxml.XMLWriter;

public class XMLUtils {

	public static ProjectXML parseProjectXML() {

		ProjectModel model = new ProjectModel();

		ProjectSettings settings = new ProjectSettings();

		String name, id, version;

		XMLParser parser = new XMLParser();

		if (new File(Nautilus.getProjectXMLFilename()).exists()) {

			XMLTree tree = parser.parseXML(new File(Nautilus
					.getProjectDirectory() + Nautilus.getProjectXMLFilename()));

			XMLTag root = tree.getRoot();

			if (root.hasChild("id")) {

				id = root.getChild("id").getValue();

			} else {

				id = "null";
			}

			if (root.hasChild("name")) {

				name = root.getChild("name").getValue();

			} else {

				name = id;
			}

			if (root.hasChild("version")) {

				version = root.getChild("version").getValue();

			} else {

				version = "-1";
			}

			if (root.hasChild("settings")) {

				XMLTag[] settingsList = root.getChild("settings")
						.getChildrenForName("setting");

				for (XMLTag setting : settingsList) {

					if (setting.hasAttribute("name")
							&& setting.hasAttribute("value")) {

						settings.addSetting(new SettingsEntry(setting
								.getAttribute("name"), setting
								.getAttribute("value")));
					}
				}
			}

			if (root.hasChild("dependencies")) {

				XMLTag[] dependencies = root.getChild("dependencies")
						.getChildrenForName("dependency");

				for (XMLTag dependency : dependencies) {

					if (dependency.getValue() != null) {

						model.addDependencyEntry(new DependencyEntry(dependency
								.getValue()));
					}
				}
			}

			if (root.hasChild("class-registry")) {

				XMLTag[] classList = root.getChild("class-registry")
						.getChildrenForName("class");

				for (XMLTag classEntry : classList) {

					String entry = classEntry.getValue();

					if (entry != null) {

						String[] nameParts = entry.split(".");

						String className;

						if (nameParts.length > 1) {

							className = nameParts[nameParts.length - 1];

						} else if (nameParts.length == 1) {

							className = nameParts[0];

						} else {

							className = null;
						}

						String[] packageParts;

						if (nameParts.length > 1) {

							packageParts = Arrays.copyOf(nameParts,
									nameParts.length - 1);

						} else {

							packageParts = nameParts;
						}

						PackageEntry lastEntry = null;

						if (packageParts.length > 0) {

							if (!model.hasPackageEntry(packageParts[0])) {

								model.addEntry(new PackageEntry(packageParts[0]));
							}

							lastEntry = model.getPackageEntry(packageParts[0]);

							for (int i = 1; i < packageParts.length; i++) {

								String part = packageParts[i];

								if (!lastEntry.hasPackageEntry(part)) {

									lastEntry.addEntry(new PackageEntry(part));
								}

								lastEntry = lastEntry.getPackageEntry(part);
							}
						}

						if (lastEntry != null) {

							lastEntry.addEntry(new ClassEntry(className));

						} else {

							model.addEntry(new ClassEntry(className));
						}
					}
				}
			}

			if (root.hasChild("file-registry")) {

				XMLTag[] files = root.getChild("file-registry")
						.getChildrenForName("file");

				for (XMLTag file : files) {

					String fileName = file.getValue();

					if (fileName != null) {

						model.addFileEntry(new FileEntry(fileName));
					}
				}
			}

			return new ProjectXML(name, id, version, model, settings);
		}

		return null;
	}

	public static ProjectXML generateProjectXML(String name, String id,
			String version) {

		ProjectModel model = new ProjectModel();

		LoggingUtils.DEFAULT_LOGGER.info("Building project settings...");

		ProjectSettings settings = new ProjectSettings();

		settings.fillWithMissing();

		LoggingUtils.DEFAULT_LOGGER.info("Building project model...");

		if (new File(Nautilus.getProjectDirectory()
				+ Nautilus.getSourceDirectory()).exists()) {

			sortClassModel(
					new File(Nautilus.getProjectDirectory()
							+ Nautilus.getSourceDirectory()),
					new File(Nautilus.getSourceDirectory()).listFiles(), null,
					model);
		}

		LoggingUtils.DEFAULT_LOGGER
				.info("Building finalized ProjectXML object...");

		return new ProjectXML(name, id, version, model, settings);
	}

	private static void sortClassModel(File currentDir, File[] fileList,
			PackageEntry entry, ProjectModel model) {

		LoggingUtils.DEFAULT_LOGGER.info("Sorting through "
				+ FileUtils.getRelativePath(Nautilus.getProjectDirectory(),
						currentDir).replace("/", ".") + "...");

		List<PackageEntry> packageEntries = new ArrayList<PackageEntry>();

		List<File> packageDirs = new ArrayList<File>();

		for (File file : fileList) {

			if (file.isDirectory()) {

				if (entry != null) {

					entry.addEntry(new PackageEntry(file.getName()));

					packageEntries.add(entry.getPackageEntry(file.getName()));

					packageDirs.add(file);

				} else {

					model.addEntry(new PackageEntry(file.getName()));

					packageEntries.add(model.getPackageEntry(file.getName()));

					packageDirs.add(file);
				}

			} else {

				String fileExtension = FileUtils.getFileExtension(file);

				if (fileExtension.equalsIgnoreCase("java")) {

					LoggingUtils.DEFAULT_LOGGER.info("Found source file "
							+ file.getName() + "...");

					if (entry != null) {

						entry.addEntry(new ClassEntry(FileUtils
								.removeExtension(file)));

					} else {

						model.addEntry(new ClassEntry(FileUtils
								.removeExtension(file)));
					}

				} else {

					LoggingUtils.DEFAULT_LOGGER
							.info("Found miscellaneous file " + file.getName()
									+ "...");

					model.addFileEntry(new FileEntry(FileUtils.getRelativePath(
							Nautilus.getProjectDirectory()
									+ Nautilus.getSourceDirectory(), file)));
				}
			}
		}

		for (int i = 0; i < packageEntries.size(); i++) {

			File file = packageDirs.get(i);

			PackageEntry nextEntry = packageEntries.get(i);

			sortClassModel(file, file.listFiles(), nextEntry, model);
		}
	}

	public static ProjectXML generateAndWriteProjectXML(String name, String id,
			String version) {

		ProjectXML xml = generateProjectXML(name, id, version);

		if (xml != null) {

			XMLTag rootTag = new XMLTag("project");

			XMLTag nameTag = new XMLTag("name");
			nameTag.setValue(name);

			XMLTag idTag = new XMLTag("id");
			idTag.setValue(id);

			XMLTag versionTag = new XMLTag("version");
			versionTag.setValue(version);

			XMLTag settingsTag = new XMLTag("settings");

			fillSettingsTag(xml.getProjectSettings().getSettings(), settingsTag);

			XMLTag classRegistry = new XMLTag("class-registry");

			fillClassRegistryTag(xml.getProjectModel().getEntries(), null,
					classRegistry);

			XMLTag fileRegistry = new XMLTag("file-registry");

			fillFileRegistryTag(xml.getProjectModel().getFileEntries(),
					fileRegistry);

			rootTag.addChild(nameTag);
			rootTag.addChild(idTag);
			rootTag.addChild(versionTag);
			rootTag.addChild(settingsTag);
			rootTag.addChild(classRegistry);
			rootTag.addChild(fileRegistry);

			XMLTree tree = new XMLTree(rootTag);

			LoggingUtils.DEFAULT_LOGGER.info("Writing ProjectXML data to "
					+ Nautilus.getProjectXMLFilename() + "...");

			XMLWriter.writeXML(new File(Nautilus.getProjectDirectory()
					+ Nautilus.getProjectXMLFilename()), tree);
		}

		return xml;
	}

	private static void fillSettingsTag(SettingsEntry[] settings,
			XMLTag settingsTag) {

		for (SettingsEntry setting : settings) {

			XMLTag settingTag = new XMLTag("setting");
			settingTag.setAttribute("name", setting.getName());
			settingTag.setAttribute("value", setting.getValue());

			settingsTag.addChild(settingTag);
		}
	}

	private static void fillClassRegistryTag(ModelEntry[] entries,
			String currentPackage, XMLTag classRegistry) {

		for (ModelEntry entry : entries) {

			if (entry instanceof ClassEntry) {

				String name = "";

				if (currentPackage != null) {

					name = currentPackage;

					if (!name.endsWith(".")) {

						name += ".";
					}

					name += entry.getName();

				} else {

					name = entry.getName();
				}

				XMLTag classTag = new XMLTag("class");
				classTag.setValue(name);

				classRegistry.addChild(classTag);

			} else if (entry instanceof PackageEntry) {

				String newPackage = "";

				if (currentPackage != null) {

					newPackage = currentPackage;

					if (!newPackage.endsWith(".")) {

						newPackage += ".";
					}

					newPackage += entry.getName();

				} else {

					newPackage = entry.getName();
				}

				fillClassRegistryTag(((PackageEntry) entry).getEntries(),
						newPackage, classRegistry);
			}
		}
	}

	private static void fillFileRegistryTag(FileEntry[] entries,
			XMLTag fileRegistry) {

		for (FileEntry entry : entries) {

			XMLTag file = new XMLTag("file");
			file.setValue(entry.getPath());

			fileRegistry.addChild(file);
		}
	}

	public static ProjectXML updateFileReferences() {

		LoggingUtils.DEFAULT_LOGGER
				.log("Updating project.xml file references...");

		ProjectXML xml = generateProjectXML("null", "null", "-1");

		if (xml != null) {

			if (new File(Nautilus.getProjectDirectory()
					+ Nautilus.getProjectXMLFilename()).exists()) {

				XMLTree tree = new XMLParser().parseXML(new File(Nautilus
						.getProjectDirectory()
						+ Nautilus.getProjectXMLFilename()));

				if (tree.getRoot().hasChild("class-registry")) {

					XMLTag classRegistry = tree.getRoot().getChild(
							"class-registry");
					
					classRegistry.clearChildren();

					fillClassRegistryTag(xml.getProjectModel().getEntries(),
							null, classRegistry);
				}

				if (tree.getRoot().hasChild("file-registry")) {

					XMLTag fileRegistry = tree.getRoot().getChild(
							"file-registry");
					
					fileRegistry.clearChildren();

					fillFileRegistryTag(xml.getProjectModel().getFileEntries(),
							fileRegistry);
				}

				LoggingUtils.DEFAULT_LOGGER.info("Writing ProjectXML data to "
						+ Nautilus.getProjectXMLFilename() + "...");

				XMLWriter.writeXML(new File(Nautilus.getProjectDirectory()
						+ Nautilus.getProjectXMLFilename()), tree);
			}
		}

		return parseProjectXML();
	}
}
