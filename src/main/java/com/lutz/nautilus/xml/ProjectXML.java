package main.java.com.lutz.nautilus.xml;

import main.java.com.lutz.nautilus.settings.ProjectSettings;
import main.java.com.lutz.nautilus.xml.data.ProjectModel;

public class ProjectXML {

	private ProjectModel model;

	private ProjectSettings settings;

	private String name, id, version;

	public ProjectXML(String name, String id, String version,
			ProjectModel model, ProjectSettings settings) {

		this.model = model;
		this.settings = settings;
		this.name = name;
		this.id = id;
		this.version = version;
	}

	public ProjectModel getProjectModel() {

		return model;
	}

	public ProjectSettings getProjectSettings() {

		return settings;
	}

	public String getProjectName() {

		return name;
	}

	public String getProjectID() {

		return id;
	}

	public String getProjectVersion() {

		return version;
	}
}
