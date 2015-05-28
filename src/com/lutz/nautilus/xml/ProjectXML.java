package com.lutz.nautilus.xml;

import java.util.HashMap;
import java.util.Map;

import com.lutz.nautilus.settings.ProjectSettings;
import com.lutz.nautilus.xml.data.ProjectModel;

public class ProjectXML {

	private ProjectModel model;

	private ProjectSettings settings;

	private String name, id, version, developer, description, distributionName,
			distributionSourceName;

	public ProjectXML(String name, String id, String version, String developer,
			String description, String distributionName,
			String distributionSourceName, ProjectModel model,
			ProjectSettings settings) {

		this.model = model;
		this.settings = settings;
		this.name = name;
		this.id = id;
		this.version = version;
		this.developer = developer;
		this.description = description;
		this.distributionName = distributionName;
		this.distributionSourceName = distributionSourceName;
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

	public String getProjectDeveloper() {

		return developer;
	}

	public String getProjectDescription() {

		return description;
	}

	public String getDistributionName() {

		return distributionName;
	}

	public String getDistributionSourceName() {

		return distributionSourceName;
	}

	public Map<String, String> getStringKeys() {

		Map<String, String> map = new HashMap<String, String>();

		map.put("name", getProjectName());
		map.put("id", getProjectID());
		map.put("version", getProjectVersion());

		return map;
	}
}
