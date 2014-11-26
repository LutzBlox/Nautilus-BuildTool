package main.java.com.lutz.nautilus.loggers;

import main.java.com.lutz.nautilus.settings.SettingsHelper;

public class LoggingUtils {

	public static final Logger DEFAULT_LOGGER = new Logger(System.out, false, false);
	
	public static boolean canLog(){
		
		try{
			
			return Boolean.parseBoolean(SettingsHelper.Settings.LOG_ACTIONS.getValue());
			
		}catch(Exception e){
			
			return Boolean.parseBoolean(SettingsHelper.Settings.LOG_ACTIONS.getDefaultValue());
		}
	}
}
