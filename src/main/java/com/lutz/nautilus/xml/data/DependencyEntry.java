package main.java.com.lutz.nautilus.xml.data;

public class DependencyEntry {

	private String path;

	private DependencyPackageType packageType;

	public DependencyEntry(String path) {

		this.path = path;
		this.packageType = DependencyPackageType.EXTERNAL;
	}

	public String getPath() {

		return path;
	}

	public DependencyPackageType getPackageType() {

		return packageType;
	}
}
