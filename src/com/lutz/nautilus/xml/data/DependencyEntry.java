package com.lutz.nautilus.xml.data;

public class DependencyEntry {

	private String path;

	private DependencyPackageType packageType;

	public DependencyEntry(String path, DependencyPackageType type) {

		this.path = path;
		this.packageType = type;
	}

	public String getPath() {

		return path;
	}

	public DependencyPackageType getPackageType() {

		return packageType;
	}
}
