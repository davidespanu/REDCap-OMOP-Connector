package test;

import java.util.HashMap;
import java.util.List;

import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import builders.MedicationBuilder;
import builders.ObservationBuilder;
import builders.PatientBuilder;
import builders.ResourcesBuilder;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import client.RedcapClient;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import util.ExcelParser;
import util.FileLogManager;
import util.LookUpTableManager;
import util.PropertiesFileManager;
import util.ResourcesDatabaseSender;


public class Test {

	public static void main(String[] args) {
		
		
		RedcapClient rc = new RedcapClient();		
		System.out.println(rc.getRecordsJson().getJSONObject(0).toString());
		
		
		//test observation Therapy
//		System.out.println(ObservationBuilder.builtObservationsListTherapyStart(rc.getRecordsJson(),ObservationBuilder.FIRST_LINE_TERAPY,"1", 
//				ObservationBuilder.WEIGTH).size());
		
		// these are by identifier -------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		Patient patient = PatientBuilder.getPatientbyId(rc.getRecordsJson(),"1");	
		String identifier = patient.getIdentifierFirstRep().getValue();
		

		// this is for nephrectomy
		Observation observation =  ObservationBuilder.getObservationPatient(rc.getRecordsJson(),"1");
		
		
		List<Observation> observationTherapyStart =  ObservationBuilder.getObservationTerapyStart(rc.getRecordsJson(),"1",ObservationBuilder.FIRST_LINE_TERAPY, 
																						ObservationBuilder.WEIGTH);
//		System.out.println(observationTherapyStart.size());
		
		List<MedicationRequest> medicationR = MedicationBuilder.getMedicationRequestfromPatient(rc.getRecordsJson(),ObservationBuilder.FIRST_LINE_TERAPY,"1",
																							MedicationBuilder.LINE_THERAPY_CLASSIFIED_1);
		
		List<MedicationAdministration> medicationA  = MedicationBuilder.getMedicationAdministrationfromPatient(rc.getRecordsJson(),ObservationBuilder.FIRST_LINE_TERAPY,"1"
																							,MedicationBuilder.LINE_THERAPY_CLASSIFIED_1);
		
		List<Observation> observationRadiotherapy = ObservationBuilder.getObservationRadiotherapy(rc.getRecordsJson(),"1",
																							ObservationBuilder.FIRST_LINE_TERAPY);
		List<Observation> observationOtherSurgery = ObservationBuilder.getObservationOtherSurgery(rc.getRecordsJson(),"1",
																							ObservationBuilder.FIRST_LINE_TERAPY);
		List<Observation> observationOtherLocalTreatemet = ObservationBuilder.getObservationOtherLocalTreatement(rc.getRecordsJson(),"1",
																							ObservationBuilder.FIRST_LINE_TERAPY);
		List<Observation> ObservatioThumorCaratteristics =  ObservationBuilder.getObservationThumorCaratteristics(rc.getRecordsJson(),"1",
				ObservationBuilder.tumorCharatteristics[0]);

		
		List<Observation> ObservatioBrainMets =  ObservationBuilder.getObservationBrainMets(rc.getRecordsJson(),"1",ObservationBuilder.FIRST_LINE_TERAPY,
				ObservationBuilder.brainMets[0]);

		
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		// serialize Resources
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		
//		System.out.println("\n");
//		String serialized = parser.encodeResourceToString(patient);
//		System.out.println(serialized);
//		
//		System.out.println("\n");
//		if(medicationR != null) {
//			String serialized_3 = parser.encodeResourceToString(medicationR.get(0));
//			System.out.println(serialized_3);
//		}else {
//			System.out.println("no medication r");
//		}
//		System.out.println("\n");
//		if(medicationR != null) {
//			String serialized_4 = parser.encodeResourceToString(medicationA.get(0));
//			System.out.println(serialized_4);
//		}else {
//			System.out.println("no medication r");
//		}
//		System.out.println("\n");
//		if(observation != null) {
//		String serialized_5 = parser.encodeResourceToString(observation);
//		System.out.println(serialized_5);
//		}else {
//			System.out.println("no observation");
//		}
//
//		System.out.println("\n");
//		System.out.println(observationRadiotherapy.size());
//
//		
//		System.out.println("\n");
//		if(observationRadiotherapy.get(0) != null) {
//		String serialized_6 = parser.encodeResourceToString(observationRadiotherapy.get(1));
//		System.out.println(serialized_6);
//		}else {
//			System.out.println("no observation");
//		}
//
//		System.out.println("\n");
//		System.out.println(observationOtherSurgery.size());
//		
//		System.out.println("\n");
//		if(observationOtherSurgery.get(0) != null) {
//		String serialized_7 = parser.encodeResourceToString(observationOtherSurgery.get(0));
//		System.out.println(serialized_7);
//		}else {
//			System.out.println("no observation");
//		}
//
//		System.out.println("\n");
//		System.out.println(observationOtherLocalTreatemet.size());
//		
//		System.out.println("\n");
//		if(observationOtherLocalTreatemet.get(0) != null) {
//		String serialized_8 = parser.encodeResourceToString(observationOtherLocalTreatemet.get(0));
//		System.out.println(serialized_8);
//		}else {
//			System.out.println("no observation");
//		}
//				
//		System.out.println("\n");
//		if(observationTherapyStart != null) {
//			String serialized_9 = parser.encodeResourceToString(observationTherapyStart.get(0));
//			System.out.println(serialized_9);
//		}else {
//			System.out.println("no observation");
//		}
//
//		
//		System.out.println("\n");
//		if(observationTherapyStart != null) {
//			String serialized_10 = parser.encodeResourceToString(ObservatioThumorCaratteristics.get(0));
//			System.out.println(serialized_10);
//		}else {
//			System.out.println("no observation");
//		}
//
//
//		System.out.println("\n");
//		if(ObservatioBrainMets != null) {
//			String serialized_11 = parser.encodeResourceToString(ObservatioBrainMets.get(0));
//			System.out.println(serialized_11);
//		}else {
//			System.out.println("no observation");
//		}

		
// --------------------------------------------------------------------------------------------
		// insert to database
//		System.out.println("\n");
//		HttpResponse<String> response = rc.insertPatient(serialized);		
//		System.out.println(response.getStatus());
//		System.out.println(response.getBody());
//		JSONObject body = new JSONObject(response.getBody());
//		String id = (String) body.get("id");
//		System.out.println("l'id trovato è: "+id);
		
		
//		System.out.println("\n");
//		HttpResponse<String> response = rc.insertPatientTest();
//		System.out.println(response.getStatus());
//		System.out.println(response.getBody());
		
//		System.out.println("\n");
//		String serialized_10 = parser.encodeResourceToString(observationTherapyStart);
//		HttpResponse<String> response = rc.insertObservation(serialized_10);
//		System.out.println(response.getStatus());
//		System.out.println(response.getBody());

		
//		HttpResponse<String> response_2 = rc.insertObservationTest();
//		System.out.println(response_2.getStatus());
//		System.out.println(response_2.getBody());
		
//		System.out.println();
//		System.out.println("\n");		
//		String serial = parser.encodeResourceToString(medicationA.get(0));
////		HttpResponse<String> response = rc.insertObservation(serial);
//		HttpResponse<String> response = rc.insertMedicationReq(serial);
//		System.out.println(response.getStatus());
//		System.out.println(response.getBody());
		

// // test file LOG (build a function or whatever after!) --------------------------------------------------------------------------------------------
//		FileLogManager.createFile();
		
//		JSONArray oldLogJSON = new JSONArray(FileLogManager.readOldLog());		
//		
//		// remember that ther's a order! -> oldLogJSON.get(1) --> check for nephrectomy
//		if(FileLogManager.createLogNephectomy(rc.getRecordsJson()).toString().equals(oldLogJSON.get(1).toString())) {
//			System.out.println("non è necessario aggiungere risorse per la nephrectomy");
//		}
//		else {
//			System.out.println("qualcosa è cambiato aggiungere risorsa nephrectomy");
//		}
//		
//		// remember that ther's a order! -> oldLogJSON.get(1) --> check for therapy start
//		if(FileLogManager.createLogTherapyStartMedication(rc.getRecordsJson()).toString().equals(oldLogJSON.get(2).toString())) {
//			System.out.println("non è necessario aggiungere risorse medication");
//		}
//		else {
//			System.out.println("qualcosa è cambiato aggiungere risorsa medication");
//		}
//		
//		// remember that ther's a order! -> oldLogJSON.get(1) --> check for Radioterapy
//		if(FileLogManager.createLogRadiotherapy(rc.getRecordsJson()).toString().equals(oldLogJSON.get(3).toString())) {
//			System.out.println("non è necessario aggiungere risorse Radiotherapy");
//		}
//		else {
//			System.out.println("qualcosa è cambiato aggiungere risorsa Radiotherapy");
//		}
//		
//		// remember that ther's a order! -> oldLogJSON.get(1) --> check for Other Surgery
//		if(FileLogManager.createLogOtherSurgery(rc.getRecordsJson()).toString().equals(oldLogJSON.get(4).toString())) {
//			System.out.println("non è necessario aggiungere risorse Other surgery");
//		}
//		else {
//			System.out.println("qualcosa è cambiato aggiungere risorsa Other surgery");
//		}
//		
//		// remember that ther's a order! -> oldLogJSON.get(1) --> check for Other local treatment 
//		if(FileLogManager.createLogOtherLocalTreatment(rc.getRecordsJson()).toString().equals(oldLogJSON.get(5).toString())) {
//			System.out.println("non è necessario aggiungere risorse Other local treatement");
//		}
//		else {
//			System.out.println("qualcosa è cambiato aggiungere risorsa Other local treatement");
//		}

		
//		FileLogManager.logBuilder(rc.getRecordsJson());

// test for building all the resources -----------------------------------------------------------------------------------------		

//		ResourcesBuilder builder = new ResourcesBuilder();
//		List<DomainResource> resources = builder.getAllResources();
//		System.out.println(resources.size());		

		rc.insert(); // ***********************************************************************************************
		
// TEST DEL LOG -----------------------------------------------------------------------------------------------------------------		

//		System.out.println("\n");
//		System.out.println("inizia il test per il log");
//		
//		ResourcesDatabaseSender sender = new ResourcesDatabaseSender();
//		List<DomainResource> list = sender.ResourcesToSend();
//		System.out.println(list.size());
//		
//		
//		System.out.println("\n");
//		if(list.size() != 0) {
//			for(int f=0;f<list.size();f++) {
//				System.out.println("\n");
//				System.out.println(parser.encodeResourceToString(list.get(f)));
//			}
//		}
		
	// Test lookuptable---------------------------------------------------------------------------------------------------------------
		
//		LookUpTableManager.lookuptableBuilder("1:226");
//		LookUpTableManager.lookuptableBuilder("2:227");
//		LookUpTableManager.lookuptableBuilder("3:228");
		
//		LookUpTableManager.readLookUpTable();
//		System.out.println(LookUpTableManager.lookup.get("3"));
		
		
		
	//--------------------------------------------------------------------------------------------------------------------------------	
		
	}// main
	
}
