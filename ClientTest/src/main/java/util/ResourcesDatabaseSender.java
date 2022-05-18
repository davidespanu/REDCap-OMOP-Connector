package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;

import builders.MedicationBuilder;
import builders.ObservationBuilder;
import builders.ResourcesBuilder;
import client.RedcapClient;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class ResourcesDatabaseSender {

	RedcapClient rc = new RedcapClient();
	ResourcesBuilder rb = new ResourcesBuilder();
	
	public List<DomainResource> ResourcesToSend() {
		
		List<DomainResource> newResources = new ArrayList<DomainResource>();
		
		FileLogManager.createFile();
		String oldLod = FileLogManager.readOldLog();
		
		if(oldLod.equals("")) {
			FileLogManager.logBuilder(rc.getRecordsJson());
			return rb.getAllResources();
		}
		
		
		JSONArray oldLogJSON = new JSONArray(FileLogManager.readOldLog());	
		// remember that ther's a order! -> oldLogJSON.get(1) --> check for nephrectomy
		if(FileLogManager.createLogNephectomy(rc.getRecordsJson()).toString().equals(oldLogJSON.get(1).toString())) {
			System.out.println("non ci sono cambiamenti nella nefrectomy");
			//return newResources; // in this case it return an empty array
		}
				
		if((FileLogManager.createLogNephectomy(rc.getRecordsJson()).length()) > (oldLogJSON.getJSONArray(1).length())) {
			
			JSONArray dif = checkAdd(FileLogManager.createLogNephectomy(rc.getRecordsJson()),oldLogJSON.getJSONArray(1));					
			for(int i = 0; i < dif.length(); i++) {					
				Observation observation =  ObservationBuilder.getObservationPatient(rc.getRecordsJson(),dif.getJSONObject(i).getString("record_id"));
				newResources.add(observation);
			}
		}

		//-------------------------------------------------------------------------
		// remember that ther's a order! -> oldLogJSON.get(1) --> check for Radiotherapy
		if(FileLogManager.createLogRadiotherapy(rc.getRecordsJson()).toString().equals(oldLogJSON.get(3).toString())) {
			System.out.println("non ci sono cambiamenti nella Radioterapy");
			//return newResources; // in this case it return an empty array
		}
				
		if((FileLogManager.createLogRadiotherapy(rc.getRecordsJson()).length()) > (oldLogJSON.getJSONArray(3).length())) {
			
			System.out.println("sono entrato qui perchè qualcosa è cambiato");
			
			JSONArray dif = checkAdd(FileLogManager.createLogRadiotherapy(rc.getRecordsJson()),oldLogJSON.getJSONArray(3));		
			System.out.println(dif.getJSONObject(0).toString());
			System.out.println(dif.getJSONObject(0).getString("record_id"));
			for(int j = 0; j < dif.length(); j++) {					
				List<Observation> observationRadiotherapy = ObservationBuilder.getObservationRadiotherapy(rc.getRecordsJson(),dif.getJSONObject(j).getString("record_id"),
						dif.getJSONObject(j).getString("redcap_event_name")); 
				newResources.addAll(observationRadiotherapy);
			}
		}		
		//-----------------------------------------------------------------------------
		//---Other surgery 
		if(FileLogManager.createLogOtherSurgery(rc.getRecordsJson()).toString().equals(oldLogJSON.get(4).toString())) {
			System.out.println("non ci sono cambiamenti nella other surgery");
			//return newResources; // in this case it return an empty array
		}
				
		if((FileLogManager.createLogOtherSurgery(rc.getRecordsJson()).length()) > (oldLogJSON.getJSONArray(4).length())) {
			
			System.out.println("sono entrato qui perchè qualcosa è cambiato");
			
			JSONArray dif = checkAdd(FileLogManager.createLogOtherSurgery(rc.getRecordsJson()),oldLogJSON.getJSONArray(4));		
			System.out.println(dif.getJSONObject(0).toString());
			System.out.println(dif.getJSONObject(0).getString("record_id"));
			for(int j = 0; j < dif.length(); j++) {					
				List<Observation> observationOtherSurgery = ObservationBuilder.getObservationOtherSurgery(rc.getRecordsJson(),dif.getJSONObject(j).getString("record_id"),
						dif.getJSONObject(j).getString("redcap_event_name")); 
				newResources.addAll(observationOtherSurgery);
			}
		}		
		
		//-------------------------------------------------------------------------------
		//---Other local treatment
		if(FileLogManager.createLogOtherLocalTreatment(rc.getRecordsJson()).toString().equals(oldLogJSON.get(5).toString())) {
			System.out.println("non ci sono cambiamenti nella other local treatement");
			//return newResources; // in this case it return an empty array
		}
				
		if((FileLogManager.createLogOtherLocalTreatment(rc.getRecordsJson()).length()) > (oldLogJSON.getJSONArray(5).length())) {
			
			System.out.println("sono entrato qui perchè qualcosa è cambiato");
			
			JSONArray dif = checkAdd(FileLogManager.createLogOtherLocalTreatment(rc.getRecordsJson()),oldLogJSON.getJSONArray(5));		
			System.out.println(dif.getJSONObject(0).toString());
			System.out.println(dif.getJSONObject(0).getString("record_id"));
			for(int j = 0; j < dif.length(); j++) {					
				List<Observation> observationOtherLocalTreatemet = ObservationBuilder.getObservationOtherLocalTreatement(rc.getRecordsJson(),dif.getJSONObject(j).getString("record_id"),
						dif.getJSONObject(j).getString("redcap_event_name")); 
				newResources.addAll(observationOtherLocalTreatemet);
			}
		}
		
		//--------------------------------------------------------------------------------

		//-------------------------------------------------------------------------------
		//---therapy start Observation
		if(FileLogManager.createLogTherapyStartObservation(rc.getRecordsJson()).toString().equals(oldLogJSON.get(6).toString())) {
			System.out.println("non ci sono cambiamenti nella therapy start");
			//return newResources; // in this case it return an empty array
		}
				
		if((FileLogManager.createLogTherapyStartObservation(rc.getRecordsJson()).length()) > (oldLogJSON.getJSONArray(6).length())) {
			
			System.out.println("sono entrato qui perchè qualcosa è cambiato");
			
			JSONArray dif = checkAdd(FileLogManager.createLogTherapyStartObservation(rc.getRecordsJson()),oldLogJSON.getJSONArray(6));		
			System.out.println(dif.getJSONObject(0).toString());
			System.out.println(dif.getJSONObject(0).getString("record_id"));
			for(int j = 0; j < dif.length(); j++) {	
				for(int k=0;k<ObservationBuilder.dataOfTherapy.length;k++) {
					List<Observation> observationTherapyStart =  ObservationBuilder.getObservationTerapyStart(rc.getRecordsJson(),dif.getJSONObject(j).getString("record_id"),
							dif.getJSONObject(j).getString("redcap_event_name"), 
							ObservationBuilder.dataOfTherapy[k]);
					newResources.addAll(observationTherapyStart);
				}				
			}
		}
		
		//--------------------------------------------------------------------------------

		if(FileLogManager.createLogTherapyStartMedication(rc.getRecordsJson()).toString().equals(oldLogJSON.get(2).toString())) {
			System.out.println("non ci sono cambiamenti nella medication Resources");
			//return newResources; // in this case it return an empty array
		}
				
		if((FileLogManager.createLogTherapyStartMedication(rc.getRecordsJson()).length()) > (oldLogJSON.getJSONArray(2).length())) {
			
			System.out.println("sono entrato qui perchè qualcosa è cambiato");
			
			JSONArray dif = checkAdd(FileLogManager.createLogTherapyStartMedication(rc.getRecordsJson()),oldLogJSON.getJSONArray(2));		
			System.out.println(dif.getJSONObject(0).toString());
			System.out.println(dif.getJSONObject(0).getString("record_id"));
			for(int j = 0; j < dif.length(); j++) {	
				for(int k=0;k<MedicationBuilder.lineDrugTheraphy.length;k++) {
					List<MedicationRequest> medicationR =  MedicationBuilder.getMedicationRequestfromPatient(rc.getRecordsJson(),dif.getJSONObject(j).getString("redcap_event_name"),
							dif.getJSONObject(j).getString("record_id"),							
							MedicationBuilder.lineDrugTheraphy[k]);
					newResources.addAll(medicationR);
//					List<MedicationAdministration> medicationA =  MedicationBuilder.getMedicationAdministrationfromPatient(rc.getRecordsJson(),dif.getJSONObject(j).getString("redcap_event_name"),
//							dif.getJSONObject(j).getString("record_id"),							
//							MedicationBuilder.lineDrugTheraphy[k]);
//					newResources.addAll(medicationA);					
				}				
			}
		}
		
		//--------------------------------------------------------------------------------
		// Tumor characteristics
		if(FileLogManager.createLogTumorCharatteristics(rc.getRecordsJson()).toString().equals(oldLogJSON.get(7).toString())) {
			System.out.println("non ci sono cambiamenti nelle tumor caratteriscs");
			//return newResources; // in this case it return an empty array
		}
				
		if((FileLogManager.createLogTumorCharatteristics(rc.getRecordsJson()).length()) > (oldLogJSON.getJSONArray(7).length())) {
			
			System.out.println("sono entrato qui perchè qualcosa è cambiato");
			
			JSONArray dif = checkAdd(FileLogManager.createLogTumorCharatteristics(rc.getRecordsJson()),oldLogJSON.getJSONArray(7));		
			System.out.println(dif.getJSONObject(0).toString());
			System.out.println(dif.getJSONObject(0).getString("record_id"));
			for(int j = 0; j < dif.length(); j++) {	
				for(int k=0;k<ObservationBuilder.tumorCharatteristics.length;k++) {
					List<Observation> ObservatioThumorCaratteristics =  ObservationBuilder.getObservationThumorCaratteristics(rc.getRecordsJson(),
							dif.getJSONObject(j).getString("record_id"),
							ObservationBuilder.tumorCharatteristics[k]);
					newResources.addAll(ObservatioThumorCaratteristics);
				}				
			}
		}
		
		
		//---------------------------------------------------------------------------------
		// Brain Mets
		if(FileLogManager.createLogBrainMets(rc.getRecordsJson()).toString().equals(oldLogJSON.get(8).toString())) {
			System.out.println("non ci sono cambiamenti nelle Brain Mets");
			//return newResources; // in this case it return an empty array
		}
				
		if((FileLogManager.createLogBrainMets(rc.getRecordsJson()).length()) > (oldLogJSON.getJSONArray(8).length())) {
			
			System.out.println("sono entrato qui perchè qualcosa è cambiato");
			
			JSONArray dif = checkAdd(FileLogManager.createLogBrainMets(rc.getRecordsJson()),oldLogJSON.getJSONArray(8));		
			System.out.println(dif.getJSONObject(0).toString());
			System.out.println(dif.getJSONObject(0).getString("record_id"));
			for(int j = 0; j < dif.length(); j++) {	
				for(int k=0;k<ObservationBuilder.brainMets.length;k++) {
					List<Observation> ObservatioBrainMets =  ObservationBuilder.getObservationBrainMets(rc.getRecordsJson(),dif.getJSONObject(j).getString("record_id"),
							dif.getJSONObject(j).getString("redcap_event_name"), 
							ObservationBuilder.brainMets[k]);
					newResources.addAll(ObservatioBrainMets);
				}				
			}
		}
		
		
		//---------------------------------------------------------------------------------
		
		FileLogManager.logBuilder(rc.getRecordsJson());
		return newResources;
	}
	
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	private JSONArray checkAdd(JSONArray first,JSONArray second) {
		JSONArray difference = new JSONArray();
		int[] index_different = new int[first.length()];
		
			for(int i = 0; i < second.length(); i++) {
				for(int j = 0; j < first.length(); j++) {
					if (first.getJSONObject(j).toString().equals(second.getJSONObject(i).toString())){
						index_different[j]++;
	                }
				}
			}
		
		for(int k=0; k<first.length();k++) {
			if(index_different[k] == 0) {
				difference.put(first.get(k));
			}
		}
		
		return difference;
	}
	
	private void checkRemove() { //JSONArray first,JSONArray second --> give this as parameters
		//TODO: here I'll write the removed resources different logic 
		System.out.println("è stata tolta una risorsa ");
	}

}
