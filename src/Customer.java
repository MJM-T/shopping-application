/*--------------------------------------------------------------------------------------------------------------------------------------------
// AUTHOR: Matthias Mitchell
// FILENAME: Customer.java
// SPECIFICATION: Defines the Customer class that is referenced frequently in CustomerInterface.java as well as defining several methods
//				   related to creating new Customer files, manipulating Customer and Inventory information, and writing to files.
// FOR: Shopping application
//------------------------------------------------------------------------------------------------------------------------------------------*/

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;
import java.text.NumberFormat;

public class Customer {
	private String cstmrFstName, cstmrLstName, cstmrAddress, cstmrCity, cstmrState, cstmrZip;
	protected static int adjustedOrderNumber;
	public static boolean outOfStock = false;
	public static boolean custDataFound = false;
	
	
	// Beginning of constructor
	protected Customer(String firstName, String lastName, String address, String city, String state, String zip) {
		cstmrFstName = firstName; cstmrLstName = lastName;
		cstmrAddress = address; cstmrCity = city; cstmrState = state; cstmrZip = zip;
	} // End of constructor
	
	
	
	// Beginning of accessors
	protected String getCustomerFirstName() {
		return cstmrFstName;
	}
	protected String getCustomerLastName() {
		return cstmrLstName;
	}
	protected String getCustomerAddress() {
		return cstmrAddress;
	}
	protected String getCustomerCity() {
		return cstmrCity;
	}
	protected String getCustomerState() {
		return cstmrState;
	}
	protected String getCustomerZip() {
		return cstmrZip;
	} // End of accessors
	
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------
// Define the static methods involved with reading, writing to, and accessing Customer/inventory files
//--------------------------------------------------------------------------------------------------------------------------------------------
	
	// Convert Customer info to a string that can be stored //
	protected static String customerToString(Customer customer) {
		return customer.getCustomerFirstName() + ", " + customer.getCustomerLastName() + ", "
				+ customer.getCustomerAddress() + ", " + customer.getCustomerCity() + ", "
				+ customer.getCustomerState() + ", " + customer.getCustomerZip();
	} // End of method
	
	
	
	
	// Test if a given customer has already been assigned a file //
	protected static boolean similarCustomerExists(Customer customer) {
		File test = new File(LoginInterface.LOCATION + customer.getCustomerFirstName() + customer.getCustomerLastName() + ".txt");
		
		if ( test.exists() ) {
			return true;
		} else {
			return false;
		}
	} // End of method
	
	
	
	
	// Create a customer file with the format "FirstLast.txt" //
	public static void createCustomerFile(Customer customer) {
		new File(LoginInterface.LOCATION + customer.getCustomerFirstName() + customer.getCustomerLastName() + ".txt");
	} // End of method
	
	
	
	
	// Write a single String to a file. //
	protected static void write(String location, String string) throws IOException {
		PrintWriter outfile = new PrintWriter(location);
		
		outfile.println(string);
		outfile.close();
	} // End of method
	/* NOTE: "write" clears the file with each usage. Used to begin (new Customer) or reset (deleted order) files.
	 */
	
	
	
	
	// Read specifically from the inventory and return the contents, reformatted //
	protected static String readInventory() {
		try {
			
		Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
		String line;
		String comp = "ITEM\t\tAMOUNT\tCOST\n___________________________________________";
		String item = "", amt = "", cost = "";
		NumberFormat currency = NumberFormat.getCurrencyInstance();
		
		while ( infile.hasNextLine() ) {
			line = infile.nextLine();
			Scanner lineParse = new Scanner(line);
			lineParse.useDelimiter(", ");
			
			item = lineParse.next();
			amt = lineParse.next();
			cost = lineParse.next();
			
			comp += "\n" + String.format("%.12s", item) + "\t\t" + amt + "\t" + currency.format(Double.valueOf(cost));
			
			lineParse.close();
		}
		
		infile.close();
		return comp;
		
		} catch (IOException e) {return null;}
	} // End of method
	/*  NOTE: Despite troubleshooting, Eclipse persists in giving a FileNotFoundException whenever I use a FileReader, despite the file
	 * 		   path being accurate, hence the try-catch statements that are never used when reading/writing files.
	 */
	
	
	
