package builders;

import java.util.UUID;

import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class PatientBuilder extends BaseBuilder {
	
	private static final String RECORD_ID = "record_id";
	private static final String ID = "id";
	private static final String GENDER = "gender";
	private static final String DATA_OF_BIRTH = "dob";
	private static final String STATUS = "status";
	
	
	
	public static Patient getPatientbyId(JSONArray array,String id) {
			JSONObject recordJson = linearSearch(array,id);
			return buildPatient(recordJson);
	}
	
	
	
//-------------------------------------------------------------------------------------------------
	
	
	
	private static Patient buildPatient(JSONObject resourceJson){
		Patient patient = new Patient();
			if(resourceJson.has(RECORD_ID)) {
				//patient.setId(resourceJson.getString(RECORD_ID));
				Identifier identifier = patient.addIdentifier();
				identifier.setValue(UUID.randomUUID().toString()+"_"+resourceJson.getString(RECORD_ID)); //fesfos_1
			}
			
			// this should change based on the record
			HumanName humanName = new HumanName();
			humanName.addGiven("Giulio");
			humanName.setFamily("Cesare");
			patient.getName().add(humanName);
			
			if(resourceJson.has(GENDER)) {
				if(resourceJson.getString(GENDER).equals("1")) {
					patient.getGenderElement().setValueAsString("male");
				}else {
					patient.getGenderElement().setValueAsString("female");
				}
			}
			if(resourceJson.has(DATA_OF_BIRTH)) {
				// yyyy-mm-dd
				if(resourceJson.getString(DATA_OF_BIRTH).equals("")) {
					
				}else {					
					String[] date = resourceJson.getString(DATA_OF_BIRTH).split("-");
					patient.setBirthDateElement(new DateType(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2])));
				}
			}
			if(resourceJson.has(STATUS)) {
				if(resourceJson.getString(STATUS).equals("1")) {
					patient.getDeceasedBooleanType().setValue(false);
				}else {
					patient.getDeceasedBooleanType().setValue(true);
				}				
			}
			
		return patient;
	}

	
}
