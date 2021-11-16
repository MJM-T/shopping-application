# Shopping Application

***A group of Java files to implement a basic GUI with front-end and back-end (insecure) management.***

***

## File Descriptions

`Main.java`: Driver file for application, sets up a GUI frame using the Swing library.

`LoginInterface.java`: First thing displayed to the user--a GUI with login fields to complete which determine whether the user is a customer, employee, or manager.

`Manager.java`: Defines the fundamental manager class, including actions which the manager can perform. Highest file in the hierarchy, can do anything a customer or employee can.

`ManagerInterface.java`: Constructs the GUI for the manager. Can do anything an employee can.

`Employee.java`: Defines the fundamental employee class, including some accessors.

`EmployeeInterface.java`: Constructs the GUI for the employee.

`inventory.txt`: Dynamic text file which stores the current items in the store.

`inventoryBackup.txt`: Backup inventory file *(DEPRECATED)*.

`userData/`: Directory; contains all data for employee/manager logins, as well as customer orders and customer information.
