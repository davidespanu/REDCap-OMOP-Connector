package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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
	
	public static HashMap<String,String[]> getMedicationsCode(){
		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
		String drugs_not_parsed = properties.getProperty("drugs");
		String[] drugs_not_parsed_yet = drugs_not_parsed.split(",");
		for(int i = 0; i<drugs_not_parsed_yet.length; i++) {
			String[] drug_parsed = drugs_not_parsed_yet[i].split(":");
			String control = drug_parsed[0];
			String[] code_name = {drug_parsed[1],drug_parsed[2]};
			medicationsCode.put(control, code_name);
		}
		return medicationsCode;
	}

	public static HashMap<String,String[]> getObservationCode(){
		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
		String drugs_not_parsed = properties.getProperty("observation");
		String[] drugs_not_parsed_yet = drugs_not_parsed.split(",");
		for(int i = 0; i<drugs_not_parsed_yet.length; i++) {
			String[] drug_parsed = drugs_not_parsed_yet[i].split(":");
			String control = drug_parsed[0];
			String[] code_name = {drug_parsed[1],drug_parsed[2]};
			medicationsCode.put(control, code_name);
		}
		return medicationsCode;
	}

	public static HashMap<String,String[]> getOtherSurgeryCode(){
		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
		String drugs_not_parsed = properties.getProperty("othersurgery");
		String[] drugs_not_parsed_yet = drugs_not_parsed.split(",");
		for(int i = 0; i<drugs_not_parsed_yet.length; i++) {
			String[] drug_parsed = drugs_not_parsed_yet[i].split(":");
			String control = drug_parsed[0];
			String[] code_name = {drug_parsed[1],drug_parsed[2]};
			medicationsCode.put(control, code_name);
		}
		return medicationsCode;
	}

	public static HashMap<String,String[]> getOtherLocalTreatemetCode(){
		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
		String drugs_not_parsed = properties.getProperty("otherlocaltreatement");
		String[] drugs_not_parsed_yet = drugs_not_parsed.split(",");
		for(int i = 0; i<drugs_not_parsed_yet.length; i++) {
			String[] drug_parsed = drugs_not_parsed_yet[i].split(":");
			String control = drug_parsed[0];
			String[] code_name = {drug_parsed[1],drug_parsed[2]};
			medicationsCode.put(control, code_name);
		}
		return medicationsCode;
	}

	public static HashMap<String,String[]> getRadiotherapyCode(){
		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
		String drugs_not_parsed = properties.getProperty("radiotherapy_sytes");
		String[] drugs_not_parsed_yet = drugs_not_parsed.split(",");
		for(int i = 0; i<drugs_not_parsed_yet.length; i++) {
			String[] drug_parsed = drugs_not_parsed_yet[i].split(":");
			String control = drug_parsed[0];
			String[] code_name = {drug_parsed[1],drug_parsed[2]};
			medicationsCode.put(control, code_name);
		}
		return medicationsCode;
	}

	public static HashMap<String,String[]> getOtherLocalTreatementOtherCode(){
		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
		String drugs_not_parsed = properties.getProperty("other_local_treatement");
		String[] drugs_not_parsed_yet = drugs_not_parsed.split(",");
		for(int i = 0; i<drugs_not_parsed_yet.length; i++) {
			String[] drug_parsed = drugs_not_parsed_yet[i].split(":");
			String control = drug_parsed[0];
			String[] code_name = {drug_parsed[1],drug_parsed[2]};
			medicationsCode.put(control, code_name);
		}
		return medicationsCode;
	}

	
	public static String[] getTherapyStartData(){
		String observations_not_parsed = properties.getProperty("therapystartobservations");
		return observations_not_parsed.split(",");
	}
	
	public static String[] getTumorCharatteristicsList(){
		String observations_not_parsed = properties.getProperty("tumorcharatteristics");
		String[] s = observations_not_parsed.split(",");
		String[] new_s = new String[s.length];
		for(int i = 0; i<new_s.length; i++) {
			String[] name_code = s[i].split(":");
			new_s[i]=name_code[0];
		}
		return new_s;
	}

	public static HashMap<String,String> getTumorCharatteristicsMap(){
		String observations_not_parsed = properties.getProperty("tumorcharatteristics");
		HashMap<String,String> tumorCharacteristicCode = new HashMap<String,String>();
		String[] s = observations_not_parsed.split(",");
		for(int i = 0;i<s.length;i++) {
			String[] name_code = s[i].split(":");
			tumorCharacteristicCode.put(name_code[0], name_code[1]);
		}
		return tumorCharacteristicCode;
	}
		
	
	
	public static String[] getBrainMetsList(){
		String observations_not_parsed = properties.getProperty("observationbrainmets");
		String[] s = observations_not_parsed.split(",");
		String[] new_s = new String[s.length];
		for(int i = 0; i<new_s.length; i++) {
			String[] name_code = s[i].split(":");
			new_s[i]=name_code[0];
		}
		return new_s;
	}

	public static HashMap<String,String> getBrainMetsListMap(){
		String observations_not_parsed = properties.getProperty("observationbrainmets");
		HashMap<String,String> tumorCharacteristicCode = new HashMap<String,String>();
		String[] s = observations_not_parsed.split(",");
		for(int i = 0;i<s.length;i++) {
			String[] name_code = s[i].split(":");
			tumorCharacteristicCode.put(name_code[0], name_code[1]);
		}
		return tumorCharacteristicCode;
	}

	
	public static String getPartialNephrectomyCode() {
		return properties.getProperty("partial_nephrectomy_code");
	}

	public static String getRadicalNephrctomyCode() {
		return properties.getProperty("radical_nephrecotmy_code");
	}

	public static String getRadicalNephrctomy() {
		return properties.getProperty("neherctomy");
	}

	public static String getRadicalNephrctomyType() {
		return properties.getProperty("nephrectomy_type");
	}

	public static String snomedCode() {
		return properties.getProperty("system_snomed");
	}

	public static String snomedMeasure() {
		return properties.getProperty("system_measure");
	}

	public static String radiotherapyIdentifier() {
		return properties.getProperty("radiotherapy_identifire");
	}

	public static String lineTherapyStartDate() {
		return properties.getProperty("line_therapy_start_date");
	}

	public static String systemRXNORM() {
		return properties.getProperty("system_rxnorm");
	}

}






