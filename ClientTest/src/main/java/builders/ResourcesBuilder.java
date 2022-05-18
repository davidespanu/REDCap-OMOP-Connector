package builders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import client.RedcapClient;
import kong.unirest.json.JSONArray;

public class ResourcesBuilder {
	
	RedcapClient client_redcap = new RedcapClient();
	JSONArray array = client_redcap.getRecordsJson();
	
	public List<DomainResource> getAllResources(){
		List<DomainResource> resources = new ArrayList<DomainResource>();
		resources.addAll(getAllPatient());
		resources.addAll(getAllObservations());
		resources.addAll(getAllMedicationrequest());
		//resources.addAll(getAllMedicationAdministration());
		return resources;
	} 
	
//In order to get all Resources-------------------------------------------------------------------------------------------------
	
	//get all patient
	private List<Patient> getAllPatient(){
		List<Patient> patients = new ArrayList<Patient>();
		String[] id = getAllPatientId().toArray(new String[getAllPatientId().size()]);
		for(int i= 0; i<id.length; i++) {			
			patients.add(PatientBuilder.getPatientbyId(array, id[i]));
		}
		return patients;
	}
	


	//getAllobservations
	private List<Observation> getAllObservations(){
		List<Observation> observations = new ArrayList<Observation>();
		String[] id = getAllPatientId().toArray(new String[getAllPatientId().size()]);
		String[] line_therapy = getAllLineTherapy().toArray(new String[getAllPatientId().size()]);
		
		for(int i = 0; i <id.length; i++) {
			
			for(int z=0; z<ObservationBuilder.tumorCharatteristics.length;z++) {
				List<Observation> ObservatioThumorCaratteristics =  ObservationBuilder.getObservationThumorCaratteristics(client_redcap.getRecordsJson(),
						id[i],
						ObservationBuilder.tumorCharatteristics[z]);
				observations.addAll(ObservatioThumorCaratteristics);
			}
			
			for(int j = 0; j <line_therapy.length; j++) {
				Observation observation =  ObservationBuilder.getObservationPatient(client_redcap.getRecordsJson(),id[i]);
				observations.add(observation);
//				List<Observation> observationTherapyStart =  ObservationBuilder.getObservationTerapyStart(client_redcap.getRecordsJson(),id[i],line_therapy[j], 
//						ObservationBuilder.WEIGTH); //TODO: remember to add all types of Therapy start elemnets 
//				observations.addAll(observationTherapyStart);
				List<Observation> observationRadiotherapy = ObservationBuilder.getObservationRadiotherapy(client_redcap.getRecordsJson(),id[i],
						line_therapy[j]);
				observations.addAll(observationRadiotherapy);
				List<Observation> observationOtherSurgery = ObservationBuilder.getObservationOtherSurgery(client_redcap.getRecordsJson(),id[i],
						line_therapy[j]);
				observations.addAll(observationOtherSurgery);
				List<Observation> observationOtherLocalTreatemet = ObservationBuilder.getObservationOtherLocalTreatement(client_redcap.getRecordsJson(),id[i],
						line_therapy[j]);
				observations.addAll(observationOtherLocalTreatemet);	
				for(int k= 0; k< ObservationBuilder.dataOfTherapy.length;k++) {
					List<Observation> observationTherapyStart =  ObservationBuilder.getObservationTerapyStart(client_redcap.getRecordsJson(),id[i],
							line_therapy[j], 
							ObservationBuilder.dataOfTherapy[k]);
					observations.addAll(observationTherapyStart);
				}
				for(int k= 0; k< ObservationBuilder.brainMets.length;k++) {
					List<Observation> observationTherapyStart =  ObservationBuilder.getObservationBrainMets(client_redcap.getRecordsJson(),id[i],
							line_therapy[j], 
							ObservationBuilder.brainMets[k]);
					observations.addAll(observationTherapyStart);
				}

			}
		}
		
		return observations;
	}

	private List<MedicationRequest> getAllMedicationrequest(){
		List<MedicationRequest> request = new ArrayList<MedicationRequest>();
		String[] id = getAllPatientId().toArray(new String[getAllPatientId().size()]);
		String[] line_therapy = getAllLineTherapy().toArray(new String[getAllPatientId().size()]);

		for(int i = 0; i <id.length; i++) {
			for(int j = 0; j <line_therapy.length; j++) {
				for(int k=0; k<MedicationBuilder.lineDrugTheraphy.length;k++) {
					List<MedicationRequest> medicationR = MedicationBuilder.getMedicationRequestfromPatient(client_redcap.getRecordsJson(),
							line_therapy[j],id[i],
							MedicationBuilder.lineDrugTheraphy[k]);
					request.addAll(medicationR);
				}
			}
		}
		return request;
	}
	
	
//	private List<MedicationAdministration> getAllMedicationAdministration(){
//		String[] id = getAllPatientId().toArray(new String[getAllPatientId().size()]);
//		String[] line_therapy = getAllLineTherapy().toArray(new String[getAllPatientId().size()]);
//		List<MedicationAdministration> administration = new ArrayList<MedicationAdministration>();
//
//		for(int i = 0; i <id.length; i++) {
//			for(int j = 0; j <line_therapy.length; j++) {
//				for(int k=0; k<MedicationBuilder.lineDrugTheraphy.length;k++) {
//					List<MedicationAdministration> medicationA  = MedicationBuilder.getMedicationAdministrationfromPatient(client_redcap.getRecordsJson(),
//							line_therapy[j],id[i]
//							,MedicationBuilder.lineDrugTheraphy[k]);
//					administration.addAll(medicationA);
//				}
//			}
//		}
//		
//		return administration;
//	}
	
	
// with these I can get all the resources I need ------------------------------------------------------------------------------------------------------------------ 	
	private Set<String> getAllPatientId(){
		
		Set<String> ids = new HashSet<String>();		
		for(int i = 0; i < array.length(); i++) {
				ids.add((String) array.getJSONObject(i).get("record_id"));
		}
		
		return ids;
	}
	
	
	private Set<String> getAllLineTherapy(){
		
		Set<String> therapies = new HashSet<String>();		
		for(int i = 0; i < array.length(); i++) {				
				therapies.add((String) array.getJSONObject(i).get("redcap_event_name"));
		}
		return therapies;
	}
	
	
	
	
}
