package builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.codesystems.ObservationStatisticsEnumFactory;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import util.PropertiesFileManager;

public class ObservationBuilder extends BaseBuilder {

	public static  String nephrectomy;
	private static  String nephrectomy_type;
	private static  String partial_nephrectomy_code;
	private static  String radical_nephrectomy_code;
	private static  String system_snomed;
	private static  String system_mesure;
	private static  String radiotherapy_identifier;
	private static  String line_therapy_start_date;
	public static String[] dataOfTherapy;
	public static String[] tumorCharatteristics;
	public static String[] brainMets; 
	public static String[] radiotherapylist;
	public static String[] otherLocalTreatementList;
	public static String[] ohterLocalTreatementType;
	public static String[] otherSurgeryList;
	public static Map<String,String> tumorCharattCode;
	public static Map<String,String> brainMetsCode;
	private static Map<String,String[]> radiotherapy;
	private static Map<String,String[]> otherLocalTreatement;
	
	
	public static void init() {
		partial_nephrectomy_code = PropertiesFileManager.getPartialNephrectomyCode();
		radical_nephrectomy_code = PropertiesFileManager.getRadicalNephrctomyCode();
		dataOfTherapy = PropertiesFileManager.getTherapyStartData();
		tumorCharatteristics=PropertiesFileManager.getTumorCharatteristicsList();
		brainMets = PropertiesFileManager.getBrainMetsList();
		tumorCharattCode = PropertiesFileManager.getTumorCharatteristicsMap();
		brainMetsCode = PropertiesFileManager.getBrainMetsListMap();
		nephrectomy = PropertiesFileManager.getRadicalNephrctomy();
		nephrectomy_type = PropertiesFileManager.getRadicalNephrctomyType();
		system_snomed = PropertiesFileManager.snomedCode();		
		system_mesure = PropertiesFileManager.snomedMeasure();
		radiotherapy_identifier = PropertiesFileManager.radiotherapyIdentifier();
		line_therapy_start_date = PropertiesFileManager.lineTherapyStartDate();
		radiotherapy = PropertiesFileManager.getRadiotherapyCode();
		otherLocalTreatement = PropertiesFileManager.getOtherLocalTreatementOtherCode();
		radiotherapylist = PropertiesFileManager.getRadiotherapyList();
		otherLocalTreatementList = PropertiesFileManager.getOtherLocalTreatementList();
		ohterLocalTreatementType = PropertiesFileManager.getOtherLocalTreatementtype();
		otherSurgeryList = PropertiesFileManager.getOtherSurgeryList();
	}
	
	
	
	public static Observation getObservationPatient(JSONArray array,String id) { //String type
		JSONObject recordJson = linearSearch(array,id);
			return buildObservationNephrectomy(recordJson,id);
	}
	
	public static List<Observation> getObservationTerapyStart(JSONArray array, String id,String line_terapy,String typeObservation){
		return builtObservationsListTherapyStart(array,line_terapy,id,typeObservation);
	}
	
	public static List<Observation> getObservationRadiotherapy(JSONArray array, String id,String line_terapy){
		return builtObservationsRadiotherapy(array,line_terapy,id);
	}
	
	public static List<Observation> getObservationOtherSurgery(JSONArray array, String id,String line_terapy){
		return buildAllObservarionOtherSurgery(array,line_terapy,id);
	}

	
	public static List<Observation> getObservationOtherLocalTreatement(JSONArray array, String id,String line_terapy){
		return buildAllObservarionOtherLocalTreatement(array,line_terapy,id);
	}
	
	public static List<Observation> getObservationThumorCaratteristics(JSONArray array,String id,String characteristic) { //String type		
			return buildAllObservarionTumorCharacteristic(array,id,characteristic);
	}
	
