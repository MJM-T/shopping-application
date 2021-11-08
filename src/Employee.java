/*--------------------------------------------------------------------------------------------------------------------------------------------
// AUTHOR: Matthias Mitchell
// FILENAME: Employee.java
// SPECIFICATION: Defines the Employee class, a parent of Customer, and defines a couple methods used miscellaneously.
// FOR: Shopping application
//------------------------------------------------------------------------------------------------------------------------------------------*/

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Employee extends Customer {
	String empID, empFstName, empLstName, empLogin;
	
	
	// Beginning of constructor
	protected Employee(String employeeID, String firstName, String lastName, String loginID) {
		super(firstName, lastName, null, null, null, null);
		
		empID = employeeID;
		empFstName = firstName; empLstName = lastName;
		empLogin = loginID;
	} // End of constructor
	
	
	
	// Beginning of accessors
	protected String getEmpID() {
		return empID;
	}
	protected String getEmpFirstName() {
		return empFstName;
	}
	protected String getEmpLastName() {
		return empLstName;
	}
	protected String getEmpLogin() {
		return empLogin;
	} // End of accessors
	
	
	
	// Convert Employee info into a String to be stored
	protected static String empToString(Employee emp) {
		return emp.getEmpID() + ", " + emp.getEmpFirstName() + ", " + emp.getEmpLastName() + ", " + emp.getEmpLogin();
	} // End of method
	
	
	protected static boolean employeeExists(String empID, String empLogin) {
		String line;
		String[] ver = new String[5];
		
		try {
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION + "workerData.txt"));
			while (infile.hasNextLine()) {
				line = infile.nextLine();
				
				Scanner parser = new Scanner(line);
				parser.useDelimiter(", ");
				for (int i = 0; i < ver.length; i++)
					ver[i] = parser.next();
				
				parser.close();
				
				if (ver[0].equals(empID) && ver[3].equals("Employee") && ver[4].equals(empLogin)) {
					return true;
				}
			}
			
			infile.close();
		} catch (IOException e) {}
		
		return false;
	}
	
}
