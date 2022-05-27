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
	public static String[] dataOfTherapy;
	public static String[] tumorCharatteristics;
	public static String[] brainMets; 
	public static Map<String,String> tumorCharattCode;
	public static Map<String,String> brainMetsCode;
	private static  String line_therapy_start_date; 
	
	
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
	
	
	//this function will call up
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
		
			if(resourceJson.getString("radiotherapy_sites___1").equals("1")) {
				CodeableConcept codeableConcept_1 = new CodeableConcept();
				Coding c1 = codeableConcept_1.addCoding();
				c1.setCode("272673000");
				c1.setSystem(system_snomed);
				c1.setDisplay("Bone structure");
				liscConcept.add(codeableConcept_1);
			}
			if(resourceJson.getString("radiotherapy_sites___2").equals("1")) {
				CodeableConcept codeableConcept_2 = new CodeableConcept();
				Coding c2 = codeableConcept_2.addCoding();
				c2.setCode("39607008");
				c2.setSystem(system_snomed);
				c2.setDisplay("Lung structure");
				liscConcept.add(codeableConcept_2);
			}
			if(resourceJson.getString("radiotherapy_sites___3").equals("1")) {
				CodeableConcept codeableConcept_3 = new CodeableConcept();
				Coding c3 = codeableConcept_3.addCoding();
				c3.setCode("12738006");
				c3.setSystem(system_snomed);
				c3.setDisplay("Brain structure");	
				liscConcept.add(codeableConcept_3);
			}
			if(resourceJson.getString("radiotherapy_sites___4").equals("1")) {
				CodeableConcept codeableConcept_4 = new CodeableConcept();
				Coding c4 = codeableConcept_4.addCoding();
				c4.setCode("87784001");
				c4.setSystem(system_snomed);
				c4.setDisplay("Soft tissues");	
				liscConcept.add(codeableConcept_4);
			}
			if(resourceJson.getString("radiotherapy_sites___5").equals("1")) {
				CodeableConcept codeableConcept_5 = new CodeableConcept();
				Coding c5 = codeableConcept_5.addCoding();
				c5.setCode("361351001");
				c5.setSystem(system_snomed);
				c5.setDisplay("Bone structure");	
				liscConcept.add(codeableConcept_5);
			}
			if(resourceJson.getString("radiotherapy_sites___6").equals("1")) {
				CodeableConcept codeableConcept_6 = new CodeableConcept();
				Coding c6 = codeableConcept_6.addCoding();
				c6.setCode("272673000");
				c6.setSystem(system_snomed);
				c6.setDisplay("Other structure");	
				liscConcept.add(codeableConcept_6);
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
		
			if(resourceJson.getString("target_organ___1").equals("1")) {
				CodeableConcept codeableConcept_1 = new CodeableConcept();
				Coding c1 = codeableConcept_1.addCoding();
				c1.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___1")[0]);
				c1.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_1);
			}
			if(resourceJson.getString("target_organ___2").equals("1")) {
				CodeableConcept codeableConcept_2 = new CodeableConcept();
				Coding c2 = codeableConcept_2.addCoding();
				c2.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___2")[0]);
				c2.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_2);
			}
			if(resourceJson.getString("target_organ___3").equals("1")) {
				CodeableConcept codeableConcept_3 = new CodeableConcept();
				Coding c3 = codeableConcept_3.addCoding();
				c3.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___3")[0]);
				c3.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_3);
			}
			if(resourceJson.getString("target_organ___4").equals("1")) {
				CodeableConcept codeableConcept_4 = new CodeableConcept();
				Coding c4 = codeableConcept_4.addCoding();
				c4.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___4")[0]);
				c4.setSystem(system_snomed);
				
				liscConcept.add(codeableConcept_4);
			}
			if(resourceJson.getString("target_organ___5").equals("1")) {
				CodeableConcept codeableConcept_5 = new CodeableConcept();
				Coding c5 = codeableConcept_5.addCoding();
				c5.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___5")[0]);
				c5.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_5);
			}
			if(resourceJson.getString("target_organ___6").equals("1")) {
				CodeableConcept codeableConcept_6 = new CodeableConcept();
				Coding c6 = codeableConcept_6.addCoding();
				c6.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___6")[0]);
				c6.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_6);
			}
			if(resourceJson.getString("target_organ___7").equals("1")) {
				CodeableConcept codeableConcept_7 = new CodeableConcept();
				Coding c7 = codeableConcept_7.addCoding();
				c7.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___7")[0]);
				c7.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_7);
			}
			if(resourceJson.getString("target_organ___8").equals("1")) {
				CodeableConcept codeableConcept_8 = new CodeableConcept();
				Coding c8 = codeableConcept_8.addCoding();
				c8.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___8")[0]);
				c8.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_8);
			}
			if(resourceJson.getString("target_organ___9").equals("1")) {
				CodeableConcept codeableConcept_9 = new CodeableConcept();
				Coding c9 = codeableConcept_9.addCoding();
				c9.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___9")[0]);
				c9.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_9);
			}
			if(resourceJson.getString("target_organ___10").equals("1")) {
				CodeableConcept codeableConcept_10 = new CodeableConcept();
				Coding c10 = codeableConcept_10.addCoding();
				c10.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___10")[0]);
				c10.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_10);
			}
			if(resourceJson.getString("target_organ___11").equals("1")) {
				CodeableConcept codeableConcept_11 = new CodeableConcept();
				Coding c11 = codeableConcept_11.addCoding();
				c11.setCode(PropertiesFileManager.getOtherSurgeryCode().get("target_organ___11")[0]);
				c11.setSystem(system_snomed);				
				liscConcept.add(codeableConcept_11);
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
	
		if(resourceJson.getString("target_organ_local_tr___1").equals("1")) {
			CodeableConcept codeableConcept_1 = new CodeableConcept();
			Coding c1 = codeableConcept_1.addCoding();
			c1.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___1")[0]);
			c1.setSystem(system_snomed);				
			liscConcept.add(codeableConcept_1);
		}
		if(resourceJson.getString("target_organ_local_tr___2").equals("1")) {
			CodeableConcept codeableConcept_2 = new CodeableConcept();
			Coding c2 = codeableConcept_2.addCoding();
			c2.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___2")[0]);
			c2.setSystem(system_snomed);				
			liscConcept.add(codeableConcept_2);
		}
		if(resourceJson.getString("target_organ_local_tr___3").equals("1")) {
			CodeableConcept codeableConcept_3 = new CodeableConcept();
			Coding c3 = codeableConcept_3.addCoding();
			c3.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___3")[0]);
			c3.setSystem(system_snomed);				
			liscConcept.add(codeableConcept_3);
		}
		if(resourceJson.getString("target_organ_local_tr___5").equals("1")) {
			CodeableConcept codeableConcept_4 = new CodeableConcept();
			Coding c4 = codeableConcept_4.addCoding();
			c4.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___5")[0]);
			c4.setSystem(system_snomed);
			
			liscConcept.add(codeableConcept_4);
		}
		if(resourceJson.getString("target_organ_local_tr___6").equals("1")) {
			CodeableConcept codeableConcept_5 = new CodeableConcept();
			Coding c5 = codeableConcept_5.addCoding();
			c5.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___6")[0]);
			c5.setSystem(system_snomed);				
			liscConcept.add(codeableConcept_5);
		}
		if(resourceJson.getString("target_organ_local_tr___7").equals("1")) {
			CodeableConcept codeableConcept_6 = new CodeableConcept();
			Coding c6 = codeableConcept_6.addCoding();
			c6.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___7")[0]);
			c6.setSystem(system_snomed);				
			liscConcept.add(codeableConcept_6);
		}
		if(resourceJson.getString("target_organ_local_tr___8").equals("1")) {
			CodeableConcept codeableConcept_7 = new CodeableConcept();
			Coding c7 = codeableConcept_7.addCoding();
			c7.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___8")[0]);
			c7.setSystem(system_snomed);				
			liscConcept.add(codeableConcept_7);
		}
		if(resourceJson.getString("target_organ_local_tr___10").equals("1")) {
			CodeableConcept codeableConcept_8 = new CodeableConcept();
			Coding c8 = codeableConcept_8.addCoding();
			c8.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___10")[0]);
			c8.setSystem(system_snomed);				
			liscConcept.add(codeableConcept_8);
		}
		if(resourceJson.getString("target_organ_local_tr___11").equals("1")) {
			CodeableConcept codeableConcept_9 = new CodeableConcept();
			Coding c9 = codeableConcept_9.addCoding();
			c9.setCode(PropertiesFileManager.getOtherLocalTreatemetCode().get("target_organ_local_tr___11")[0]);
			c9.setSystem(system_snomed);				
			liscConcept.add(codeableConcept_9);
		}
	
	return liscConcept;
	}

	private static List<CodeableConcept> completeCodingTargetOrgans(JSONObject resourceJson) {
		
		List<CodeableConcept> liscConcept = new ArrayList<CodeableConcept>();
			
			//TODO: the codes are wrong!!
		
			if(resourceJson.getString("local_treatment_type___1").equals("1")) {
				CodeableConcept codeableConcept_1 = new CodeableConcept();
				Coding c1 = codeableConcept_1.addCoding();
				c1.setCode("26782000");
				c1.setSystem(system_snomed);
				c1.setDisplay("Cryotherapy");
				liscConcept.add(codeableConcept_1);
			}
			if(resourceJson.getString("local_treatment_type___2").equals("1")) {
				CodeableConcept codeableConcept_2 = new CodeableConcept();
				Coding c2 = codeableConcept_2.addCoding();
				c2.setCode("433058002");
				c2.setSystem(system_snomed);
				c2.setDisplay("Radiofrequency");
				liscConcept.add(codeableConcept_2);
			}
			if(resourceJson.getString("local_treatment_type___3").equals("1")) {
				CodeableConcept codeableConcept_3 = new CodeableConcept();
				Coding c3 = codeableConcept_3.addCoding();
				c3.setCode("229569007");
				c3.setSystem(system_snomed);
				c3.setDisplay("Thermo ablation");	
				liscConcept.add(codeableConcept_3);
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
		identifier.setValue("Internal EHR SerialCode");
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
