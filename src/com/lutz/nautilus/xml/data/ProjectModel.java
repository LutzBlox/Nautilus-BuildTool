package com.lutz.nautilus.xml.data;

import java.util.ArrayList;
import java.util.List;

public class ProjectModel {

	private List<ModelEntry> entries = new ArrayList<ModelEntry>();

	private List<FileEntry> fileEntries = new ArrayList<FileEntry>();

	private List<DependencyEntry> dependencies = new ArrayList<DependencyEntry>();

	public void addEntry(ModelEntry entry) {

		entries.add(entry);
	}

	public void addFileEntry(FileEntry entry) {

		fileEntries.add(entry);
	}

	public ModelEntry[] getEntries() {

		return entries.toArray(new ModelEntry[] {});
	}

	public void addDependencyEntry(DependencyEntry entry) {

		dependencies.add(entry);
	}

	public boolean hasPackageEntry(String name) {

		for (PackageEntry entry : getPackageEntries()) {

			if (entry.getName().equals(name)) {

				return true;
			}
		}

		return false;
	}

	public PackageEntry getPackageEntry(String name) {

		for (PackageEntry entry : getPackageEntries()) {

			if (entry.getName().equals(name)) {

				return entry;
			}
		}

		return null;
	}

	public PackageEntry[] getPackageEntries() {

		List<PackageEntry> packageEntries = new ArrayList<PackageEntry>();

		for (ModelEntry m : entries) {

			if (m instanceof PackageEntry) {

				packageEntries.add((PackageEntry) m);
			}
		}

		return packageEntries.toArray(new PackageEntry[] {});
	}

	public ClassEntry[] getClassEntries() {

		List<ClassEntry> classEntries = new ArrayList<ClassEntry>();

		for (ModelEntry m : entries) {

			if (m instanceof ClassEntry) {

				classEntries.add((ClassEntry) m);
			}
		}

		return classEntries.toArray(new ClassEntry[] {});
	}

	public FileEntry[] getFileEntries() {

		return fileEntries.toArray(new FileEntry[] {});
	}

	public DependencyEntry[] getDependencyEntries() {

		return dependencies.toArray(new DependencyEntry[] {});
	}
}
