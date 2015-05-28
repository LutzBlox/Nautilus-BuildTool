package com.lutz.nautilus.settings;

import java.util.ArrayList;
import java.util.List;

import com.lutz.nautilus.settings.SettingsHelper.Settings;


public class ProjectSettings {

	private List<SettingsEntry> settings = new ArrayList<SettingsEntry>();

	public void addSetting(SettingsEntry entry) {

		settings.add(entry);
	}

	public SettingsEntry[] getSettings() {

		return settings.toArray(new SettingsEntry[] {});
	}

	public boolean hasSetting(String name) {

		for (SettingsEntry entry : settings) {

			if (entry.getName().equalsIgnoreCase(name)) {

				return true;
			}
		}

		return false;
	}

	public SettingsEntry getSettingForName(String name) {

		for (SettingsEntry entry : settings) {

			if (entry.getName().equalsIgnoreCase(name)) {

				return entry;
			}
		}

		return null;
	}

	public void fillWithMissing() {

		for (Settings s : SettingsHelper.Settings.getSettings()) {

			if (!hasSetting(s.getXMLName())) {

				this.addSetting(new SettingsEntry(s.getXMLName(), s
						.getDefaultValue()));
			}
		}
	}
}