	// Given an array of item names and amounts, adjust the amounts in the inventory accordingly //
	protected static void addInventoryItems(String[] items, int[] amounts) {
		
		try {
			LoginInterface.numInvLines = 0; // Recalculate the number of lines in inventory.txt
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
			
			while (infile.hasNextLine()) {
				infile.nextLine();
				LoginInterface.numInvLines++;
			}
			
			infile.close();
		} catch (IOException e) {}
		
		
		String[] oldItems = new String[LoginInterface.numInvLines];
		int[] oldAmounts = new int[LoginInterface.numInvLines];
		double[] oldCosts = new double[LoginInterface.numInvLines];
		outOfStock = false;
		
		
		try {
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
			
			for (int i = 0; i < LoginInterface.numInvLines; i++) {
				
				Scanner parser = new Scanner(infile.nextLine());
				parser.useDelimiter(", ");
				
				oldItems[i] = parser.next();
				oldAmounts[i] = Integer.valueOf(parser.next());
				oldCosts[i] = Double.valueOf(parser.next());
				
				parser.close();
			}
			for (int i = 0; i < items.length; i++) {
				for (int j = 0; j < LoginInterface.numInvLines; j++) {
					
					if (items[i].equals(oldItems[j])) {
						
						oldAmounts[j] += amounts[i];
						
						if (oldAmounts[j] < 0) {
							outOfStock = true;
						}
						
					}
				}
			}
			infile.close();
			
			if (outOfStock == false) {
				
				PrintWriter outfile = new PrintWriter(LoginInterface.INV_LOCATION);
				
				for (int i = 0 ; i < LoginInterface.numInvLines - 1; i++) {
					
					outfile.println(oldItems[i] + ", " + oldAmounts[i] + ", " + oldCosts[i]);
				}
					
				outfile.print(oldItems[LoginInterface.numInvLines - 1] + ", " + oldAmounts[LoginInterface.numInvLines - 1]
					+ ", " + oldCosts[LoginInterface.numInvLines - 1]);
				
				outfile.close();
			}
			
		} catch (IOException e) {}
	}
	
	
	
	// Given an item name, find the current amount in inventory.txt //
	protected static String getItemAmount(String item) {
		try {
		
		Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
		String line; String amt = null;
		
		while (infile.hasNextLine()) {
			
			line = infile.nextLine();
			Scanner parseItems = new Scanner(line);
			parseItems.useDelimiter(", ");
			
			if (parseItems.next().equalsIgnoreCase(item)) {
				amt = parseItems.next();
			}
			
			parseItems.close();
		}
		
		infile.close();
		return amt;
		} catch (IOException e) {return null;}
	} // End of method
	
	
	
	// Given an item name, find its current amount in inventory.txt //
	protected static String getItemCost(String item) {
		try {
			
		Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
		String line; String cost = null;
		
		while (infile.hasNextLine()) {
			
			line = infile.nextLine();
			Scanner parseItems = new Scanner(line);
			parseItems.useDelimiter(", ");
			
			if (parseItems.next().equalsIgnoreCase(item)) {
				parseItems.next();
				cost = parseItems.next();
			}
			
			parseItems.close();
		}
		
		infile.close();
		return cost;
		} catch (IOException e) {return null;}
	} // End of method
	
	
	
	// Check inventory.txt to see if a given item exists //
	protected static boolean inventoryItemExists(String item) {
		try {
		
		Scanner infile = new Scanner(new FileReader(LoginInterface.INV_LOCATION));
		String line;
		
		while (infile.hasNextLine()) {
			
			line = infile.nextLine();
			Scanner parseItems = new Scanner(line);
			parseItems.useDelimiter(", ");
			
			if (parseItems.next().equalsIgnoreCase(item)) {
				parseItems.close();
				return true;
			}
			
			parseItems.close();
		}
		
		infile.close();
		return false;
		} catch (IOException e) {return false;}
	} // End of method
	
	
	
