/*--------------------------------------------------------------------------------------------------------------------------------------------
// AUTHOR: Matthias Mitchell
// FILENAME: Main.java
// SPECIFICATION: Driver file.
// FOR: Shopping application
//------------------------------------------------------------------------------------------------------------------------------------------*/

import java.awt.*;
import javax.swing.*;

public class Main extends JPanel {
	
	static JFrame frame;
	static LoginInterface login;
	
	public static void main(String[] args) {
		
		frame = new JFrame("Final Project");
		frame.setPreferredSize(new Dimension(445, 350));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		login = new LoginInterface();
		login.addComponents(frame.getContentPane());
		
		frame.pack();
		frame.setVisible(true);
	}

}

/*
	EXAMPLE OUTPUT FOR CUSTOMER ORDER:
	
Customer: Corey Baker
123 Main St.
Franklin, OH 12345

Items to be purchased: 
3	Apples		$2.97
2	Oranges		$1.98
1	Peanuts		$2.99
____________________________________________
Subtotal:			$7.94
Discounts:			$0.00
Tax:			8%
____________________________________________
Total:			$8.58

*/

/*

	EXAMPLE OUTPUT FOR SHOP:

ITEM		AMOUNT	COST
___________________________________________
Apples		74	$0.99
Bananas		80	$0.99
BlackPepper		19	$1.99
Cinnamon		28	$1.99
Cream		23	$1.99
Cumin		20	$1.99
Eggs		26	$1.49
Flour		60	$1.49

(continued)

*/
