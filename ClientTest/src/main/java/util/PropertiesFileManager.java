package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropertiesFileManager {
	
	static String file_name = "config.properties";
	static Properties properties; 
	

	public static void init() {
		try {
			properties = getProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	// this get the absolute path of the properties file
	public static File getFileProperties() {		
		File file = new File("src/main/resources/"+file_name);
		return file;
	}
	
	public static Properties getProperties() throws IOException{
		Properties prop = new Properties();
		File file = getFileProperties();
		FileInputStream fis = new FileInputStream(file);
		prop.load(fis);	
		return prop ;
	} 

	public static String getToken() {
		return properties.getProperty("redcap.token");
	}

	public static String getRedcapUrl() {
		return properties.getProperty("redcap.url");
	}

	public static String getPatientUrl() {
		return properties.getProperty("omop.patient");
	}

	public static String getObservation() {
		return properties.getProperty("omop.observation");
	}

	public static String getMedication() {
		return properties.getProperty("omop.medication");
	}

	
}