	// Use a basic insertion sort to sort an array in ascending alphabetical order //
	protected static String[][] sortAlphabetically(String[][] array) {
		String[] temp;
		
		for (int i = 1; i < array.length; i++) {
			for (int j = i; j > 0; j--) {
				if (array[j][0] == null) {
					
				} else if (array[j][0].compareTo(array[j-1][0]) < 0) {
					temp = array[j];
					array[j] = array[j-1];
					array[j-1] = temp;
				}
			}
		}
		
		return array;
	} // End of method
	
	
	
	// Given a Customer, find the Customer's order file and return the order array //
	protected static String[][] getCustomerOrder(Customer customer, String[][] array) {
		try {
			String line;
			int i = 0;
			Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION
				+ customer.getCustomerFirstName() + customer.getCustomerLastName() + ".txt"));
			infile.nextLine();
			
			while (infile.hasNextLine()) {
				line = infile.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(", ");
				
				array[i][0] = parser.next();
				array[i][1] = parser.next();
				array[i][2] = parser.next();
				
				i++;
				parser.close();
			}
			adjustedOrderNumber = i;
			while (i < array.length) {
				array[i][0] = null;
				array[i][1] = null;
				array[i][2] = null;
				i++;
			}
			
			infile.close();
			return array;
		
		} catch (IOException e) {return array;}
	} // End of method
	
	
	
	// Add a Customer's first name and last name to customerData.txt //
	protected static void addCustomerData(Customer customer) {
		try {
			Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION + "customerData.txt"));
			int numLines = 0;
			
			while (infile.hasNextLine()) {
				infile.nextLine();
				numLines++;
			}
			infile = new Scanner(new FileReader(LoginInterface.LOCATION + "customerData.txt"));
			
			String[] info = new String[numLines + 1];
			for (int i = 0; i < numLines; i++) {
				info[i] = infile.nextLine();
			}
			
			info[numLines] = customer.getCustomerFirstName() + ", " + customer.getCustomerLastName();
			
			PrintWriter outfile = new PrintWriter(LoginInterface.LOCATION + "customerData.txt");
			for (int i = 0; i < numLines; i++) {
				outfile.println(info[i]);
			}
			outfile.print(info[numLines]);
			
			
			infile.close();
			outfile.close();
		} catch (IOException e) {}
	}
	
	// Get a Customer's first and last name from customerData.txt //
	protected static String getCustomerData() {
		String comp = "";
		try {
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION + "customerData.txt"));
			while (infile.hasNextLine()) {
				comp += infile.nextLine() + "\n";
			}
			
			infile.close();
		} catch (IOException e) {}
		
		return comp;
	}
	
	protected static void removeCustomerData(String fName, String lName) {
		
		try {
			
			Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION + "customerData.txt"));
			int numLines = 0;
			String line;
			
			while (infile.hasNextLine()) {
				infile.nextLine();
				numLines++;
			}
			
			infile.close();
			infile = new Scanner(new FileReader(LoginInterface.LOCATION + "customerData.txt"));
			
			String[][] custData = new String[numLines][2];
			for (int i = 0; i < numLines; i++) {
				
				line = infile.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(", ");
				
				try {
					custData[i][0] = parser.next();
					custData[i][1] = parser.next();
				} catch (NoSuchElementException e) {break;}
				
				if (custData[i][0].equals(fName) && custData[i][1].equals(lName)) {
					custData[i][0] = null;
					custData[i][1] = null;
					custDataFound = true;
				}
				
				parser.close();
			}
			
			PrintWriter outfile = new PrintWriter(LoginInterface.LOCATION + "customerData.txt");
			
			for (int i = 0; i < numLines; i++) {
				if (custData[i][0] != null) {
					outfile.println(custData[i][0] + ", " + custData[i][1]);
				}
			}
			
			outfile.close();
			infile.close();
		} catch (IOException e) {}
		
	} // End of method
	
} // End of class