	public static List<Observation> getObservationBrainMets(JSONArray array,String id,String line_terapy,String characteristic) { 		
		return buildAllObservarionBrainMets(array,id,line_terapy,characteristic);
}
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------
	
	
	private static Observation buildObservationNephrectomy(JSONObject resourceJson,String id) {
		Observation observation = new Observation();
		if(!resourceJson.has(nephrectomy) ||
				(resourceJson.has(nephrectomy) && resourceJson.getString(nephrectomy).equals("0"))) {			
			return null;
		}				
		observation.setStatus(observation.getStatus().fromCode("final"));
		
		if(resourceJson.has(nephrectomy_type)){
			if(resourceJson.getString(nephrectomy_type).equals("1")) {
				Coding coding = observation.getCode().addCoding();
				coding.setCode(partial_nephrectomy_code);
				coding.setSystem(system_snomed);
				coding.setDisplay("Partial nephtectomy");
			}
			else if(resourceJson.getString(nephrectomy_type).equals("2")){
				Coding coding = observation.getCode().addCoding();
				coding.setCode(radical_nephrectomy_code);
				coding.setSystem(system_snomed);
				coding.setDisplay("Radical nephtectomy");
				
			}else {
				Coding coding = observation.getCode().addCoding();
				coding.setCode(radical_nephrectomy_code);
				coding.setSystem(system_snomed);
				coding.setDisplay("Simple nephtectomy");				
			}
		}
		
		String d = resourceJson.getString("nephrectomy_date");				
		observation.setEffective(new DateTimeType(d));

		
		observation.getSubject().setReference("Patient/"+id); 
		return observation;
	}

// observation Therapy Start-----------------------------------------------------------------------------------------------------------------------------------	

	private static JSONArray getTerapyStartInstances(JSONArray array,String line_terapy, String id,String typeObservation){
		JSONArray jarray = new JSONArray();
			for(int i = 0; i<array.length(); i++) {
				if(array.getJSONObject(i).get("redcap_event_name").equals(line_terapy)
				&& !(array.getJSONObject(i).get(typeObservation).equals(""))
				&& array.getJSONObject(i).get("record_id").equals(id))								
				{
					jarray.put(array.getJSONObject(i));;
				}
			}
		return jarray;
	}

	// this will called up
	public static List<Observation> builtObservationsListTherapyStart(JSONArray array,String line_terapy, String id,String typeObservation){

		JSONArray jarray = getTerapyStartInstances(array,line_terapy,id,typeObservation);
		List<Observation> listOb= new ArrayList<Observation>();
		for(int i = 0; i < jarray.length(); i++) {
			listOb.add(buildObservationsTerapyStart(jarray.getJSONObject(i),id,typeObservation));
		}
		return listOb;
	}

	
	
	private static Observation buildObservationsTerapyStart(JSONObject resourceJson,String id,String typeObservation) {
		Observation observation = new Observation();
		if(!resourceJson.has(typeObservation)){			
			return null;
		}
		observation.setStatus(observation.getStatus().fromCode("final"));

		Coding coding = observation.getCode().addCoding();
		coding.setCode(PropertiesFileManager.getObservationCode().get(typeObservation)[0]);
		System.out.println();
		coding.setSystem(system_snomed);
		
		coding.setDisplay(typeObservation);
		
		observation.getSubject().setReference("Patient/"+id);
		
		if(resourceJson.has(line_therapy_start_date)) {
			// yyyy-mm-dd
			if(resourceJson.getString(line_therapy_start_date).equals("")) {				
			}else {					
				String d = resourceJson.getString(line_therapy_start_date);				
				observation.setEffective(new DateTimeType(d));
			}
		}
		
		Quantity quantity = observation.getValueQuantity();
		quantity.setCode(PropertiesFileManager.getObservationCode().get(typeObservation)[1]);
		if(!(resourceJson.getString(typeObservation).equals(""))) {
		quantity.setValue(Double.parseDouble(resourceJson.getString(typeObservation)));
		}		
		quantity.setSystem(system_mesure);
		return observation;
	}
	
	
	
//-----------get list of Radiotherapy based on therapy line and patient id
	private static JSONArray getRadiotherapyInstances(JSONArray array,String line_terapy, String id){
		JSONArray jarray = new JSONArray();
			for(int i = 0; i<array.length(); i++) {
				if(array.getJSONObject(i).get("redcap_event_name").equals(line_terapy)
				&& array.getJSONObject(i).get("redcap_repeat_instrument").equals("radiotherapy")
				&& array.getJSONObject(i).get("record_id").equals(id)) {
					jarray.put(array.getJSONObject(i));;
				}
			}
		return jarray;
	}
	
	
	//this function will be called up
	private static List<Observation> builtObservationsRadiotherapy(JSONArray array,String line_terapy, String id){

		JSONArray jarray = getRadiotherapyInstances(array,line_terapy,id);
		List<Observation> listOb= new ArrayList<Observation>();
		for(int i = 0; i < jarray.length(); i++) {
			listOb.addAll(createCodeableConceptObservationsRadiotherapy(jarray.getJSONObject(i),id));
		}
		return listOb;
	}
	
	
	
