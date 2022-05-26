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
import util.FileLogManager;
import util.LookUpTableManager;
import util.PropertiesFileManager;
import util.ResourcesDatabaseSender;


public class Test {

	public static void main(String[] args) {
		
		RedcapClient rc = new RedcapClient();		
		System.out.println(rc.getRecordsJson().getJSONObject(0).toString());
		
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		
		rc.insert(); 
	}// main
	
}
