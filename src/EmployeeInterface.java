/*--------------------------------------------------------------------------------------------------------------------------------------------
// AUTHOR: Matthias Mitchell
// FILENAME: EmployeeInterface.java
// SPECIFICATION: Creates and adds the components for the Employee's GUI. Also defines all the Listeners attached to JButtons. Frequently
//				   references Customer.java and LoginInterface.java methods and variables.
// FOR: Shopping application
//------------------------------------------------------------------------------------------------------------------------------------------*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Scanner;

import javax.swing.*;

public class EmployeeInterface extends LoginInterface {
	
	// BEGINNING of variable declarations
	protected static JButton view, back, checkout, remove;
	protected static JTextField customerReview, customerEdit;
	protected static JButton createCstmr;
	protected static JLabel feedback, feedbackModify;
	protected static JLabel reviewLabel, modifyLabel;
	protected static JTextArea orders, inventory;
	protected static JScrollPane ordersScroll, inventoryScroll;
	
	final static String REVIEW = "Review Orders";
	final static String MODIFY = "Modify Order";
	final static String CUSTOMER = "New Customer";
	final static String INVENTORY = "View Inventory";
	
	static NumberFormat currency = NumberFormat.getCurrencyInstance();
	// END of variable declarations
	
	
	public static void addEmployeeComponents() {
		
	// Create the Customer review tab //
		JPanel reviewTab = new JPanel();
		
		// BEGINNING of variable instantiations
		orders = new JTextArea(12, 30);
		ordersScroll = new JScrollPane(orders, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		reviewLabel = new JLabel("To view an order, type a name in the format \"First, Last\"\n and click \"View.\"");
		customerReview = new JTextField(12);
		view = new JButton("View Order"); back = new JButton("Back");
		// END of variable instantiations
		
		orders.setEditable(false);
		orders.setText(Customer.getCustomerData());
		
		// BEGINNING of adding components
		reviewTab.add(reviewLabel);
		reviewTab.add(ordersScroll);
		reviewTab.add(customerReview);
		reviewTab.add(view); reviewTab.add(back);
		// END of adding components
		
		back.setVisible(false);
		
		view.addActionListener(new ViewOrder());
		back.addActionListener(new BackOrder());
		
		
	// Create the Customer order-modification tab //
		JPanel modifyTab = new JPanel();
		
		// BEGINNING of variable instantiations
		modifyLabel = new JLabel("To choose an order to modify, type a name in the format \"First, Last.\"");
		customerEdit = new JTextField(12);
		checkout = new JButton("Complete Order"); remove = new JButton("Remove Order");
		feedbackModify = new JLabel("      (Feedback)      ");
		// END of variable instantiations
		
		// BEGINNING of adding components
		modifyTab.add(modifyLabel);
		modifyTab.add(customerEdit);
		modifyTab.add(checkout); modifyTab.add(remove);
		modifyTab.add(feedbackModify);
		// END of adding components
		
		checkout.addActionListener(new CompleteOrder());
		remove.addActionListener(new RemoveOrder());
		
		
	// Create the Customer creation tab (grid-layout) //
		JPanel customerTab = new JPanel();
		
		customerTab.setLayout(new GridLayout(7, 2, 0, 4));
		
		// BEGINNING of variable instantiations
		createCstmr = new JButton("Create Customer Account");
		feedback = new JLabel("      (Feedback)");
		// END of variable instantiations
		
		// BEGINNING of adding components
		customerTab.add(LoginInterface.fNameOut); customerTab.add(LoginInterface.fNameIn);
		customerTab.add(LoginInterface.lNameOut); customerTab.add(LoginInterface.lNameIn);
		customerTab.add(LoginInterface.addressOut); customerTab.add(LoginInterface.addressIn);
		customerTab.add(LoginInterface.cityOut); customerTab.add(LoginInterface.cityIn);
		customerTab.add(LoginInterface.stateOut); customerTab.add(LoginInterface.stateIn);
		customerTab.add(LoginInterface.zipOut); customerTab.add(LoginInterface.zipIn);
		customerTab.add(createCstmr); customerTab.add(feedback);
		// END of adding components
		
		createCstmr.addActionListener(new CreateCstmrAccount());
		
	// Create the inventory review tab //
		JPanel inventoryTab = new JPanel();
		
		// BEGINNING of variable instantiations
		inventory = new JTextArea(12, 30);
		inventoryScroll = new JScrollPane(inventory, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// END of variable instantiations
		
		inventory.setEditable(false);
		inventory.setText(Customer.readInventory());
		
		// BEGINNING of adding components
		inventoryTab.add(inventoryScroll);
		// END of adding components
		
		
	// Add tabs to pane //
		LoginInterface.allTabs.addTab(REVIEW, reviewTab);
		LoginInterface.allTabs.addTab(MODIFY, modifyTab);
		LoginInterface.allTabs.addTab(CUSTOMER, customerTab);
		LoginInterface.allTabs.addTab(INVENTORY, inventoryTab);
		
		
	} // End of method
	
	
	
	// Access a Customer's order file and display the contents //
	private static class ViewOrder implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			String n1, n2, comp = "";
			Scanner parser = new Scanner(customerReview.getText());
			parser.useDelimiter(", ");
			n1 = parser.next(); n2 = parser.next();
			
			
			try {
				numInvLines = 0;
				Scanner counter = new Scanner(new FileReader(INV_LOCATION));
				
				while (counter.hasNextLine()) {
					counter.nextLine();
					numInvLines++;
				}
				
				counter.close();
			} catch (IOException e) {}
			
			String[][] order = new String[numInvLines][3];
			
			
			parser.close();
			Customer temp = new Customer(n1, n2, null, null, null, null);
			if (Customer.similarCustomerExists(temp) == false) {
				
				orders.setText("Customer not found. Please go back and try again.");
			} else {
				
				Customer.getCustomerOrder(temp, order);
				for (int i = 0; i < order.length; i++) {
					if (order[i][0] != null) {
						comp += order[i][0] + "\t\t" + order[i][1] + "\t" + currency.format(Double.valueOf(order[i][2])) + "\n";
					}
				}
				orders.setText(comp);
			}
			back.setVisible(true);
			view.setVisible(false);
			
		} // End of method
	} // End of private class
	
	
	// Exit out of viewing a Customer's order to listing all Customers //
	private static class BackOrder implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			orders.setText(Customer.getCustomerData());
			
			back.setVisible(false);
			view.setVisible(true);
			
		} // End of method
	} // End of private class
	
	
	// Force-complete a Customer's order //
	private static class CompleteOrder implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			try {
				
				Scanner counter = new Scanner(new FileReader(INV_LOCATION));
				numInvLines = 0; // Recalculate the number of lines in inventory.txt
				while (counter.hasNextLine()) {
					counter.nextLine();
					numInvLines++;
				}
				counter.close();
				
				Scanner parser = new Scanner(customerEdit.getText());
				parser.useDelimiter(", ");
				String n1 = parser.next();
				String n2 = parser.next();
				parser.close();
				
				Scanner infile = new Scanner(new FileReader(LOCATION + n1 + n2 + ".txt"));
				
				int orderNumber = 0;
				String line;
				
				infile.nextLine();
				while (infile.hasNextLine()) {
					infile.nextLine();
					orderNumber++;
				}
				infile.close();
				
				infile = new Scanner(new FileReader(LOCATION + n1 + n2 + ".txt"));
				infile.nextLine();
				
				int[] compareToInventory = new int[orderNumber];
				String[] orderItems = new String[orderNumber];
				String[] prices = new String[orderNumber];
				
				for (int i = 0; i < orderNumber; i++) {
					line = infile.nextLine();
					parser = new Scanner(line);
					parser.useDelimiter(", ");
					
					orderItems[i] = parser.next();
					compareToInventory[i] = -Integer.valueOf(parser.next());
					prices[i] = parser.next();
					
					parser.close();
				}
				
				Customer.addInventoryItems(orderItems, compareToInventory);
				if (Customer.outOfStock == true) {
					
					feedbackModify.setText("      Customer has over ordered. Cannot complete.      ");
				} else {
					
					int compFileName = 100000 + CustomerInterface.random.nextInt(899999);
					new File(LOCATION + compFileName + ".txt");
					
					PrintWriter outfile = new PrintWriter(LOCATION + compFileName + ".txt");
					
					for (int i = 0; i < orderNumber; i++) {
						outfile.println(orderItems[i] + ", " + -compareToInventory[i] + ", " + prices[i]);
					}
					
					outfile.close();
				}
				
				parser.close();
				infile.close();
				
				orders.setText(Customer.getCustomerData());
				inventory.setText(Customer.readInventory());
				
				if (new File(LOCATION + n1 + n2 + ".txt").delete()) {
					feedbackModify.setText("      Successfully completed order.      ");
					Customer.removeCustomerData(n1, n2);
					orders.setText(Customer.getCustomerData());
				}
				
			} catch (IOException e) {e.printStackTrace();}
			
		} // End of method
	} // End of private class
	
	
	private static class RemoveOrder implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			String line = customerEdit.getText();
			Scanner parser = new Scanner(line);
			parser.useDelimiter(", ");
			String n1 = parser.next();
			String n2 = parser.next();
			parser.close();
			
			Customer.removeCustomerData(n1, n2);
			
			if (Customer.custDataFound == false) {
				feedbackModify.setText("      Customer not found.      ");
			} else {
				if (new File(LOCATION + n1 + n2 + ".txt").delete()) {
					feedbackModify.setText("      Successfully removed order.      ");
					orders.setText(Customer.getCustomerData());
				}
			}
			
		} // End of method
	} // End of private class
	
	
	private static class CreateCstmrAccount implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			Customer newCstmr = new Customer( 
				fNameIn.getText(), lNameIn.getText(), addressIn.getText(), cityIn.getText(), stateIn.getText(), zipIn.getText() 
			);
			numInvLines = 0;
			
			try {
				Scanner counter = new Scanner(new FileReader(INV_LOCATION));
				
				while (counter.hasNextLine()) {
					counter.nextLine();
					numInvLines++;
				}
				
				counter.close();
			} catch (IOException e) {}
			
			if ( Customer.similarCustomerExists(newCstmr) == false ) {
				Customer.createCustomerFile(newCstmr);
				Customer.addCustomerData(newCstmr);
				
				try {
					Customer.write(LOCATION + newCstmr.getCustomerFirstName() + newCstmr.getCustomerLastName() + ".txt", 
							Customer.customerToString(newCstmr));
				} catch (IOException e) {}
				
				feedback.setText("      Successfully created Customer");
				orders.setText(Customer.getCustomerData());
				
				
			} else {
				feedback.setText("      Similar customer already exists.");
			}
			
		} // End of method
	} // End of private class
	
	
} // End of class