	private static List<Observation> createCodeableConceptObservationsRadiotherapy(JSONObject resourceJson, String id){
		List<Observation> observations = new ArrayList<Observation>();
		List<CodeableConcept> list_concept = completeCoding(resourceJson);
		
		for(int i = 0; i < list_concept.size(); i++) {
			observations.add(buildObservarionRadiotherapy(resourceJson,id,list_concept.get(i)));
		}
		
		return observations;
	}

	
	
	private static Observation buildObservarionRadiotherapy(JSONObject resourceJson, String id,CodeableConcept codable) {
		
		Observation observation = new Observation();
		Identifier identifier = observation.addIdentifier();
		identifier.setValue(radiotherapy_identifier);
		observation.setStatus(observation.getStatus().fromCode("final"));

		Coding coding = observation.getCode().addCoding();
		coding.setCode("419815003");
		coding.setSystem("http://snomed.info/sct");
		coding.setDisplay("Radiation oncology");
		
		observation.getSubject().setReference("Patient/"+id);
		if(!(resourceJson.getString("radiotherapy_start_date").equals(""))) {
			
			Coding codingValeCodabole = observation.getValueCodeableConcept().addCoding();
			codingValeCodabole.setSystem(system_snomed);
			codingValeCodabole.setCode("168533005");
			if(!(resourceJson.getString("radiotherapy_stop_date").equals(""))) {
				String d = resourceJson.getString("radiotherapy_stop_date");				
				observation.setEffective(new DateTimeType(d));
				codingValeCodabole.setDisplay("Radiotherapy stopped");
			}else {
				String d = resourceJson.getString("radiotherapy_start_date");				
				observation.setEffective(new DateTimeType(d));
				codingValeCodabole.setDisplay("Radiotherapy started");
			}
		}
		
		observation.setCode(codable);		
		
		Annotation annotation = observation.addNote();
		String nfractions = resourceJson.getString("number_of_fractions");
		String dose = resourceJson.getString("dose_gy");
		String text = (codable.getCoding().get(0).getDisplay())+""+nfractions+" number of fractions, "+dose+" Gy";		
		annotation.setText(text);
		return observation; 
	}
	
	private static List<CodeableConcept> completeCoding(JSONObject resourceJson) {
		
		List<CodeableConcept> liscConcept = new ArrayList<CodeableConcept>();
		
		for(int i = 0; i < radiotherapylist.length; i++) {
			if(resourceJson.getString(radiotherapylist[i]).equals("1")) {
				CodeableConcept codeableConcept = new CodeableConcept();
				Coding c = codeableConcept.addCoding();
				c.setCode(radiotherapy.get(radiotherapylist[i])[0]);
				c.setSystem(system_snomed);
				c.setDisplay(radiotherapy.get(radiotherapylist[i])[1]);	
				liscConcept.add(codeableConcept);
			}
		}

		
		return liscConcept;
	}
	
	
	//-------- build Other Surgery  ---------------------------------------------------------------------------------------
	
