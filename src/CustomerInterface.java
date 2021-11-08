/*--------------------------------------------------------------------------------------------------------------------------------------------
// AUTHOR: Matthias Mitchell
// FILENAME: CustomerInterface.java
// SPECIFICATION: Creates and adds the components for the Customer's GUI. Also defines the Listeners related to shopping, saving, and
//				   removing orders. Frequently references LoginInterface.java variables and Customer.java methods.
// FOR: Shopping application
//------------------------------------------------------------------------------------------------------------------------------------------*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Random;

import javax.swing.*;

public class CustomerInterface extends Main {
	
	// BEGINNING of variable declarations
	private static JTextArea inventory, cart; // To display the shop and Customer order
	private static JScrollPane inventoryScroll, cartScroll; // Scroll bars for the TextAreas
	private static JTextField itemIn; // A field for Customer to specify an item to add to/remove from their order
	private static JButton addItem, removeItem, saveList, deleteList, exit; // Buttons to edit Customer's order
	private static JLabel feedback, cartFeedback, exitLabel; // Various labels for warnings or errors
	private static JButton completeOrder; // Button to complete Customer order
	
	final static String INVENTORY = "View Shop"; // Title for Customer shop tab
	final static String CART = "View Cart"; // Title for Customer cart tab
	final static String EXIT = "Exit Shop"; // Title for exit tab
	
	final static double TAX = 1.08; // Tax to apply to a saved Customer order
	
	protected static int orderNumber = 0; // To keep track of the position of the Customer's order array.
	
	protected static Random random = new Random(); // To create a randomized file name for completed Customer orders
	
	/* NOTE: orderNumber is modifiable in the event that Customer loads a previous file. In this case, it is replaced with 
	 * 		  adjustedOrderNumber, defined in Customer class.
	 */
	
	static NumberFormat currency = NumberFormat.getCurrencyInstance(); // To format Customer's displayed order
	static NumberFormat percent = NumberFormat.getPercentInstance(); // To format tax percentage applied in order
	// END of variable declarations
	
	
	public static void addCustomerComponents() {
		
	// Create the shop/inventory tab for Customer //
		JPanel shopTab = new JPanel();
		
		// BEGINNING of variable instantiations
		inventory = new JTextArea(12, 30);
		inventoryScroll = new JScrollPane(inventory, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		itemIn = new JTextField(12); addItem = new JButton("Add Item"); removeItem = new JButton("Remove Item");
		feedback = new JLabel("(Feedback)      ");
		saveList = new JButton("Save Order"); deleteList = new JButton("Delete Order");
		// END of variable instantiations
		
		inventory.setEditable(false);
		inventory.setText(Customer.readInventory());
		
		// BEGINNING of adding components
		shopTab.add(inventoryScroll);
		shopTab.add(itemIn);
		shopTab.add(addItem); shopTab.add(removeItem);
		shopTab.add(feedback);
		shopTab.add(saveList); shopTab.add(deleteList);
		// END of adding components
		
		addItem.addActionListener(new AddItem());
		removeItem.addActionListener(new RemoveItem());
		saveList.addActionListener(new SaveList());
		deleteList.addActionListener(new DeleteList());
		
	// Create the completed order tab for Customer //
		JPanel cartTab = new JPanel();
		
		// BEGINNING of variable instantiations
		cart = new JTextArea(12, 30);
		cartScroll = new JScrollPane(cart, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cartFeedback = new JLabel("Please save order to check out.");
		completeOrder = new JButton("Check Out");
		// END of variable instantiations
		
		cart.setEditable(false);
		
		// BEGINNING of adding components
		cartTab.add(cartScroll);
		cartTab.add(completeOrder);
		cartTab.add(cartFeedback);
		// END of adding components
		
		completeOrder.addActionListener(new CompleteOrder());
		completeOrder.setVisible(false);
		
		if (LoginInterface.alreadyLoaded == true) {
			displayOrder();
		}
		
	// Create the exit tab for Customer //
		JPanel exitTab = new JPanel();
		
		// BEGINNING of variable instantiations
		exitLabel = new JLabel("If not checked out, please save the current order to load it later.");
		exit = new JButton("Exit to Login Interface");
		// END of variable instantiations
		
		// BEGINNING of adding components
		exitTab.add(exitLabel);
		exitTab.add(exit);
		// END of adding components
		
		exit.addActionListener(new Exit());
		
		
	// Add tabs to pane //
		LoginInterface.allTabs.addTab(INVENTORY, shopTab);
		LoginInterface.allTabs.addTab(CART, cartTab);
		LoginInterface.allTabs.addTab(EXIT, exitTab);
		
	} // End of method
	
	
	
	
	private static class AddItem implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			if (Customer.inventoryItemExists(itemIn.getText())) {
				
				LoginInterface.customerOrder[orderNumber][0] = itemIn.getText();
				
				LoginInterface.customerOrder[orderNumber][1] = "1";
				
				LoginInterface.customerOrder[orderNumber][2] = Customer.getItemCost(itemIn.getText());
				
				String order = "";
				
				for (int i = 0; i < orderNumber; i++) { // Search array
					if (LoginInterface.customerOrder[i][0].equalsIgnoreCase(itemIn.getText())) {
						
						LoginInterface.customerOrder[orderNumber][0] = null;
						LoginInterface.customerOrder[orderNumber][1] = null;
						LoginInterface.customerOrder[orderNumber][2] = null;
						
						LoginInterface.customerOrder[i][1] = Integer.toString(Integer.valueOf(LoginInterface.customerOrder[i][1]) + 1);
						orderNumber--;
						
						LoginInterface.customerOrder[i][2] = 
							Double.toString(Integer.valueOf(LoginInterface.customerOrder[orderNumber][1]) * 
							Double.valueOf(Customer.getItemCost(itemIn.getText())));
						
						break;
					}
				}
				
				LoginInterface.customerOrder = Customer.sortAlphabetically(LoginInterface.customerOrder);
				
				for (int i = 0; i < LoginInterface.customerOrder.length; i++) { // Iterate through array
					if (LoginInterface.customerOrder[i][0] != null) {
						
						order += LoginInterface.customerOrder[i][1] + "\t";
						order += LoginInterface.customerOrder[i][0] + "\t\t";
						order += currency.format(Double.valueOf(LoginInterface.customerOrder[i][2])) + "\n";
						
					}
				}
				
				orderNumber++;
				
				completeOrder.setVisible(false);
				cartFeedback.setVisible(true);
				cart.setText(order);
				
				feedback.setText("(Feedback)      ");
			} else {
				feedback.setText("Item not found.      ");
			}
			
		} // End of method
	} // End of private class
	
	
	
	
	private static class RemoveItem implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			boolean itemFound = false;
			String order = "";
			
			for (int i = 0; i < orderNumber; i++) {
				if (LoginInterface.customerOrder[i][0].equalsIgnoreCase(itemIn.getText())) {
					
					itemFound = true;
					
					LoginInterface.customerOrder[i][1] = Integer.toString(Integer.valueOf(LoginInterface.customerOrder[i][1]) - 1);
					
					if (LoginInterface.customerOrder[i][1].equals("-1")) {
						
						LoginInterface.customerOrder[i][1] = "0";
						feedback.setText("Item at zero.      ");
					}
					
					LoginInterface.customerOrder[i][2] = 
						Double.toString(Integer.valueOf(LoginInterface.customerOrder[i][1]) * 
						Double.valueOf(Customer.getItemCost(itemIn.getText())));
					
					for (int j = 0; j < LoginInterface.customerOrder.length; j++) { // Search array
						if (LoginInterface.customerOrder[j][0] != null) {
							
							order += LoginInterface.customerOrder[j][1] + "\t";
							order += LoginInterface.customerOrder[j][0] + "\t\t";
							order += currency.format(Double.valueOf(LoginInterface.customerOrder[j][2])) + "\n";
							
						}
					}
					completeOrder.setVisible(false);
					cartFeedback.setVisible(true);
					cart.setText(order);
					break;
				}
			}
			if (itemFound == false) {
				feedback.setText("Item not found.      ");
			}
			
		} // End of method
	} // End of private class
	
	
	
	
	private static class SaveList implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
		// Write the current Customer order to the Customer's file //
			try {
				PrintWriter outfile = new PrintWriter(LoginInterface.LOCATION + 
					LoginInterface.customer.getCustomerFirstName() + 
					LoginInterface.customer.getCustomerLastName() + ".txt");
				
				outfile.print(Customer.customerToString(LoginInterface.customer));
				
				for (int i = 0; i < LoginInterface.customerOrder.length; i++) {
					
					if (LoginInterface.customerOrder[i][0] == null) {
						break;
					}
					
					outfile.print("\n"
						+ LoginInterface.customerOrder[i][0] + ", "
						+ LoginInterface.customerOrder[i][1] + ", "
						+ LoginInterface.customerOrder[i][2]);
				}
				
				outfile.close();
			} catch (IOException e) {}
			
		// Reformat and display Customer order, along with subtotal and total payments due //
			displayOrder();
			
		} // End of method
	} // End of private class
	
	
	
	
	private static class DeleteList implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			for (int i = 0; i < LoginInterface.customerOrder.length; i++) {
				
				LoginInterface.customerOrder[i][0] = null;
				LoginInterface.customerOrder[i][1] = null;
				LoginInterface.customerOrder[i][2] = null;
			}
			
			orderNumber = 0;
			
			// Reset Customer file
			try {
				Customer.write(LoginInterface.LOCATION + LoginInterface.customer.getCustomerFirstName() +
					LoginInterface.customer.getCustomerLastName() + ".txt", Customer.customerToString(LoginInterface.customer));
			} catch (IOException e) {}
			
			cart.setText("Order successfully deleted.");
			completeOrder.setVisible(false);
			cartFeedback.setVisible(true);
			
		} // End of method
	} // End of private class
	
	
	
	
	private static class CompleteOrder implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			int[] compareToInventory = new int[orderNumber];
			String[] orderItems = new String[orderNumber];
			int compFileName;
			
			for (int i = 0; i < orderNumber; i++) {
				
				compareToInventory[i] = -Integer.valueOf(LoginInterface.customerOrder[i][1]);
				orderItems[i] = LoginInterface.customerOrder[i][0];
				
			}
			
			Customer.addInventoryItems(orderItems, compareToInventory);
			if (Customer.outOfStock == true) {
				cart.setText("One or more items were over ordered. Please compare order to inventory.");
				completeOrder.setVisible(false);
				cartFeedback.setVisible(true);
			} else {
				inventory.setText(Customer.readInventory());
				
				compFileName = 100000 + random.nextInt(899999);
				new File(LoginInterface.LOCATION + compFileName + ".txt");
				
				try {
					
					PrintWriter outfile = new PrintWriter(LoginInterface.LOCATION + compFileName + ".txt");
					
					for (int i = 0; i < orderNumber; i++) {
						
						outfile.println(
							LoginInterface.customerOrder[i][0] + ", "
							+ LoginInterface.customerOrder[i][1] + ", "
							+ LoginInterface.customerOrder[i][2]
						);
					}
					
					new File(LoginInterface.LOCATION + LoginInterface.customer.getCustomerFirstName()
						+ LoginInterface.customer.getCustomerLastName() + ".txt").delete();
					
					outfile.close();
				} catch (IOException e) {}
				
				Customer.createCustomerFile(LoginInterface.customer);
				cart.setText("Order placed. Thank you.");
			}
		} // End of method
	} // End of private class
	
	
	
	
	private static class Exit implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
			LoginInterface.allTabs.setVisible(false);
			Main.login.addComponents(Main.frame.getContentPane());
			
		}
	}
	
	
	
	
	protected static void displayOrder() {
		
		String compOrder = "Customer: ";
		double subtotal = 0, discount = 0;
		compOrder += LoginInterface.customer.getCustomerFirstName() + " "
			+ LoginInterface.customer.getCustomerLastName() + "\n"
			+ LoginInterface.customer.getCustomerAddress() + "\n"
			+ LoginInterface.customer.getCustomerCity() + ", "
			+ LoginInterface.customer.getCustomerState() + " "
			+ LoginInterface.customer.getCustomerZip() + "\n\n"
			+ "Items to be purchased: \n";
		
		for (int i = 0; i < LoginInterface.customerOrder.length; i++) {
			
			if (LoginInterface.customerOrder[i][0] == null) {
				break;
			}
			
			compOrder += LoginInterface.customerOrder[i][1] + "\t";
			compOrder += LoginInterface.customerOrder[i][0] + "\t\t";
			compOrder += currency.format(Double.valueOf(LoginInterface.customerOrder[i][2])) + "\n";
			
			subtotal += Double.valueOf(LoginInterface.customerOrder[i][2]);
			
		}
		
		compOrder += "____________________________________________" + "\nSubtotal:\t\t\t"
			+ currency.format(subtotal) + "\nDiscounts:\t\t\t" + currency.format(discount)
			+ "\nTax:\t\t\t" + percent.format(TAX - 1) + "\n"
			+ "____________________________________________" + "\nTotal:\t\t\t"
			+ currency.format((subtotal - discount)*TAX);
		
		cart.setText(compOrder);
		cartFeedback.setVisible(false);
		completeOrder.setVisible(true);
	} // End of method
} // End of class
