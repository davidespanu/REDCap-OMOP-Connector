package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class PropertiesFileManager {
	
	static final String FILE_NAME = "config.properties";
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
		File file = new File("src/main/resources/"+FILE_NAME);
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

	public static String getPatientRecordId() {
		return properties.getProperty("record_id");
	}

	public static String getPatientId() {
		return properties.getProperty("id");
	}

	public static String getPatientGender() {
		return properties.getProperty("gender");
	}

	public static String getPatientDob() {
		return properties.getProperty("data_of_birth");
	}

	public static String getPatientStatus() {
		return properties.getProperty("status");
	}

	public static String[] getTherapyDrug(){
		String line_therapy_drug = properties.getProperty("line_therapy_drug");
		String[] line_therapy = line_therapy_drug.split(",");
		return line_therapy;		
	}
	
	public static String getTherapyStartDate() {
		return properties.getProperty("line_therapy_start_date");
	}
}