	private static JSONArray getOtherSurgeryInstances(JSONArray array,String line_terapy, String id){
		JSONArray jarray = new JSONArray();
			for(int i = 0; i<array.length(); i++) {
				if(array.getJSONObject(i).get("redcap_event_name").equals(line_terapy)
				&& array.getJSONObject(i).get("redcap_repeat_instrument").equals("other_surgery")
				&& array.getJSONObject(i).get("record_id").equals(id)) {
					jarray.put(array.getJSONObject(i));;
				}
			}
		return jarray;
	}
	
	//this function will call up
	private static List<Observation> buildAllObservarionOtherSurgery(JSONArray array,String line_terapy, String id){

		JSONArray jarray = getOtherSurgeryInstances(array,line_terapy,id);
		List<Observation> listOb= new ArrayList<Observation>();
		for(int i = 0; i < jarray.length(); i++) {
			listOb.addAll(createCodeableConceptObservations(jarray.getJSONObject(i),id));
		}
		return listOb;
	}
	
	
	
	private static Observation   buildObservarionOtherSurgery(JSONObject resourceJson, String id,CodeableConcept codable) {
		Observation observation = new Observation();
		Identifier identifier = observation.addIdentifier();
		identifier.setValue(radiotherapy_identifier); // the identifier is the same of the Radiotherapy one
		observation.setStatus(observation.getStatus().fromCode("final"));
		
		if(codable == null) {
			return null;
		}
		observation.setCode(codable);
		observation.getSubject().setReference("Patient/"+id);
		
		String d = resourceJson.getString("therapeutic_surgery_date");				
		observation.setEffective(new DateTimeType(d));
		
		return observation;
	}
	
	
	private static List<Observation> createCodeableConceptObservations(JSONObject resourceJson, String id){
		List<Observation> observations = new ArrayList<Observation>();
		List<CodeableConcept> list_concept = createCodeableConcept(resourceJson);
		
		for(int i = 0; i < list_concept.size(); i++) {
			observations.add(buildObservarionOtherSurgery(resourceJson,id,list_concept.get(i)));
		}
		
		return observations;
	}
	
	private static List<CodeableConcept> createCodeableConcept(JSONObject resourceJson) {
			
			List<CodeableConcept> liscConcept = new ArrayList<CodeableConcept>();
		
			for(int i = 0; i<otherSurgeryList.length; i++) {
				if(resourceJson.getString(otherSurgeryList[i]).equals("1")) {
					CodeableConcept codeableConcept = new CodeableConcept();
					Coding c = codeableConcept.addCoding();
					c.setCode(PropertiesFileManager.getOtherSurgeryCode().get(otherSurgeryList[i])[0]);
					c.setSystem(system_snomed);				
					liscConcept.add(codeableConcept);
				}
				
			}
		return liscConcept;
	}
	
	
	//---- Other Local Treatment -----------------------------------------------------------------------------------------------------
	
