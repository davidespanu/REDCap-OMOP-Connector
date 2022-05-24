package client;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;

import builders.PatientBuilder;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import util.LookUpTableManager;
import util.PropertiesFileManager;
import util.ResourcesDatabaseSender;


public class RedcapClient {

	
	public RedcapClient() {
		PropertiesFileManager.init();
		PatientBuilder.initParams();
	}

	private HttpResponse<JsonNode> getRecords() {
		return Unirest.post(PropertiesFileManager.getRedcapUrl())
				.field("token",PropertiesFileManager.getToken())
				.field("content", "record")
				.field("format", "json")
				.field("returnFormat", "json").asJson();
	}
	
	public JSONArray getRecordsJson(){
		return getRecords().getBody().getArray();
	}
	
	//-------------------------------------------------------------------------
	// insert to database
	public HttpResponse<String> insertPatient(String patient) {
		Unirest.shutDown();
		Unirest.config().verifySsl(false);		
		HttpResponse<String> response = Unirest.post(PropertiesFileManager.getPatientUrl())

				  .header("Content-Type", "application/json")
				  .header("Accept-Encoding", "application/json")
				  .header("Authorization", "Basic Q0FTRU06R0lPUkQx")
				  .body(patient)
				  .asString();
		return response;
	} 
	
	
	public HttpResponse<String> insertObservation(String observation) {
		Unirest.shutDown();
		Unirest.config().verifySsl(false);
		System.out.println(observation);
		HttpResponse<String> response = Unirest.post(PropertiesFileManager.getObservation())
				.header("Content-Type", "application/json")
				  .header("Accept-Encoding", "application/json")
				  .header("Authorization", "Basic Q0FTRU06R0lPUkQx")
				  .body(observation)
				  .asString();
		return response;
	} 
	

	
	public HttpResponse<String> insertMedicationReq(String medication) {
		Unirest.shutDown();
		Unirest.config().verifySsl(false);
		System.out.println(medication);
		HttpResponse<String> response = Unirest.post(PropertiesFileManager.getMedication())
				  .header("Content-Type", "application/json")
				  .header("Accept-Encoding", "application/json")
				  .header("Authorization", "Basic Q0FTRU06R0lPUkQx")
				  .body(medication)
				  .asString();
		return response;
	} 
	
	

	//----------------------------------------------------------------------------------------------------------------------------------
	// insert to database
	private void insertToDatabase(List<DomainResource> resources) {		
		List<DomainResource> otherResources = new ArrayList<DomainResource>();
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();

		for(int i=0; i<resources.size(); i++) {
			if(resources.get(i) instanceof Patient) {				
				Patient patient = (Patient) resources.get(i);
				String identifier = patient.getIdentifierFirstRep().getValue();
				System.out.println("l'identifir trovato è: "+identifier);
				String[] div = identifier.split("_");				
				String serial = parser.encodeResourceToString(patient);
				HttpResponse<String> response = insertPatient(serial);
				System.out.println("lo stato dell'inserimento del paziente è: "+response.getStatus());
				JSONObject body = new JSONObject(response.getBody());				
				String id = (String) body.get("id");				
				LookUpTableManager.lookuptableBuilder(div[1]+":"+id);
				
			}else{
				otherResources.add(resources.get(i));
			}
		}// end for
		
		
		for(int j=0; j<otherResources.size();j++) {
			if(otherResources.get(j) instanceof Observation) {
				Observation observation = (Observation) otherResources.get(j);
				String subject = observation.getSubject().getReference();
				String[] div = subject.split("/");
				LookUpTableManager.readLookUpTable();
				String reference = LookUpTableManager.lookup.get(div[1]);
				System.out.println(LookUpTableManager.lookup.get(div[1]));
				observation.getSubject().setReference("Patient/"+reference);
				String serial = parser.encodeResourceToString(observation);
				HttpResponse<String> response = insertObservation(serial);
				System.out.println("lo stato dell'inserimento dell'observation è: "+response.getStatus());
			}else if(otherResources.get(j) instanceof MedicationRequest){
				MedicationRequest medication = (MedicationRequest) otherResources.get(j);
				String subject = medication.getSubject().getReference();
				String[] div = subject.split("/");
				LookUpTableManager.readLookUpTable();
				String reference = LookUpTableManager.lookup.get(div[1]);
				medication.getSubject().setReference("Patient/"+reference);
				String serial = parser.encodeResourceToString(medication);
				HttpResponse<String> response = insertMedicationReq(serial);
				System.out.println("lo stato dell'inserimento della medication è: "+response.getStatus());
				
			}
		}
		
	}
	
	
	public void insert() {
		ResourcesDatabaseSender rs = new ResourcesDatabaseSender();
		List<DomainResource> resources = rs.ResourcesToSend();
		if(resources.size() != 0) {
			insertToDatabase(resources);
		}else {
			System.out.println("****there are not resources to insert****");
		}
		
	}
	
	
	//----------------------------------------------------------------------------------------------------------------------------------


}
