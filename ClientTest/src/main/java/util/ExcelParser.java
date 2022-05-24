package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser  {
	
	private static File myFile;
	private static FileInputStream fis;
	private static XSSFWorkbook myWorkBook;
	
	private static void getFile() {
		myFile = new File("C://Users//david//Documents//eclipseProjects//ClientTest//src//main//resources//medications.xlsx");
		try {
			fis = new FileInputStream(myFile);
			myWorkBook = new XSSFWorkbook (fis);
		} catch (FileNotFoundException e) {			
			System.out.println(e.getMessage().toString());
		} catch (IOException e) {			
			System.out.println(e.getMessage().toString());
		}		
	}
	
//	public static HashMap<String,String[]> getMedicationsCode(){
//		getFile();
//		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
//		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
//		for (Iterator iterator = mySheet.rowIterator(); iterator.hasNext();) {
//		    XSSFRow row = (XSSFRow) iterator.next();
//		    Double value =row.getCell(2).getNumericCellValue();
//		    int value_int = value.intValue();
//		    String value_string = Integer.toString(value_int);
//		    String name =row.getCell(3).getStringCellValue();
//		    String[] code_name = {value_string,name};
//		    Double contol = row.getCell(0).getNumericCellValue();
//		    int control_int = contol.intValue();
//		    String control_string = Integer.toString(control_int);
//		    medicationsCode.put(control_string, code_name);  		    
//		}		
//		return medicationsCode;
//	}
	
//---------------------------------------------------------------------------------------------------	
	
	// key --> nameObservation value--> [code,	unit of measurement]
	public static HashMap<String,String[]> getObservationsCode(){
		getFile();
		HashMap<String,String[]> observationsCode = new HashMap<String,String[]>();
		XSSFSheet mySheet = myWorkBook.getSheetAt(1);		
		for (Iterator iterator = mySheet.rowIterator(); iterator.hasNext();) {
		    XSSFRow row = (XSSFRow) iterator.next();		    
		    Double value =row.getCell(2).getNumericCellValue();			    
		    String value_string = Integer.toString(value.intValue());
		    String um = row.getCell(5).getStringCellValue();
		    String[] s = {value_string,um};
		    observationsCode.put(row.getCell(0).getStringCellValue(),s);  		    
		}		
		return observationsCode;		
	}
	
//---------------------------------------------------------------------------------------------------
	
	public static HashMap<String,String[]> getOtherSurgeryCode(){
		getFile();
		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
		XSSFSheet mySheet = myWorkBook.getSheetAt(3);
		for (Iterator iterator = mySheet.rowIterator(); iterator.hasNext();) {
		    XSSFRow row = (XSSFRow) iterator.next();
		    int value_int;
		    try {
		    	Double value =row.getCell(3).getNumericCellValue();
		    	value_int = value.intValue();
		    }catch(IllegalStateException e) {
		    	String value = row.getCell(3).getStringCellValue();
		    	value_int = Integer.parseInt(value);
		    }
		    
		    String value_string = Integer.toString(value_int);
		    String name =row.getCell(1).getStringCellValue();
		    String[] code_name = {value_string,name};
//		    Double contol = row.getCell(0).getNumericCellValue();
//		    int control_int = contol.intValue();
//		    String control_string = Integer.toString(control_int);
		    String control_string =row.getCell(0).getStringCellValue();		    
		    medicationsCode.put(control_string, code_name);  		    
		}		
		return medicationsCode;
	}
	
//-----------------------------------------------------------------------------------------------
	public static HashMap<String,String[]> getOtherLocalTreatemetCode(){
		getFile();
		HashMap<String,String[]> medicationsCode = new HashMap<String,String[]>();
		XSSFSheet mySheet = myWorkBook.getSheetAt(4);
		for (Iterator iterator = mySheet.rowIterator(); iterator.hasNext();) {
		    XSSFRow row = (XSSFRow) iterator.next();
		    int value_int;
		    try {
		    	Double value =row.getCell(3).getNumericCellValue();
		    	value_int = value.intValue();
		    }catch(IllegalStateException e) {
		    	String value = row.getCell(3).getStringCellValue();
		    	value_int = Integer.parseInt(value);
		    }
		    
		    String value_string = Integer.toString(value_int);
		    String name =row.getCell(1).getStringCellValue();
		    String[] code_name = {value_string,name};
//		    Double contol = row.getCell(0).getNumericCellValue();
//		    int control_int = contol.intValue();
//		    String control_string = Integer.toString(control_int);
		    String control_string =row.getCell(0).getStringCellValue();		    
		    medicationsCode.put(control_string, code_name);  		    
		}		
		return medicationsCode;
	}
	
	
}
