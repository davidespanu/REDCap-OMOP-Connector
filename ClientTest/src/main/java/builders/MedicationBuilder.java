package builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import util.PropertiesFileManager;

public class MedicationBuilder extends BaseBuilder{
	
	private static  String system_rxnorm;
	private static  String system_snomed;
	private static String line_therapy_start_date;
	public static String[] lineDrugTheraphy;
	public static HashMap<String,String[]> drugs;
	
	public static void init() {
		lineDrugTheraphy = PropertiesFileManager.getTherapyDrug();
		line_therapy_start_date = PropertiesFileManager.getTherapyStartDate();
		drugs = PropertiesFileManager.getMedicationsCode();
		system_snomed = PropertiesFileManager.snomedCode();
		system_rxnorm = PropertiesFileManager.systemRXNORM();
	}
	
	
	public static List<MedicationRequest> getMedicationRequestfromPatient(JSONArray array,String line_terapy,String id,String LineTherapyClass) {
		return builtMadicationRequestListTherapyStart(array,line_terapy,id,LineTherapyClass);		
	}
	
	public static List<MedicationAdministration> getMedicationAdministrationfromPatient(JSONArray array,String line_terapy,String id,String LineTherapyClass) {
		return builtMadicationAdministationListTherapyStart(array,line_terapy,id,LineTherapyClass);		
	}
	
	
	//------------------------------------------------------------------------------------------------
	
	private static JSONArray getTerapyStartInstances(JSONArray array,String line_terapy, String id,String LineTherapyClass){
		JSONArray jarray = new JSONArray();
			for(int i = 0; i<array.length(); i++) {
				if(array.getJSONObject(i).get("redcap_event_name").equals(line_terapy)
				&& !(array.getJSONObject(i).get(LineTherapyClass).equals(""))
				&& array.getJSONObject(i).get("record_id").equals(id))								
				{
					jarray.put(array.getJSONObject(i));;
				}
			}
		return jarray;
	}
	
	// this is to call up
	public static List<MedicationRequest> builtMadicationRequestListTherapyStart(JSONArray array,String line_terapy, String id,String typeObservation){

		JSONArray jarray = getTerapyStartInstances(array,line_terapy,id,typeObservation);
		List<MedicationRequest> listOb= new ArrayList<MedicationRequest>();
		for(int i = 0; i < jarray.length(); i++) {
			if(buildMedicationRequest(jarray.getJSONObject(i),id,typeObservation) != null) {
				listOb.add(buildMedicationRequest(jarray.getJSONObject(i),id,typeObservation));
			}			
		}
		return listOb;
	}

	
	
	private static MedicationRequest buildMedicationRequest(JSONObject resourceJson,String id,String LineTherapyClass) {
		
		MedicationRequest medicationRequest = new MedicationRequest();
		medicationRequest.setStatus(medicationRequest.getStatus().fromCode("active"));
		medicationRequest.setIntent(medicationRequest.getIntent().fromCode("order"));

		if(resourceJson.has(LineTherapyClass)){				
			String drug = resourceJson.getString(LineTherapyClass);
			Coding coding = medicationRequest.getMedicationCodeableConcept().addCoding();
			coding.setCode(drugs.get(drug)[0]);//TODO: add new code for drugs in file excel
			coding.setSystem(system_rxnorm);
			coding.setDisplay(drugs.get(drug)[1]);				
//			}
		}else {
			return null;
		}
		
		medicationRequest.getSubject().setReference("Patient/"+id);

		if(resourceJson.has(line_therapy_start_date)) {
			if(resourceJson.getString(line_therapy_start_date).equals("")) {				
			}else {					
				String d = resourceJson.getString(line_therapy_start_date);				
				medicationRequest.setAuthoredOnElement(new DateTimeType(d));
			}
		}
		
		Coding coding = medicationRequest.getPerformerType().addCoding();
		coding.setCode("309343006");
		coding.setSystem(system_snomed);
		coding.setDisplay("Physician");				
		
		
		
		return medicationRequest;
	}
	
	//Medication Administration -----------------------------------------------------------------------------------------------------------------------------------------
	
	private static JSONArray getTerapyStartInstancesMedicationAdm(JSONArray array,String line_terapy, String id,String LineTherapyClass){
		JSONArray jarray = new JSONArray();
			for(int i = 0; i<array.length(); i++) {
				if(array.getJSONObject(i).get("redcap_event_name").equals(line_terapy)
				&& !(array.getJSONObject(i).get(LineTherapyClass).equals(""))
				&& array.getJSONObject(i).get("record_id").equals(id))								
				{
					jarray.put(array.getJSONObject(i));;
				}
			}
		return jarray;
	}
	
	// this is to call up
	public static List<MedicationAdministration> builtMadicationAdministationListTherapyStart(JSONArray array,String line_terapy, String id,String typeObservation){

		JSONArray jarray = getTerapyStartInstancesMedicationAdm(array,line_terapy,id,typeObservation);
		List<MedicationAdministration> listOb= new ArrayList<MedicationAdministration>();
		for(int i = 0; i < jarray.length(); i++) {
			if(buildMedicationAdministration(jarray.getJSONObject(i),id,typeObservation) != null) {
				listOb.add(buildMedicationAdministration(jarray.getJSONObject(i),id,typeObservation));
			}
		}
		return listOb;
	}

	
	private static MedicationAdministration buildMedicationAdministration(JSONObject resourceJson,String id,String LineTherapyClass) {
		
		MedicationAdministration medicationAdministration = new MedicationAdministration();
		medicationAdministration.setStatus("completed");
		
		if(resourceJson.has(LineTherapyClass)){	
			String drug = resourceJson.getString(LineTherapyClass);
			Coding coding = medicationAdministration.getMedicationCodeableConcept().addCoding();
			coding.setCode(drugs.get(drug)[0]);
			coding.setSystem(system_rxnorm);
			coding.setDisplay(drugs.get(drug)[1]);				
		}else {
			return null;
		}
		
		medicationAdministration.getSubject().setReference("Patient/"+id);

		if(resourceJson.has(line_therapy_start_date)) {
			if(resourceJson.getString(line_therapy_start_date).equals("")) {				
			}else {					
				String d = resourceJson.getString(line_therapy_start_date);				
				DateTimeType date = new DateTimeType(d);  
				medicationAdministration.setEffective(date);
			}
		}
		
		Reference reference = medicationAdministration.getRequest();
		reference.setReference("MedicationRequest/"+679);
								
		return medicationAdministration;
	}
	
	
	
}
