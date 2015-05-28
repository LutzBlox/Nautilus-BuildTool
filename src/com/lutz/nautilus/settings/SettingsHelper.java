package com.lutz.nautilus.settings;

import com.lutz.nautilus.xml.ProjectXML;

public final class SettingsHelper {

	public static enum Settings {

		LEAVE_PROJECT_XML("leaveprojxml",
				"true"), LOG_ACTIONS("logactions", "true");

		private String name, defaultValue, value;

		private Settings(String name, String defaultValue) {

			this.name = name;
			this.defaultValue = defaultValue;
			this.value = defaultValue;
		}

		public String getXMLName() {

			return name;
		}

		public String getDefaultValue() {

			return defaultValue;
		}

		public void setValue(String value) {

			this.value = value;
		}

		public static void setValue(String settingName, String value) {

			for (Settings s : getSettings()) {

				if (s.getXMLName().equalsIgnoreCase(settingName)) {

					s.setValue(value);
				}
			}
		}

		public String getValue() {

			return value;
		}

		public static Settings[] getSettings() {

			return Settings.values();
		}
	}

	public static void loadSettingsFromProjectXML(ProjectXML xml) {

		for (SettingsEntry entry : xml.getProjectSettings().getSettings()) {

			Settings.setValue(entry.getName(), entry.getValue());
		}
	}
}
