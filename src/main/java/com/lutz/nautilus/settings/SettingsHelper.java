package main.java.com.lutz.nautilus.settings;

import main.java.com.lutz.nautilus.Nautilus;
import main.java.com.lutz.nautilus.xml.ProjectXML;

public final class SettingsHelper {

	public static enum Settings {

		BUILD_SOURCES("buildsrc", "true"), LEAVE_PROJECT_XML("leaveprojxml", "true"), LOG_ACTIONS("logactions", "true"), JAVA_VERSION("jrev", Nautilus.getCurrentJavaVersion());

		private String name, defaultValue, value;
		
		private Settings(String name, String defaultValue){
			
			this.name = name;
			this.defaultValue = defaultValue;
			this.value = defaultValue;
		}
		
		public String getXMLName(){
			
			return name;
		}
		
		public String getDefaultValue(){
			
			return defaultValue;
		}
		
		public void setValue(String value){
			
			this.value = value;
		}
		
		public static void setValue(String settingName, String value){
			
			for(Settings s : getSettings()){
				
				if(s.getXMLName().equalsIgnoreCase(settingName)){
					
					s.setValue(value);
				}
			}
		}
		
		public String getValue(){
			
			return value;
		}
		
		public static Settings[] getSettings() {

			return new Settings[] { BUILD_SOURCES, LEAVE_PROJECT_XML,
					LOG_ACTIONS, JAVA_VERSION };
		}
	}
	
	public static void loadSettingsFromProjectXML(ProjectXML xml){
		
		for(SettingsEntry entry : xml.getProjectSettings().getSettings()){
			
			Settings.setValue(entry.getName(), entry.getValue());
		}
	}
}
