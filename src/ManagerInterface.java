/*--------------------------------------------------------------------------------------------------------------------------------------------
// AUTHOR: Matthias Mitchell
// FILENAME: Main.java
// SPECIFICATION: Creates and adds the components for Manager's GUI. Also defines all the listeners attached to JButtons. Frequently 
//				   references Manager.java methods.
// FOR: Shopping application
//------------------------------------------------------------------------------------------------------------------------------------------*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;

public class ManagerInterface extends EmployeeInterface {
	
	// BEGINNING of variable declarations
	private static JButton addItem, removeItem, deleteItem; // To modify item information in the inventory
	private static JTextField modifyItem; // To take in user request on which inventory item to modify
	private static JButton newItem; // To create add a new item to the inventory
	private static JTextField newName, newAmount, newCost; // To take in user info on new inventory item's properties
	private static JLabel feedback, nameLabel, amountLabel, costLabel; // Labels to assist
	
	private static JTextArea empList; // To list all Employees
	private static JScrollPane empListScroll; // Scroll bar for Employee list
	
	private static JTextField empIDIn, infoToChange; // To take in user-supplied info
	private static JButton removeEmp, changeID, changeFName, changeLName, changeLogin; // To modify/remove Employee
	private static JLabel requestID, empty3, requestInfo; 
	private static JLabel empty1, empty2; // To create empty space on a grid-layout
	
	private static JLabel newEmpInfo; // To assist user
	private static JTextField empNameIn; // To take in new Employee's full name (Format: "First, Last")
	private static JButton createEmp; // To create new Employee
	
	final static String MODIFY_INV = "Modify Inventory";
	final static String DISPLAY_EMP = "View Employees";
	final static String MODIFY_EMP = "Modify Employee";
	final static String NEW_EMP = "New Employee";
	// END of variable declarations
	
	
	protected static void addManagerComponents() {
		
		addEmployeeComponents(); // Add all Employee tabs and components, as well as their ActionListeners
		
	// Create the inventory modification tab (grid-layout) //
		JPanel modifyInvTab = new JPanel();
		modifyInvTab.setLayout(new GridLayout(3, 4, 0, 75)); // Large vertical gap for thinner components
		
		// BEGINNING of variable instantiations
		modifyItem = new JTextField();
		addItem = new JButton("Add 1"); removeItem = new JButton("Remove 1"); deleteItem = new JButton("Delete Item");
		newItem = new JButton("Create Item");
		newName = new JTextField(); newAmount = new JTextField(); newCost = new JTextField();
		feedback = new JLabel("");
		nameLabel = new JLabel("   ^ Item Name ^");
		amountLabel = new JLabel(" ^ Item Amount ^");
		costLabel = new JLabel("   ^ Item Cost ^");
		// END of variable instantiations
		
		// BEGINNING of adding components
		modifyInvTab.add(modifyItem);
		modifyInvTab.add(addItem); modifyInvTab.add(removeItem); modifyInvTab.add(deleteItem);
		modifyInvTab.add(newItem);
		modifyInvTab.add(newName); modifyInvTab.add(newAmount); modifyInvTab.add(newCost);
		modifyInvTab.add(feedback);
		modifyInvTab.add(nameLabel); modifyInvTab.add(amountLabel); modifyInvTab.add(costLabel);
		// END of adding components
		
		addItem.addActionListener(new AddItem());
		removeItem.addActionListener(new RemoveItem());
		deleteItem.addActionListener(new DeleteItem());
		newItem.addActionListener(new NewItem());
		
	// Create the Employee display tab //
		JPanel displayEmpTab = new JPanel();
		
		// BEGINNING of variable instantiations
		empList = new JTextArea(12, 30);
		empListScroll = new JScrollPane(empList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// END of variable instantiations
		
		empList.setEditable(false);
		empList.setText(Manager.readEmployees());
		
		// BEGINNING of adding components
		displayEmpTab.add(empListScroll);
		// END of adding components
		
	// Create the remove/modify Employee tab (grid-layout) //
		JPanel modifyEmpTab = new JPanel();
		modifyEmpTab.setLayout(new GridLayout(6, 2, 0, 5));
		
		// BEGINNING of variable instantiations
		requestID = new JLabel("ID# of Employee to modify:"); empIDIn = new JTextField();
		removeEmp = new JButton("Remove Employee"); empty3 = new JLabel();
		empty1 = new JLabel(); empty2 = new JLabel();
		requestInfo = new JLabel("Change Employee info to:"); infoToChange = new JTextField();
		changeID = new JButton("Change ID#"); changeLogin = new JButton("Change Login ID");
		changeFName = new JButton("Change First Name"); changeLName = new JButton("Change Last Name");
		// END of variable instantiations
		
		// BEGINNING of adding components
		modifyEmpTab.add(requestID); modifyEmpTab.add(empIDIn);
		modifyEmpTab.add(removeEmp); modifyEmpTab.add(empty3);
		modifyEmpTab.add(empty1); modifyEmpTab.add(empty2);
		modifyEmpTab.add(requestInfo); modifyEmpTab.add(infoToChange);
		modifyEmpTab.add(changeID); modifyEmpTab.add(changeLogin);
		modifyEmpTab.add(changeFName); modifyEmpTab.add(changeLName);
		// END of adding components
		
		changeID.addActionListener(new ChangeEmpID());
		changeLogin.addActionListener(new ChangeEmpLogin());
		changeFName.addActionListener(new ChangeEmpFName());
		changeLName.addActionListener(new ChangeEmpLName());
		removeEmp.addActionListener(new RemoveEmp());
		
	// Create the new Employee tab //
		JPanel newEmpTab = new JPanel();
		
		// BEGINNING of variable instantiations
		newEmpInfo = new JLabel("Give Employee's full name in the format \"First, Last\"");
		empNameIn = new JTextField(12);
		createEmp = new JButton("Create Employee");
		// END of variable instantiations
		
		// BEGINNING of adding components
		newEmpTab.add(newEmpInfo);
		newEmpTab.add(empNameIn);
		newEmpTab.add(createEmp);
		// END of adding components
		
		createEmp.addActionListener(new CreateEmployee());
		
		
		
		LoginInterface.allTabs.add(MODIFY_INV, modifyInvTab);
		LoginInterface.allTabs.add(DISPLAY_EMP, displayEmpTab);
		LoginInterface.allTabs.add(MODIFY_EMP, modifyEmpTab);
		LoginInterface.allTabs.add(NEW_EMP, newEmpTab);
		
	} // End of method
	
	
	
	
	private static class AddItem implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			String[] item = {modifyItem.getText()};
			int[] amount = {1};
			
			Customer.addInventoryItems(item, amount);
			EmployeeInterface.inventory.setText(Customer.readInventory());
		} // End of method
	} // End of private class
	
	private static class RemoveItem implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			String[] item = {modifyItem.getText()};
			int[] amount = {-1};
			
			Customer.addInventoryItems(item, amount);
			EmployeeInterface.inventory.setText(Customer.readInventory());
			
		} // End of method
	} // End of private class
	
	private static class DeleteItem implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			String item = modifyItem.getText();
			Manager.deleteItem(item);
			EmployeeInterface.inventory.setText(Customer.readInventory());
			
		} // End of method
	} // End of private class
	
	private static class NewItem implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			String name = newName.getText();
			String amount = newAmount.getText();
			String cost = newCost.getText();
			Manager.newItem(name, amount, cost);
			EmployeeInterface.inventory.setText(Customer.readInventory());
			
		} // End of method
	} // End of private class
	
	private static class ChangeEmpID implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			Manager.changeEmployeeInfoAt(0, infoToChange.getText(), empIDIn.getText());
			empList.setText(Manager.readEmployees());
			
		} // End of method
	} // End of private class
	
	private static class ChangeEmpLogin implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			Manager.changeEmployeeInfoAt(4, infoToChange.getText(), empIDIn.getText());
			empList.setText(Manager.readEmployees());
			
		} // End of method
	} // End of private class
	
	private static class ChangeEmpFName implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			Manager.changeEmployeeInfoAt(1, infoToChange.getText(), empIDIn.getText());
			empList.setText(Manager.readEmployees());
			
		} // End of method
	} // End of private class
	
	private static class ChangeEmpLName implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			Manager.changeEmployeeInfoAt(2, infoToChange.getText(), empIDIn.getText());
			empList.setText(Manager.readEmployees());
			
		} // End of method
	} // End of private class
	
	private static class CreateEmployee implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			Random rand = new Random();
			String comp = "";
			String emp = empNameIn.getText();
			Scanner parser = new Scanner(emp);
			parser.useDelimiter(", ");
			
			String fn = parser.next();
			String ln = parser.next();
			String login = fn.substring(0, 1).toLowerCase() + ln.substring(0, 4).toLowerCase() + (100 + rand.nextInt(899));
			
			parser.close();
			
			try {
				
				Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION + "workerData.txt"));
				
				while (infile.hasNextLine()) {
					comp += infile.nextLine() + "\n";
				}
				comp += (100000 + rand.nextInt(899999)) + ", " + fn + ", " + ln + ", " + "Employee, " + login;
				
				PrintWriter outfile = new PrintWriter(LoginInterface.LOCATION + "workerData.txt");
				
				outfile.print(comp);
				
				outfile.close();
				infile.close();
			} catch (IOException e) {}
			
			empList.setText(Manager.readEmployees());
			
		} // End of method
	} // End of private class
	
	private static class RemoveEmp implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			String line;
			String comp = "";
			String[] array = new String[5];
			boolean empty = false;
			
			try {
				
				Scanner infile = new Scanner(new FileReader(LoginInterface.LOCATION + "workerData.txt"));
				while (infile.hasNextLine()) {
					line = infile.nextLine();
					Scanner parser = new Scanner(line);
					parser.useDelimiter(", ");
					
					for (int i = 0; i < array.length; i++) {
						array[i] = parser.next();
						
						if (array[0].equals(empIDIn.getText()) == false) {
							comp += array[i];
							
							if (i < array.length - 1)
								comp += ", ";
						} else {
							empty = true;
						}
					}
					
					if (infile.hasNextLine() && empty == false) {
						comp += "\n";
					}
					
					parser.close();
				}
				
				PrintWriter outfile = new PrintWriter(LoginInterface.LOCATION + "workerData.txt");
				
				outfile.print(comp);
				
				outfile.close();
				infile.close();
			} catch (IOException e) {}
			
			empList.setText(Manager.readEmployees());
			
		} // End of method
	} // End of class
	
} // End of class
