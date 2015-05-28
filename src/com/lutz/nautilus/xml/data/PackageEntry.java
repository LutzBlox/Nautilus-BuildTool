package com.lutz.nautilus.xml.data;

import java.util.ArrayList;
import java.util.List;

public class PackageEntry extends ModelEntry {

	private List<ModelEntry> entries = new ArrayList<ModelEntry>();

	public PackageEntry(String name) {

		super(name);
	}

	public void addEntry(ModelEntry entry) {

		entries.add(entry);
	}

	public ModelEntry[] getEntries() {

		return entries.toArray(new ModelEntry[] {});
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
}
