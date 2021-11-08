/*--------------------------------------------------------------------------------------------------------------------------------------------
// AUTHOR: Matthias Mitchell
// FILENAME: LoginInterface.java
// SPECIFICATION: Creates and adds the components for the GUI starter screen as well as defining some central variables such as numInvLines
//				   that are frequently referenced and changed in other files. Also defines Listeners used to create a Customer account or
//				   verify Employee/Manager information for login.
// FOR: Shopping application
//------------------------------------------------------------------------------------------------------------------------------------------*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;

public class LoginInterface extends Main {
	
	// BEGINNING of variable declarations
	protected static JLabel welcome, fNameOut, lNameOut, addressOut, cityOut, stateOut, zipOut; // For CUSTOMER (to label fields)
	protected static JTextField fNameIn, lNameIn, addressIn, cityIn, stateIn, zipIn; // For CUSTOMER (fields to take info)
	private JLabel empIDOut, empLoginOut; // For EMPLOYEE (to label fields)
	private JTextField empIDIn, empLoginIn; // For EMPLOYEE (fields to take login)
	private JLabel mgrIDOut, mgrLoginOut; // For MANAGER (to label fields)
	private JTextField mgrIDIn, mgrLoginIn; // For MANAGER (fields to take login)
	private JButton doneC, doneE, doneM; // Buttons to login/create accounts
	private JButton overwrite, load; // Buttons to allow customer to overwrite an identical file
	private JLabel infoC, infoE, infoM; // Labels to alert the user of any issues with the info
	protected static JTabbedPane allTabs; // Pane, acts as a universal container for all tabs in all interfaces
	
	final public static String LOCATION = "src/userData/"; // Constant string for the location of workerData.txt and all Customer files
	final public static String INV_LOCATION = "src/inventory.txt"; // Constant string for the location of the inventory
	final static String WELCOME = "(None)"; // Title for a welcome tab
	final static String CUSTOMER = "Customer"; // Title for a Customer account creation tab
	final static String EMPLOYEE = "Employee"; // Title for an employee login tab
	final static String MANAGER = "Manager"; // Title for a manager login tab
	
	final static int NUM_TABS = 4; // A constant to reference for removing the tabs to change the GUI for Customer/Employee/Manager
	
	public static Customer customer; // A public Customer object referenced in several files to modify
	public static boolean alreadyLoaded = false; // To indicate if a Customer already exists
	
	public static int numInvLines = 0; // A public integer that is referenced and adjusted to represent the number of lines in inventory.txt
	public static String[][] customerOrder; // A public array that holds a Customer's order
	// END of variable declarations
	
	
	
	public void addComponents(Container holder) {
		setBackground(Color.WHITE);
		
		allTabs = new JTabbedPane();
		
//--------------------------------------------------------------------------------------------------------------------------------------------
// Create the various login tabs to ultimately combine under the allTabs pane in a card-layout style.
//--------------------------------------------------------------------------------------------------------------------------------------------
		
		JPanel welcomeTab = new JPanel();
		
	// Create the welcome tab (flow-layout) //
		welcome = new JLabel("Welcome! Please select a tab to login.");
		welcomeTab.add(welcome);
		
		
		
	// Create the customer login tab (grid-layout) //
		JPanel customerTab = new JPanel();
		customerTab.setLayout(new GridLayout(8, 2, 0, 4));
		
		// BEGINNING of variable instantiations
		fNameOut = new JLabel("First Name:"); lNameOut = new JLabel("Last Name:"); addressOut = new JLabel("Street Address:");
		 cityOut = new JLabel("City:"); stateOut = new JLabel("State:"); zipOut = new JLabel("ZIP Code:");
		fNameIn = new JTextField(); lNameIn = new JTextField(); addressIn = new JTextField(); cityIn = new JTextField();
		 stateIn = new JTextField(); zipIn = new JTextField();
		doneC = new JButton("Create Account"); infoC = new JLabel("      (Feedback)");
		overwrite = new JButton(); load = new JButton();
		// END of variable instantiations
		
		// BEGINNING of adding components
		customerTab.add(fNameOut); customerTab.add(fNameIn); customerTab.add(lNameOut); customerTab.add(lNameIn);
		 customerTab.add(addressOut); customerTab.add(addressIn); customerTab.add(cityOut); customerTab.add(cityIn);
		 customerTab.add(stateOut); customerTab.add(stateIn); customerTab.add(zipOut); customerTab.add(zipIn);
		 customerTab.add(doneC); customerTab.add(infoC); customerTab.add(overwrite); customerTab.add(load);
		// END of adding components
		
		doneC.addActionListener(new CreateAccount());
		
		overwrite.addActionListener(new OverwriteC()); load.addActionListener(new LoadC());
		overwrite.setVisible(false); load.setVisible(false);
		
		
		
	// Create the employee login tab (grid-layout) //
		JPanel employeeTab = new JPanel();
		employeeTab.setLayout(new GridLayout(3, 2, 0, 90));
		
		// BEGINNING of variable instantiations
		empIDOut = new JLabel("ID Number:");
		 empLoginOut = new JLabel("Login ID:");
		empIDIn = new JTextField(); empLoginIn = new JTextField();
		doneE = new JButton("Login"); infoE = new JLabel("      (Feedback)");
		// END of variable instantiations
		
		// BEGINNING of adding components
		employeeTab.add(empIDOut); employeeTab.add(empIDIn); employeeTab.add(empLoginOut); employeeTab.add(empLoginIn);
		 employeeTab.add(doneE); employeeTab.add(infoE);
		// END of adding components
		
		doneE.addActionListener(new EmployeeLogin());
		
		
		
	// Create the manager login tab (grid-layout) //
		JPanel managerTab = new JPanel();
		managerTab.setLayout(new GridLayout(3, 2, 0, 90));
		
		// BEGINNING of variable instantiations
		mgrIDOut = new JLabel("ID Number:");
		 mgrLoginOut = new JLabel("Login ID:");
		mgrIDIn = new JTextField(); mgrLoginIn = new JTextField();
		doneM = new JButton("Login"); infoM = new JLabel("      (Feedback)");
		// END of variable instantiations
		
		// BEGINNING of adding components
		managerTab.add(mgrIDOut); managerTab.add(mgrIDIn); managerTab.add(mgrLoginOut); managerTab.add(mgrLoginIn);
		 managerTab.add(doneM); managerTab.add(infoM);
		// END of adding components
		 
		 doneM.addActionListener(new ManagerLogin());
		
		
		
	// Add components to pane //
		allTabs.addTab(WELCOME, welcomeTab);
		allTabs.addTab(CUSTOMER, customerTab);
		allTabs.addTab(EMPLOYEE, employeeTab);
		allTabs.addTab(MANAGER, managerTab);
		
	// Add pane to main container //
		holder.add(allTabs);
		
	} // End of addComponents
	
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------
// Define the ButtonListeners for Customer's "Create Account" and Employee and Manager's "Login"
//--------------------------------------------------------------------------------------------------------------------------------------------
	
	// ButtonListener for Customer account creation //
	private class CreateAccount implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			customer = new Customer(
				fNameIn.getText(), lNameIn.getText(), addressIn.getText(), cityIn.getText(), stateIn.getText(), zipIn.getText()
			); // End of instantiation
			numInvLines = 0;
			// Reset the registered number of lines in the inventory and recalculate
			try {
				Scanner counter = new Scanner(new FileReader(INV_LOCATION));
				
				while (counter.hasNextLine()) {
					counter.nextLine();
					numInvLines++;
				}
				
				counter.close();
			} catch (IOException e) {}
			
			// Instantiate a new Customer order
			customerOrder = new String[numInvLines][3];
			
			if ( Customer.similarCustomerExists(customer) == false ) { // If no previous order exists in this name
				Customer.createCustomerFile(customer);
				Customer.addCustomerData(customer);
				
				try {
					// Create and write the first line to the Customer's file
					Customer.write(LOCATION + customer.getCustomerFirstName() + customer.getCustomerLastName() + ".txt", 
							Customer.customerToString(customer));
				} catch (IOException e) {}
				
				// Remove the login tabs and add the Customer GUI
				for (int i = 0; i < NUM_TABS; i++) {
					allTabs.removeTabAt(0);
				}
				
				CustomerInterface.addCustomerComponents();
				
			} else { // If previous order exists in this name
				infoC.setText("      Similar customer already exists.");
				doneC.setVisible(false);
				
				// Allow user to choose between loading the old order, or overwriting the old order
				overwrite.setVisible(true); load.setVisible(true);
				overwrite.setText("Overwrite Previous File"); load.setText("Load Previous File");
			}
		} // End of method

	} // End of private class
	
	
	// ButtonListener to overwrite an identical Customer file //
	private class OverwriteC implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			try {
				PrintWriter outfile = new PrintWriter(LOCATION + customer.getCustomerFirstName()
					+ customer.getCustomerLastName() + ".txt");
				
				// Rewrite the first line of the Customer file
				outfile.println(Customer.customerToString(customer));
				
				outfile.close();
			} catch (IOException e) {}
			
			// Remove the login tabs and add Customer GUI
			for (int i = 0; i < NUM_TABS; i++) {
				allTabs.removeTabAt(0);
			}
			
			CustomerInterface.addCustomerComponents();
			
		} // End of method
		
	} // End of private class
	
	
	// ButtonListener to load a Customer file //
	private class LoadC implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			customerOrder = Customer.getCustomerOrder(customer, customerOrder);
			CustomerInterface.orderNumber = Customer.adjustedOrderNumber;
			alreadyLoaded = true; // Referenced in Customer.java to display the loaded order
			
			// Remove the login tabs and add Customer GUI
			for (int i = 0; i < NUM_TABS; i++) {
				allTabs.removeTabAt(0);
			}
			
			CustomerInterface.addCustomerComponents();
			
		} // End of method
		
	} // End of private class
	
	
	
	private class EmployeeLogin implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			if (Employee.employeeExists(empIDIn.getText(), empLoginIn.getText())) {
				// If Employee info supplied is accurate, remove the login tabs and add the Manager GUI components
				for (int i = 0; i < NUM_TABS; i++) {
					allTabs.removeTabAt(0);
				}
				
				EmployeeInterface.addEmployeeComponents();
				
			} else { // If info is not recognized
				infoE.setText("      Information not recognized.");
			}
			
		} // End of method
		
	} // End of private class
	
	
	private class ManagerLogin implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			if (Manager.managerExists(mgrIDIn.getText(), mgrLoginIn.getText())) {
				
				// If Manager info supplied is accurate, remove the login tabs and add the Manager GUI components
				for (int i = 0; i < NUM_TABS; i++) {
					allTabs.removeTabAt(0);
				}
				
				ManagerInterface.addManagerComponents();
				
			} else { // If info is not recognized
				infoM.setText("      Information not recognized");
			}
		} // End of method
	} // End of private class
	
	
} // End of class
