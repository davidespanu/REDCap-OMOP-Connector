package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import kong.unirest.json.JSONArray;

public class LookUpTableManager {

	static String path = "C:\\Users\\david\\Documents\\eclipseProjects\\ClientTest\\src\\main\\resources";
	public static Map<String,String> lookup = new HashMap<String,String>();
	
	
	public static void createLookUpTable() {
		try {
			  System.out.println(path);
		      File myObj = new File(path+"\\lookuptable.txt");
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
	public static void lookuptableBuilder(String recordID_id) {
		createLookUpTable();
		try {						
			BufferedWriter output = new BufferedWriter(new FileWriter(path+"\\lookuptable.txt", true));
			output.append(recordID_id);
			output.append("\n");
			output.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}

	/**
	 * lookupTable:
	 * record_id:id_OMOP
	 * 
	 */	
	public static String readLookUpTable() {
		try {
			  String data = "";
		      File myObj = new File(path+"\\lookuptable.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        data = myReader.nextLine();
		        System.out.println(data);
		        String[] div = data.split(":");
		        lookup.put(div[0],div[1]);
		      }		      
		      myReader.close();
		      return data;
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		      return null;
		    }		
	} 

	
	

	
}