	private static JSONArray getOtherLocalTreatementInstances(JSONArray array,String line_terapy, String id){
		JSONArray jarray = new JSONArray();
			for(int i = 0; i<array.length(); i++) {
				if(array.getJSONObject(i).get("redcap_event_name").equals(line_terapy)
				&& array.getJSONObject(i).get("redcap_repeat_instrument").equals("other_local_treatment")
				&& array.getJSONObject(i).get("record_id").equals(id)) {
					jarray.put(array.getJSONObject(i));;
				}
			}
		return jarray;
	}

	
	//this function will call up
	private static List<Observation> buildAllObservarionOtherLocalTreatement(JSONArray array,String line_terapy, String id){

		JSONArray jarray = getOtherLocalTreatementInstances(array,line_terapy,id);
		List<Observation> listOb= new ArrayList<Observation>();
		for(int i = 0; i < jarray.length(); i++) {
			listOb.addAll(createCodeableConceptObservationsOtherLocalTreatement(jarray.getJSONObject(i),id));
		}
		return listOb;
	}

	
	private static Observation   buildObservarionOtherLocalTreatement(JSONObject resourceJson, String id,CodeableConcept code,CodeableConcept bodySite) {
		Observation observation = new Observation();
		Identifier identifier = observation.addIdentifier();
		identifier.setValue(radiotherapy_identifier); // the identifier is the same of the Radiotherapy one
		observation.setStatus(observation.getStatus().fromCode("final"));
		
		if(code == null) {
			return null;
		}
		observation.setCode(code);
		observation.getSubject().setReference("Patient/"+id);
		
		String d = resourceJson.getString("local_treatment_date");				
		observation.setEffective(new DateTimeType(d));
		
		observation.setBodySite(bodySite);
		
		
		return observation;
	}

	private static List<Observation> createCodeableConceptObservationsOtherLocalTreatement(JSONObject resourceJson, String id){
		List<Observation> observations = new ArrayList<Observation>();
		List<CodeableConcept> list_concept = createCodeableConceptTargetOrgans(resourceJson);
		List<CodeableConcept> list_concept_body = completeCodingTargetOrgans(resourceJson);
		
		for(int i = 0; i < list_concept.size(); i++) {
			for(int j = 0; j < list_concept_body.size(); j++) {
				observations.add(buildObservarionOtherLocalTreatement(resourceJson,id,list_concept_body.get(j),list_concept.get(i)));
			}			
		}
		
		return observations;
	}
	
	

	private static List<CodeableConcept> createCodeableConceptTargetOrgans(JSONObject resourceJson) {
		
		List<CodeableConcept> liscConcept = new ArrayList<CodeableConcept>();
	
		for(int i=0; i < otherLocalTreatementList.length; i++) {
			if(resourceJson.getString(otherLocalTreatementList[i]).equals("1")) {
				CodeableConcept codeableConcept = new CodeableConcept();
				Coding c = codeableConcept.addCoding();
				c.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get(otherLocalTreatementList[i])[0]);
				c.setSystem(system_snomed);				
				liscConcept.add(codeableConcept);
			}
		}
		
