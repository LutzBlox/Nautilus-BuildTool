package com.lutz.nautilus.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lutz.easyxml.XMLParser;
import com.lutz.easyxml.XMLTag;
import com.lutz.easyxml.XMLTree;
import com.lutz.easyxml.XMLWriter;
import com.lutz.nautilus.Nautilus;
import com.lutz.nautilus.builders.script.ScriptUtils;
import com.lutz.nautilus.io.FileUtils;
import com.lutz.nautilus.loggers.LoggingUtils;
import com.lutz.nautilus.settings.ProjectSettings;
import com.lutz.nautilus.settings.SettingsEntry;
import com.lutz.nautilus.xml.data.ClassEntry;
import com.lutz.nautilus.xml.data.DependencyEntry;
import com.lutz.nautilus.xml.data.DependencyPackageType;
import com.lutz.nautilus.xml.data.FileEntry;
import com.lutz.nautilus.xml.data.ModelEntry;
import com.lutz.nautilus.xml.data.PackageEntry;
import com.lutz.nautilus.xml.data.ProjectModel;

public class XMLUtils {

	public static ProjectXML parseProjectXML() {

		ProjectModel model = new ProjectModel();

		ProjectSettings settings = new ProjectSettings();

		String name, id, version, developer, description, distName, distSrcName;

		Map<String, String> valueMap = new HashMap<String, String>();

		XMLParser parser = new XMLParser();

		if (new File(Nautilus.getProjectXMLFilename()).exists()) {

			XMLTree tree = parser.parseXML(new File(Nautilus
					.getProjectDirectory() + Nautilus.getProjectXMLFilename()));

			XMLTag root = tree.getRoot();

			if (root.hasChild("id")) {

				id = root.getChild("id").getValue();

				if (id != null) {

					id = ScriptUtils.parseString(id, valueMap, new String[] {},
							new String[] {});

				} else {

					id = "null";
				}

			} else {

				id = "null";
			}

			valueMap.put("id", id);

			if (root.hasChild("name")) {

				name = root.getChild("name").getValue();

				if (name != null) {

					name = ScriptUtils.parseString(name, valueMap,
							new String[] {}, new String[] {});

				} else {

					name = id;
				}

			} else {

				name = id;
			}

			valueMap.put("name", name);

			if (root.hasChild("version")) {

				version = root.getChild("version").getValue();

				if (version != null) {

					version = ScriptUtils.parseString(version, valueMap,
							new String[] {}, new String[] {});

				} else {

					version = "0";
				}

			} else {

				version = "0";
			}

			valueMap.put("version", version);

			if (root.hasChild("developer")) {

				developer = root.getChild("developer").getValue();

				if (developer != null) {

					developer = ScriptUtils.parseString(developer, valueMap,
							new String[] {}, new String[] {});

				} else {

					developer = "-";
				}

			} else {

				developer = "-";
			}

			valueMap.put("version", developer);

			if (root.hasChild("description")) {

				description = root.getChild("description").getValue();

				if (description != null) {

					description = ScriptUtils.parseString(description,
							valueMap, new String[] {}, new String[] {});

				} else {

					description = "-";
				}

			} else {

				description = "-";
			}

			valueMap.put("description", description);

			if (root.hasChild("distribution")) {

				XMLTag dist = root.getChild("distribution");

				if (dist.hasChild("name")) {

					distName = dist.getChild("name").getValue();

					if (distName != null) {

						distName = ScriptUtils.parseString(distName, valueMap,
								new String[] {}, new String[] {});

					} else {

						distName = name;
					}

				} else {

					distName = name;
				}

				valueMap.put("dist-name", distName);

				if (dist.hasChild("src-name")) {

					distSrcName = dist.getChild("src-name").getValue();

					if (distSrcName != null) {

						distSrcName = ScriptUtils.parseString(distSrcName,
								valueMap, new String[] {}, new String[] {});

					} else {

						distSrcName = name + "-src";
					}

				} else {

					distSrcName = name + "-src";
				}

				valueMap.put("dist-src-name", distSrcName);

			} else {

				distName = name;
				distSrcName = name + "-src";

				valueMap.put("dist-name", distName);
				valueMap.put("dist-src-name", distSrcName);
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

						DependencyPackageType type = DependencyPackageType.EXTERNAL;

						if (dependency.hasAttribute("package-method")) {

							type = DependencyPackageType.parse(dependency
									.getAttribute("package-method"));
						}

						model.addDependencyEntry(new DependencyEntry(dependency
								.getValue(), type));
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

			return new ProjectXML(name, id, version, developer, description,
					distName, distSrcName, model, settings);

		} else {

			LoggingUtils.SYSTEM_OUT
					.log("The project does not contain a project.xml!");
		}

		return null;
	}

	public static ProjectXML generateProjectXML(String name, String id,
			String version, String developer, String description,
			String distName, String distSourceName) {

		ProjectModel model = new ProjectModel();

		LoggingUtils.SYSTEM_OUT.info("Building project settings...");

		ProjectSettings settings = new ProjectSettings();

		settings.fillWithMissing();

		LoggingUtils.SYSTEM_OUT.info("Building project model...");

		if (new File(Nautilus.getProjectDirectory()
				+ Nautilus.getSourceDirectory()).exists()) {

			sortClassModel(
					new File(Nautilus.getProjectDirectory()
							+ Nautilus.getSourceDirectory()),
					new File(Nautilus.getSourceDirectory()).listFiles(), null,
					model);
		}

		LoggingUtils.SYSTEM_OUT.info("Building finalized ProjectXML object...");

		return new ProjectXML(name, id, version, developer, description,
				distName, distSourceName, model, settings);
	}

	private static void sortClassModel(File currentDir, File[] fileList,
			PackageEntry entry, ProjectModel model) {

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

					if (entry != null) {

						entry.addEntry(new ClassEntry(FileUtils
								.removeExtension(file)));

					} else {

						model.addEntry(new ClassEntry(FileUtils
								.removeExtension(file)));
					}

				} else {

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
			String version, String developer, String description,
			String distName, String distSrcName) {

		ProjectXML xml = generateProjectXML(name, id, version, developer,
				description, distName, distSrcName);

		if (xml != null) {

			Map<String, String> valueMap = new HashMap<String, String>();

			XMLTag rootTag = new XMLTag("project");

			XMLTag idTag = new XMLTag("id");
			id = ScriptUtils.parseString(id, valueMap, new String[] {},
					new String[] {});
			idTag.setValue(id);

			valueMap.put("id", id);

			XMLTag nameTag = new XMLTag("name");
			name = ScriptUtils.parseString(name, valueMap, new String[] {},
					new String[] {});
			nameTag.setValue(name);

			valueMap.put("name", name);

			XMLTag versionTag = new XMLTag("version");
			version = ScriptUtils.parseString(version, valueMap,
					new String[] {}, new String[] {});
			versionTag.setValue(version);

			valueMap.put("version", version);

			XMLTag devTag = new XMLTag("developer");
			developer = ScriptUtils.parseString(developer, valueMap,
					new String[] {}, new String[] {});
			devTag.setValue(developer);

			valueMap.put("developer", developer);

			XMLTag descTag = new XMLTag("description");
			description = ScriptUtils.parseString(description, valueMap,
					new String[] {}, new String[] {});
			descTag.setValue(description);

			valueMap.put("description", description);

			XMLTag distTag = new XMLTag("distribution");

			XMLTag distNameTag = new XMLTag("name");
			distName = ScriptUtils.parseString(distName, valueMap,
					new String[] {}, new String[] {});
			distNameTag.setValue(distName);
			distTag.addChild(distNameTag);

			valueMap.put("dist-name", distName);

			XMLTag distSrcNameTag = new XMLTag("src-name");
			distSrcName = ScriptUtils.parseString(distSrcName, valueMap,
					new String[] {}, new String[] {});
			distSrcNameTag.setValue(distSrcName);
			distTag.addChild(distSrcNameTag);

			valueMap.put("dist-src-name", distSrcName);

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
			rootTag.addChild(devTag);
			rootTag.addChild(descTag);
			rootTag.addChild(distTag);
			rootTag.addChild(settingsTag);
			rootTag.addChild(classRegistry);
			rootTag.addChild(fileRegistry);

			XMLTree tree = new XMLTree(rootTag);

			LoggingUtils.SYSTEM_OUT.info("Writing ProjectXML data to "
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

	public static ProjectXML updateProjectXML(boolean updateId, String id,
			boolean updateName, String name, boolean updateVersion,
			String version, boolean updateDeveloper, String developer,
			boolean updateDescription, String description,
			boolean updateFileRef, String distName, boolean updateDistName,
			String distSrcName, boolean updateDistSrcName) {

		LoggingUtils.SYSTEM_OUT.log("Updating project.xml...");

		if (new File(Nautilus.getProjectDirectory()
				+ Nautilus.getProjectXMLFilename()).exists()) {

			XMLTree tree = new XMLParser().parseXML(new File(Nautilus
					.getProjectDirectory() + Nautilus.getProjectXMLFilename()));

			Map<String, String> valueMap = new HashMap<String, String>();

			id = ScriptUtils.parseString(id, valueMap, new String[] {},
					new String[] {});

			if (updateId && id != null) {

				if (tree.getRoot().hasChild("id")) {

					tree.getRoot().getChild("id").setValue(id);
				}
			}

			if (id != null && !id.equals("")) {

				valueMap.put("id", id);

			} else {

				if (tree.getRoot().hasChild("id")) {

					valueMap.put("id", tree.getRoot().getChild("id").getValue());
				}
			}

			name = ScriptUtils.parseString(name, valueMap, new String[] {},
					new String[] {});

			if (updateName && name != null) {

				if (tree.getRoot().hasChild("name")) {

					tree.getRoot().getChild("name").setValue(name);
				}
			}

			if (name != null && !name.equals("")) {

				valueMap.put("name", name);

			} else {

				if (tree.getRoot().hasChild("name")) {

					valueMap.put("name", tree.getRoot().getChild("name")
							.getValue());
				}
			}

			version = ScriptUtils.parseString(version, valueMap,
					new String[] {}, new String[] {});

			if (updateVersion && version != null) {

				if (tree.getRoot().hasChild("version")) {

					tree.getRoot().getChild("version").setValue(version);
				}
			}

			if (version != null && !version.equals("")) {

				valueMap.put("version", version);

			} else {

				if (tree.getRoot().hasChild("version")) {

					valueMap.put("version", tree.getRoot().getChild("version")
							.getValue());
				}
			}

			developer = ScriptUtils.parseString(developer, valueMap,
					new String[] {}, new String[] {});

			if (updateDeveloper && developer != null) {

				if (tree.getRoot().hasChild("developer")) {

					tree.getRoot().getChild("developer").setValue(developer);
				}
			}

			if (developer != null && !developer.equals("")) {

				valueMap.put("developer", developer);

			} else {

				if (tree.getRoot().hasChild("developer")) {

					valueMap.put("developer",
							tree.getRoot().getChild("developer").getValue());
				}
			}

			description = ScriptUtils.parseString(description, valueMap,
					new String[] {}, new String[] {});

			if (updateDescription && description != null) {

				if (tree.getRoot().hasChild("description")) {

					tree.getRoot().getChild("description")
							.setValue(description);
				}
			}

			if (description != null && !description.equals("")) {

				valueMap.put("description", description);

			} else {

				if (tree.getRoot().hasChild("description")) {

					valueMap.put("description",
							tree.getRoot().getChild("description").getValue());
				}
			}

			distName = ScriptUtils.parseString(distName, valueMap,
					new String[] {}, new String[] {});
			distSrcName = ScriptUtils.parseString(distSrcName, valueMap,
					new String[] {}, new String[] {});

			if (updateDistName && distName != null) {

				if (tree.getRoot().hasChild("distribution")) {

					if (tree.getRoot().getChild("distribution")
							.hasChild("name")) {

						tree.getRoot()
								.getChild("distribution")
								.getChild("name")
								.setValue(
										ScriptUtils.parseString(distName,
												valueMap, new String[] {},
												new String[] {}));
					}
				}
			}

			if (description != null && !description.equals("")) {

				valueMap.put("dist-name", distName);

			} else {

				if (tree.getRoot().hasChild("distribution")) {

					if (tree.getRoot().getChild("distribution")
							.hasChild("name")) {

						valueMap.put("dist-name",
								tree.getRoot().getChild("distribution")
										.getChild("name").getValue());
					}
				}
			}

			if (updateDistSrcName && distSrcName != null) {

				if (tree.getRoot().hasChild("distribution")) {

					if (tree.getRoot().getChild("distribution")
							.hasChild("src-name")) {

						tree.getRoot()
								.getChild("distribution")
								.getChild("src-name")
								.setValue(
										ScriptUtils.parseString(distSrcName,
												valueMap, new String[] {},
												new String[] {}));
					}
				}
			}

			if (description != null && !description.equals("")) {

				valueMap.put("dist-src-name", distSrcName);

			} else {

				if (tree.getRoot().hasChild("distribution")) {

					if (tree.getRoot().getChild("distribution")
							.hasChild("src-name")) {

						valueMap.put("dist-src-name",
								tree.getRoot().getChild("distribution")
										.getChild("src-name").getValue());
					}
				}
			}

			if (updateFileRef) {

				ProjectXML xml = generateProjectXML(id, name, version,
						developer, description, distName, distSrcName);

				if (xml != null) {

					if (tree.getRoot().hasChild("class-registry")) {

						XMLTag classRegistry = tree.getRoot().getChild(
								"class-registry");

						classRegistry.clearChildren();

						fillClassRegistryTag(
								xml.getProjectModel().getEntries(), null,
								classRegistry);
					}

					if (tree.getRoot().hasChild("file-registry")) {

						XMLTag fileRegistry = tree.getRoot().getChild(
								"file-registry");

						fileRegistry.clearChildren();

						fillFileRegistryTag(xml.getProjectModel()
								.getFileEntries(), fileRegistry);
					}
				}
			}

			LoggingUtils.SYSTEM_OUT.info("Writing ProjectXML data to "
					+ Nautilus.getProjectXMLFilename() + "...");

			XMLWriter.writeXML(new File(Nautilus.getProjectDirectory()
					+ Nautilus.getProjectXMLFilename()), tree);
		}

		return parseProjectXML();
	}
}
