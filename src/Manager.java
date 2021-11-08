/*--------------------------------------------------------------------------------------------------------------------------------------------
// AUTHOR: Matthias Mitchell
// FILENAME: Manager.java
// SPECIFICATION: Defines the Manager class, a parent of Employee, and defines the methods frequently used for Listeners, text areas, etc. in
//				   ManagerInterface.java.
// FOR: Shopping application
//------------------------------------------------------------------------------------------------------------------------------------------*/

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Manager extends Employee {
	String mgrID, mgrFstName, mgrLstName, mgrLogin;
	
	// Beginning of constructor
	protected Manager(String mgrID, String firstName, String lastName, String loginID) {
		super(mgrID, firstName, lastName, loginID);
		
		this.mgrID = mgrID;
		mgrFstName = firstName; mgrLstName = lastName;
		mgrLogin = loginID;
	} // End of constructor
	
	
	
	protected static boolean managerExists(String mgrID, String mgrLogin) {
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
				
				if (ver[0].equals(mgrID) && ver[3].equals("Manager") && ver[4].equals(mgrLogin)) {
					return true;
				}
			}
			
			infile.close();
		} catch (IOException e) {}
		
		return false;
	} // End of method
	
	
	protected static void deleteItem(String item) {
		
		try {
			LoginInterface.numInvLines = 0;
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
			
			while (infile.hasNextLine()) {
				infile.nextLine();
				LoginInterface.numInvLines++;
			}
			
			infile.close();
		} catch (IOException e) {}
		
		String[][] comp = new String[LoginInterface.numInvLines][3];
		String line;
		
		try {
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
			
			for (int i = 0; infile.hasNextLine(); i++) {
				line = infile.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(", ");
				
				comp[i][0] = parser.next();
				comp[i][1] = parser.next();
				comp[i][2] = parser.next();
				
				parser.close();
			}
			infile.close();
			
			PrintWriter outfile = new PrintWriter(LoginInterface.INV_LOCATION);
			
			for (int i = 0; i < LoginInterface.numInvLines; i++) {
				if (comp[i][0].equals(item) == false) {
					outfile.print(comp[i][0] + ", " + comp[i][1] + ", " + comp[i][2]);
					
					if (i < LoginInterface.numInvLines - 2) {
						outfile.print("\n");
					} else if (i < LoginInterface.numInvLines - 1 && comp[LoginInterface.numInvLines - 1][0].equals(item) == false) {
						outfile.print("\n");
					}
				}
			}
			
			outfile.close();
		} catch (IOException e) {}
		
	} // End of method
	
	protected static void newItem(String name, String amount, String cost) {
		
		try {
			LoginInterface.numInvLines = 0;
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
			
			while (infile.hasNextLine()) {
				infile.nextLine();
				LoginInterface.numInvLines++;
			}
			
			infile.close();
		} catch (IOException e) {}
		
		String line;
		String[][] comp = new String[LoginInterface.numInvLines + 1][3];
		comp[0][0] = name;
		comp[0][1] = amount;
		comp[0][2] = cost;
		
		try {
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
			
			for (int i = 1; infile.hasNextLine(); i++) {
				line = infile.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(", ");
				
				comp[i][0] = parser.next();
				comp[i][1] = parser.next();
				comp[i][2] = parser.next();
				
				parser.close();
			}
			
			Customer.sortAlphabetically(comp);
			
			PrintWriter outfile = new PrintWriter(LoginInterface.INV_LOCATION);
			
			for (int i = 0; i < LoginInterface.numInvLines; i++) {
				outfile.println(comp[i][0] + ", " + comp[i][1] + ", " + comp[i][2]);
			}
			
			outfile.print( // Print last line with no new line
				comp[LoginInterface.numInvLines][0] + ", " + comp[LoginInterface.numInvLines][1] + ", " + comp[LoginInterface.numInvLines][2]
			);
			
			outfile.close();
			infile.close();
		} catch (IOException e) {}
		
	} // End of method
	
	
	
	protected static String readEmployees() {
		String comp = "ID#\tFIRST\tLAST\tLOGIN\n_____________________________________________";
		String line;
		String[] array = new String[5];
		
		try {
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION + "workerData.txt"));
			while (infile.hasNextLine()) {
				line = infile.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(", ");
				
				for (int i = 0; i < array.length; i++)
					array[i] = parser.next();
				
				if (array[3].equals("Employee")) {
					comp += "\n" + array[0] + "\t" + array[1] + "\t" + array[2] + "\t" + array[4];
				}
				
				parser.close();
			}
			
			infile.close();
		} catch (IOException e) {}
		
		return comp;
		
	} // End of method
	
	
	
	protected static void changeEmployeeInfoAt(int arrayLocation, String changeTo, String empID) {
		
		String[] comp = new String[5];
		String line;
		String out = "";
		
		try {
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION + "workerData.txt"));
			while (infile.hasNextLine()) {
				line = infile.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(", ");
				
				for (int i = 0; i < comp.length; i++) {
					comp[i] = parser.next();
					
					if (comp[0].equals(empID) && i == arrayLocation)
						comp[i] = changeTo;
					
					if (i < comp.length - 1)
						out += comp[i] + ", ";
					else
						out += comp[i];
				}
				
				if (infile.hasNextLine())
					out += "\n";
				
				parser.close();
			}
			
			PrintWriter outfile = new PrintWriter(LoginInterface.LOCATION + "workerData.txt");
			outfile.print(out);
			
			outfile.close();
			infile.close();
		} catch (IOException e) {}
		
	} // End of method
	
} // End of class