	return liscConcept;
	}

	private static List<CodeableConcept> completeCodingTargetOrgans(JSONObject resourceJson) {
		
		List<CodeableConcept> liscConcept = new ArrayList<CodeableConcept>();
			
			for(int i = 0; i<ohterLocalTreatementType.length; i++) {
				if(resourceJson.getString(ohterLocalTreatementType[i]).equals("1")) {
					CodeableConcept codeableConcept = new CodeableConcept();
					Coding c = codeableConcept.addCoding();
					c.setCode(otherLocalTreatement.get(ohterLocalTreatementType[i])[0]);
					c.setSystem(system_snomed);
					c.setDisplay(otherLocalTreatement.get(ohterLocalTreatementType[i])[1]);	
					liscConcept.add(codeableConcept);
				}
			}
			
		return liscConcept;
	}
	
	
	
	//Tumor Characteristics --------------------------------------------------------------------------------------------------------
	
	private static JSONArray getTumorCharacteristicInstances(JSONArray array, String id,String characteristic){
		JSONArray jarray = new JSONArray();
			for(int i = 0; i<array.length(); i++) {
				if((array.getJSONObject(i).get(characteristic).equals("1"))
				&& array.getJSONObject(i).get("record_id").equals(id))								
				{
					jarray.put(array.getJSONObject(i));;
				}
			}
		return jarray;
	}

	//this function will call up
	private static List<Observation> buildAllObservarionTumorCharacteristic(JSONArray array, String id,String characteristic){

		JSONArray jarray = getTumorCharacteristicInstances(array,id,characteristic);
		List<Observation> listOb= new ArrayList<Observation>();
		for(int i = 0; i < jarray.length(); i++) {
			listOb.add(buildObservarionThumorCaratteristics(jarray.getJSONObject(i),id,characteristic));
		}		
		return listOb;		
	}
	
	
	private static Observation buildObservarionThumorCaratteristics(JSONObject resourceJson, String id, String characteristic) {
		
		Observation observation = new Observation();
		Identifier identifier = observation.addIdentifier();
		identifier.setValue("Internal EHR SerialCode");
		observation.setStatus(observation.getStatus().fromCode("final"));

		Coding coding = observation.getCode().addCoding();
		coding.setCode(tumorCharattCode.get(characteristic));
		coding.setSystem("http://snomed.info/sct");
		
		
		observation.getSubject().setReference("Patient/"+id);
		
		if(!(resourceJson.getString("dmet").equals(""))) {
			
			String d = resourceJson.getString("dmet");				
			observation.setEffective(new DateTimeType(d));

			
			Coding codingValeCodabole = observation.getValueCodeableConcept().addCoding();
			codingValeCodabole.setSystem(system_snomed);
			codingValeCodabole.setCode("52101004");
			codingValeCodabole.setDisplay("Present");
			}
		
		return observation; 
	}
		
	//------------------------------------------------------------------------------------------------------------------------------
	// Brain Mets
	private static JSONArray getBrainMetsInstances(JSONArray array,String line_terapy, String id){
		JSONArray jarray = new JSONArray();
			for(int i = 0; i<array.length(); i++) {
				if(array.getJSONObject(i).get("redcap_event_name").equals(line_terapy)
				&& (((array.getJSONObject(i).get("brain_mets_present").equals("1"))||
					array.getJSONObject(i).get("brain_mets_present").equals("2")))
				&& array.getJSONObject(i).get("record_id").equals(id)) {
					jarray.put(array.getJSONObject(i));;
				}
			}
		return jarray;
	}

	
	//this function will call up
	private static List<Observation> buildAllObservarionBrainMets(JSONArray array, String id,String line_terapy,String characteristic){

		JSONArray jarray = getBrainMetsInstances(array,line_terapy,id);
		List<Observation> listOb= new ArrayList<Observation>();
		for(int i = 0; i < jarray.length(); i++) {
			listOb.add(buildObservarionBrainMets(jarray.getJSONObject(i),id,characteristic));
		}		
		return listOb;		
	}
	

	
	private static Observation buildObservarionBrainMets(JSONObject resourceJson, String id, String characteristic) {
		
		Observation observation = new Observation();
		Identifier identifier = observation.addIdentifier();
		identifier.setValue(PropertiesFileManager.radiotherapyIdentifier());
		observation.setStatus(observation.getStatus().fromCode("final"));

		Coding coding = observation.getCode().addCoding();
		coding.setCode(brainMetsCode.get(characteristic));
		coding.setSystem("http://snomed.info/sct");
		
		
		observation.getSubject().setReference("Patient/"+id);

		String d = resourceJson.getString("evaluation_date_brain_mets");				
		observation.setEffective(new DateTimeType(d));

		
		if((resourceJson.getString(characteristic).equals("1"))) {

			Coding codingValeCodabole = observation.getValueCodeableConcept().addCoding();
			codingValeCodabole.setSystem(system_snomed);
			codingValeCodabole.setCode("52101004");
			codingValeCodabole.setDisplay("Present");

		}else {

			Coding codingValeCodabole = observation.getValueCodeableConcept().addCoding();
			codingValeCodabole.setSystem(system_snomed);
			codingValeCodabole.setCode("52101004");
			codingValeCodabole.setDisplay("Absent");
			
		}
		
		return observation; 
	}
	
	//------------------------------------------------------------------------------------------------------------------------------
}
