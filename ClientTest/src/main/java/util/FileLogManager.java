package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class FileLogManager {
	
	static String path = "C:\\Users\\david\\Documents\\eclipseProjects\\ClientTest\\src\\main\\resources";
	
	public static void createFile() {
		try {
			  System.out.println(path);
		      File myObj = new File(path+"\\log.txt");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    } 	
	}
	
	// every time I receive a 
	public static void logBuilder(JSONArray array) {
		createFile();
		try {						
			FileWriter myWriter = new FileWriter(path+"\\log.txt");
			myWriter.write("");
			//TODO: write a function for building the log
			myWriter.write(assembleLog(array).toString());
			myWriter.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}

	public static String readOldLog() {
		try {
			  String data = "";
		      File myObj = new File(path+"\\log.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        data = data.concat(myReader.nextLine());		        
		      }		      
		      myReader.close();
		      return data;
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		      return null;
		    }		
	} 
	
	
	//-----here it will be a method that call all of above to build the all JSONArray for the log-----------------------------------------------
	public static JSONArray assembleLog(JSONArray array) {
		JSONArray jsonarray = new JSONArray();
		jsonarray.put(createLogForPatients(array));
		jsonarray.put(createLogNephectomy(array));
		jsonarray.put(createLogTherapyStartMedication(array));		
		jsonarray.put(createLogRadiotherapy(array));
		jsonarray.put(createLogOtherSurgery(array));
		jsonarray.put(createLogOtherLocalTreatment(array));
		jsonarray.put(createLogTherapyStartObservation(array));
		jsonarray.put(createLogTumorCharatteristics(array));
		jsonarray.put(createLogBrainMets(array));
		return jsonarray;
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------
	
	
	/**
	 * Patient --> record_id && id not ""
	 * 
	 * {
	 * 	"record_id":"1",
	 * 	"id":"ID01"
	 * }
	 * 
	 * 
	 */
	private static JSONArray createLogForPatients(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("id").equals("")))){
				JSONObject json = new JSONObject();
				// NOTA: JSONArray<JSONArray<JSONOBject> !! sono divisi per categoria l'array è ordinato
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("id", array.getJSONObject(i).getString("id"));
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;
	}

	
	/**
	 * Patient --> record_id && id not ""
	 * 
	 * {
	 * 	"record_id":"1",
	 * 	"dmet":"data"
	 * }
	 * 
	 * 
	 */
	public static JSONArray createLogTumorCharatteristics(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("dmet").equals("")))){
				JSONObject json = new JSONObject();
				// NOTA: JSONArray<JSONArray<JSONOBject> !! sono divisi per categoria l'array è ordinato
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("dmet", array.getJSONObject(i).getString("dmet"));
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;
	}
	
	
	
	
	/**
	 * Observation Nepfrectomy
	 * 
	 * {
	 * 	"record_id":"1",
	 *  "nephrectomy_type":"2"
	 *  "nephrectomy_date":"date"
	 * }
	 * 
	 * 
	 * 
	 */
	public static JSONArray createLogNephectomy(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("nephrectomy_type").equals("")))){
				JSONObject json = new JSONObject();
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("nephrectomy_type", array.getJSONObject(i).getString("nephrectomy_type"));
				json.append("nephrectomy_date", array.getJSONObject(i).getString("nephrectomy_date"));
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;
	}
	
	/**
	 * 
	 * Observations
	 * 
	 * {
	 *   "record_id":"1",
	 *   "redcap_event_name" = "second_line_therap_arm_1",
	 *   "line_therapy_start_date" = date
	 *   ""
	 * }
	 * 
	 */
	public static JSONArray createLogTherapyStartObservation(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("redcap_event_name").equals("")))
			&& (!(array.getJSONObject(i).getString("line_therapy_start_date").equals("")))){
				JSONObject json = new JSONObject();
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("redcap_event_name", array.getJSONObject(i).getString("redcap_event_name"));
				json.append("line_therapy_start_date", array.getJSONObject(i).getString("line_therapy_start_date"));
				json.append("line_therapy_drug", array.getJSONObject(i).getString("line_therapy_drug"));
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;
	}
	

	
	
	
	/**
	 * 
	 * Medication
	 * 
	 * {
	 *   "record_id":"1",
	 *   "redcap_event_name" = "second_line_therap_arm_1",
	 *   "line_therapy_start_date" = date
	 *   ""
	 * }
	 * 
	 */
	public static JSONArray createLogTherapyStartMedication(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("redcap_event_name").equals("")))
			&& (!(array.getJSONObject(i).getString("line_therapy_start_date").equals("")))){
				JSONObject json = new JSONObject();
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("redcap_event_name", array.getJSONObject(i).getString("redcap_event_name"));
				json.append("line_therapy_start_date", array.getJSONObject(i).getString("line_therapy_start_date"));
				json.append("line_therapy_drug", array.getJSONObject(i).getString("line_therapy_drug"));
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;
	}
	
	
	/**
	 * 
	 * Radioterapy
	 * 
	 * {
	 *   "record_id":"1",
	 *   "redcap_event_name" = "second_line_therap_arm_1",
	 *   "radiotherapy_start_date" = date
	 *   ""
	 * }
	 * 
	 */
	public static JSONArray createLogRadiotherapy(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("redcap_event_name").equals("")))
			&& (!(array.getJSONObject(i).getString("radiotherapy_start_date").equals("")))){
				JSONObject json = new JSONObject();
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("redcap_event_name", array.getJSONObject(i).getString("redcap_event_name"));
				json.append("radiotherapy_start_date", array.getJSONObject(i).getString("radiotherapy_start_date"));				
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;

	}
	
	/**
	 * 
	 * Other Surgery
	 * 
	 * {
	 *   "record_id":"1",
	 *   "redcap_event_name" = "second_line_therap_arm_1",
	 *   "radiotherapy_start_date" = date
	 *   ""
	 * }
	 * 
	 */
	public static JSONArray createLogOtherSurgery(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("redcap_event_name").equals("")))
			&& (!(array.getJSONObject(i).getString("therapeutic_surgery_date").equals("")))){
				JSONObject json = new JSONObject();
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("redcap_event_name", array.getJSONObject(i).getString("redcap_event_name"));
				json.append("therapeutic_surgery_date", array.getJSONObject(i).getString("therapeutic_surgery_date"));				
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;
	}

	/**
	 * 
	 * Other Local Treatement 
	 * 
	 * {
	 *   "record_id":"1",
	 *   "redcap_event_name" = "second_line_therap_arm_1",
	 *   "radiotherapy_start_date" = date
	 *   ""
	 * }
	 * 
	 */
	public static JSONArray createLogOtherLocalTreatment(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("redcap_event_name").equals("")))
			&& (!(array.getJSONObject(i).getString("local_treatment_date").equals("")))){
				JSONObject json = new JSONObject();
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("redcap_event_name", array.getJSONObject(i).getString("redcap_event_name"));
				json.append("local_treatment_date", array.getJSONObject(i).getString("local_treatment_date"));				
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;
	}
	
	/**
	 * 
	 * BrainMets 
	 * 
	 * {
	 *   "record_id":"1",
	 *   "redcap_event_name" = "second_line_therap_arm_1",
	 *   "radiotherapy_start_date" = date
	 *   ""
	 * }
	 * 
	 */
	public static JSONArray createLogBrainMets(JSONArray array) {
		JSONArray logPatientArray = new JSONArray();
		for(int i = 0; i < array.length();i++) {
			if(!(array.getJSONObject(i).getString("record_id").equals(""))
			&& (!(array.getJSONObject(i).getString("redcap_event_name").equals("")))
			&& (!(array.getJSONObject(i).getString("evaluation_date_brain_mets").equals("")))){
				JSONObject json = new JSONObject();
				json.append("record_id", array.getJSONObject(i).getString("record_id"));
				json.append("redcap_event_name", array.getJSONObject(i).getString("redcap_event_name"));
				json.append("evaluation_date_brain_mets", array.getJSONObject(i).getString("evaluation_date_brain_mets"));				
				logPatientArray.put(json);
			}
		}		
		return logPatientArray;
	}
	
	
}
