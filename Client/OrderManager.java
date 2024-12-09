package Client;

import Entity.Order;
import Entity.Customer;
import Entity.Food;
import ADT.ArrayList;
import ADT.ListInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

public class OrderManager {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[0;34m";
    public static final String GREEN = "\u001B[32m";

    //List
    private ListInterface<Customer> customerList = new ArrayList<>();
    private ListInterface<Order> orderList = new ArrayList<>();
    private ListInterface<Food> foodList = new ArrayList<>();

    Customer customer = new Customer();
    Order order = new Order();
    Food food = new Food();

    private Scanner scanner = new Scanner(System.in);

    public OrderManager() {
        //dummy data for customers
        customerList.add(new Customer(1001, "Jennie", "012-2364578"));
        customerList.add(new Customer(1002, "Rose", "018-2469857"));
        customerList.add(new Customer(1003, "Jisoo", "012-5942365"));
        customerList.add(new Customer(1004, "Lisa", "012-9876543"));

        //dummy data for menus
        foodList.add(new Food("F001", "Tropical Chicken", 23.4));
        foodList.add(new Food("F002", "Beef Pepperoni", 30.50));
        foodList.add(new Food("F003", "Deluxe Cheese", 20.30));
        foodList.add(new Food("F004", "Hawaiian Chicken", 25.20));
        foodList.add(new Food("F005", "Island Supreme", 19.70));
        
    }

    public void displayMenu() {
        System.out.println("\n" + BLUE + "\t\tFood Menu");
        System.out.println("\t\t=========" + RESET);
        System.out.println("\t\t---+---------+---------------------------+-----------");
        System.out.println("\t\tNo | Food ID |         Food Name         | Unit Price");
        System.out.println("\t\t---+---------+---------------------------+-----------");
        for (int i = 1; i <= foodList.getNumberOfEntries(); i++) {
            System.out.printf("\t\t%2d | %-7s | %-25s | %10.2f\n", i, foodList.getEntry(i).getFoodId(), foodList.getEntry(i).getFoodName(), foodList.getEntry(i).getUnitPrice());
        }
        System.out.println("\t\t---+---------+---------------------------+-----------");
    }

    public void getOrder() {

        int orderId;
        while (true) {
            System.out.print("\n\t\tEnter Order ID: ");
            orderId = scanner.nextInt();

            //validation for existing orderID
            order = orderList.getEntry(new Order(orderId));
            if (order != null) {
                System.out.println(RED + "\t\t[Order ID already exists.]" + RESET);
            } else {
                break;
            }
        }

        int row;
        while (true) {
            System.out.print("\n\t\tEnter row: ");
            row = scanner.nextInt();
            if (row > foodList.getNumberOfEntries()) {
                System.out.println(RED + "\t\tInvalid input. Out of range." + RESET);
            } else {
                break;
            }
        }

        int quantity = 0;
        System.out.print("\n\t\tEnter quantity: ");
        quantity = scanner.nextInt();

        order = new Order(orderId, LocalDate.now(), foodList.getEntry(row).getFoodName(), foodList.getEntry(row).getUnitPrice(), quantity);

        System.out.println("\n\n\t\tYour Order");
        System.out.println("\t\t==========");
        System.out.println("\t\tOrder ID   : " + orderId);
        System.out.println("\t\tOrder Date : " + LocalDate.now());
        System.out.println("\t\tFood Name  : " + foodList.getEntry(row).getFoodName());
        System.out.printf("\t\tUnit Price : %.2f\n", foodList.getEntry(row).getUnitPrice());
        System.out.println("\t\tQuantity   : " + quantity);
        System.out.println("\t\t------------------------------");
        System.out.printf("\t\tTotal     : %.2f\n", foodList.getEntry(row).getUnitPrice() * quantity);

        System.out.println("\t\t------------------------------");


        scanner.nextLine(); //buffer

        //prompt customer details
        int custId;
        System.out.print("\n\t\tEnter Customer ID: ");
        custId = scanner.nextInt();

        //boolean existing = false;
        customer = customerList.getEntry(new Customer(custId));
        if (customer == null) {
            System.out.println(RED + "\t\t[No customer found.]" + RESET);
            scanner.nextLine(); //buffer
            while (true) {
                System.out.println("\n\t\tAdd new customer? (Y/N)");
                System.out.print("\t\t>");

                char addNewCust = Character.toUpperCase(scanner.nextLine().charAt(0));
                if (addNewCust == 'Y') {
                    //prompt input
                    System.out.print("\n\t\tEnter Customer ID: " + custId + "\n");
                    System.out.print("\t\tEnter Customer Name: ");
                    String name = scanner.nextLine();

                    System.out.print("\t\tEnter Phone Number: ");
                    String phoneNum = scanner.nextLine();

                    customer = new Customer(custId, name, phoneNum);
                    customerList.add(customer);
                    break;
                } else if (addNewCust == 'N') {
                    break;
                } else {
                    System.out.println(RED + "\t\tInvalid option. Please enter only Y or N" + RESET);
                }
            }
        }else{
            scanner.nextLine(); //buffer
        }
        
        //confirmation
        while (true) {
            System.out.println("\n\t\tConfirm to place order? (Y/N)");
            System.out.print("\t\t>");

            char addOrder = Character.toUpperCase(scanner.nextLine().charAt(0));
            if (addOrder == 'Y') {
                //store order into arraylist
                orderList.add(order);
                customer.addOrderToCust(order);
                appendCustOrderFile(customer, order);

                System.out.println(GREEN + "\t\t[Order has been placed uccessfully.]" + RESET);
                break;
            } else if (addOrder == 'N') {
                System.out.println(RED + "\t\t[Ordering is cancelled.]" + RESET);
                break;
            } else {
                System.out.println(RED + "\t\t[Invalid option. Please enter only Y or N.]" + RESET);
            }
        }
        
        //display order summary
    }

    public void displayCustomerOrder() {

        System.out.println("\t\t|----+---------+----------------------+--------------+-----------------+----------+------------+----------------------+------------+-----+------------|");
        System.out.println("\t\t| No | Cust ID |    Customer Name     | Phone Number | Order(s) placed | Order ID | Order Date |       Food Name      | Food Price | Qty | Total Cost |");
        System.out.println("\t\t|----+---------+----------------------+--------------+-----------------+----------+------------+----------------------+------------+-----+------------|");

        int labelling =1;
        for (int i = 1; i <= customerList.getNumberOfEntries(); i++) {

            
            ListInterface<Order> tempList = new ArrayList<>();
            tempList = customerList.getEntry(i).getAllOrder();

            if (tempList.isEmpty()) {
                System.out.printf("\t\t| %2s | %-7s | %-20s | %-12s | %15d | %-76s |\n", labelling, customerList.getEntry(i).getCustId(), customerList.getEntry(i).getCustName(), customerList.getEntry(i).getCustPhoneNumber(), tempList.getNumberOfEntries(), "No records found.");
                labelling++;
            }

            for (int j = 1; j <= tempList.getNumberOfEntries(); j++) {
                if (j == 1) {
                    System.out.printf("\t\t| %2s | %-7s | %-20s | %-12s | %15d | %-8s | %-10s | %-20s | %10.2f | %3s | %10.2f |\n", labelling, customerList.getEntry(i).getCustId(), customerList.getEntry(i).getCustName(), customerList.getEntry(i).getCustPhoneNumber(), tempList.getNumberOfEntries(), orderList.getEntry(j).getOrderId(), orderList.getEntry(j).getOrderDate(), orderList.getEntry(j).getFoodName(), orderList.getEntry(j).getUnitPrice(), orderList.getEntry(j).getQuantity(), orderList.getEntry(j).getUnitPrice() * orderList.getEntry(j).getQuantity());
                    labelling++;
                } else {
                    System.out.printf("\t\t| %2s | %-7s | %-20s | %-12s | %15s | %-8s | %-10s | %-20s | %10.2f | %3s | %10.2f |\n", "", "", "", "", "", orderList.getEntry(j).getOrderId(), orderList.getEntry(j).getOrderDate(), orderList.getEntry(j).getFoodName(), orderList.getEntry(j).getUnitPrice(), orderList.getEntry(j).getQuantity(), orderList.getEntry(j).getUnitPrice() * orderList.getEntry(j).getQuantity());

                }
            }
            System.out.println("\t\t|----+---------+----------------------+--------------+-----------------+----------+------------+----------------------+------------+-----+------------|");
        }
    }

    public void searchCustomer() {
        System.out.println("\n\n" + BLUE + "\t\tSearch Customer Order");
        System.out.println("\t\t=====================" + RESET);
        System.out.println("\t\t1. Search by Customer");
        System.out.println("\t\t2. Search by Order");
        System.out.print("\n\t\tOption: ");
        int searchOption = scanner.nextInt();

        switch (searchOption) {
            case 1:
                scanner.nextLine(); //buffer

                System.out.println("\n\t\tCustomer");
                System.out.println("\t\t========");

                int custId;
                System.out.print("\t\tEnter Customer ID: ");
                custId = scanner.nextInt();

                //boolean existing = false;
                customer = customerList.getEntry(new Customer(custId));
                if (customer == null) {
                    System.out.println(RED + "\t\t[No customer found.]" + RESET);
                } else {
                    System.out.println("\t\tCustomer ID  : " + customer.getCustId());
                    System.out.println("\t\tCustomer Name: " + customer.getCustName());
                    System.out.println("\t\tPhone Number : " + customer.getCustPhoneNumber());

                    ListInterface<Order> tempList = customerList.getEntry(new Customer(custId)).getAllOrder();
                    System.out.println("\t\tOrder(s) placed: " + tempList.getNumberOfEntries());

                    for (int j = 1; j <= tempList.getNumberOfEntries(); j++) {
                        System.out.println("");
                        System.out.println("\t\tOrder ID   : " + tempList.getEntry(j).getOrderId());
                        System.out.println("\t\tOrder Date : " + tempList.getEntry(j).getOrderDate());
                        System.out.println("\t\tFood       : " + tempList.getEntry(j).getFoodName());
                        System.out.printf("\t\tUnit Price : %.2f\n", tempList.getEntry(j).getUnitPrice());
                        System.out.println("\t\tQuantity   : " + tempList.getEntry(j).getQuantity());
                        System.out.printf("\t\tTotal      : %.2f\n", tempList.getEntry(j).getUnitPrice() * tempList.getEntry(j).getQuantity());
                    }
                }
                break;
            case 2:
                scanner.nextLine();//buffer

                System.out.println("\n\t\tOrder");
                System.out.println("\t\t=====");

                System.out.println("\t\t[1] Order ID");
                System.out.println("\t\t[2] Order Date");
                System.out.println("\t\t[3] Food Name");

                System.out.print("\n\t\tOption: ");
                int orderOption = scanner.nextInt();

                switch (orderOption) {
                    case 1:

                        scanner.nextLine(); //rewind stdin
                        int orderId;

                        System.out.print("\n\t\tEnter Order ID: ");
                        orderId = scanner.nextInt();

                        order = orderList.getEntry(new Order(orderId));

                        if (order == null) {
                            System.out.println(RED + "\t\t[No records found.]" + RESET);
                        } else {
                            System.out.println("");
                            System.out.println("\t\tOrder ID   : " + order.getOrderId());
                            System.out.println("\t\tOrder Date : " + order.getOrderDate());
                            System.out.println("\t\tFood       : " + order.getFoodName());
                            System.out.printf("\t\tUnit Price : %.2f\n", order.getUnitPrice());
                            System.out.println("\t\tQuantity   : " + order.getQuantity());
                            System.out.printf("\t\tTotal      : %.2f\n", order.getUnitPrice() * order.getQuantity());
                        }

                        break;
                    case 2:
                        scanner.nextLine(); //buffer
                        String orderDate;
                        while (true) {
                            System.out.print("\n\t\tEnter Order Date (YYYY-MM-DD): ");
                            orderDate = scanner.nextLine();

                            if (orderDate.length() != 10) {
                                System.out.println(RED + "\t\t[The length of order date should be 10.]" + RESET);
                            } else if (!Character.isDigit(orderDate.charAt(0)) || !Character.isDigit(orderDate.charAt(1)) || !Character.isDigit(orderDate.charAt(2)) || !Character.isDigit(orderDate.charAt(3)) || orderDate.charAt(4) != '-' || !Character.isDigit(orderDate.charAt(5)) || !Character.isDigit(orderDate.charAt(6)) || orderDate.charAt(7) != '-' || !Character.isDigit(orderDate.charAt(8)) || !Character.isDigit(orderDate.charAt(9))) {
                                System.out.println(RED + "\t\t[Invalid format. Try (2022-12-12)]" + RESET);
                            } else if(orderDate.substring(5,7).compareTo("12") > 1){
                                System.out.println(RED + "\t\t[Invalid month value.]" + RESET);
                            }else if(orderDate.substring(8,10).compareTo("31") > 1){
                                System.out.println(RED + "\t\t[Invalid day value.]" + RESET);
                            }else if (LocalDate.parse(orderDate).compareTo((LocalDate.now())) > 0) {
                                System.out.println(RED + "\t\t[No records available for future date.]" + RESET);
                            }  else {
                                break;
                            }
                        }

                        LocalDate date = LocalDate.parse(orderDate);
                        //order = orderList.getEntry(new Order(date));

                        for (int i = 1; i <= orderList.getNumberOfEntries(); i++) {
                            if (date.equals(orderList.getEntry(i).getOrderDate())) {
                                order = orderList.getEntry(new Order(orderList.getEntry(i).getOrderId()));

                                System.out.println("");
                                System.out.println("\t\tOrder ID   : " + order.getOrderId());
                                System.out.println("\t\tOrder Date : " + order.getOrderDate());
                                System.out.println("\t\tFood       : " + order.getFoodName());
                                System.out.printf("\t\tUnit Price : %.2f\n", order.getUnitPrice());
                                System.out.println("\t\tQuantity   : " + order.getQuantity());
                                System.out.printf("\t\tTotal      : %.2f\n", order.getUnitPrice() * order.getQuantity());
                            }
                        }

                        if (order == null) {
                            System.out.println(RED + "\t\t[No records found.]" + RESET);
                        }

                        break;
                    case 3:
                        scanner.nextLine(); //buffer
                        String foodName;

                        System.out.print("\n\t\tEnter Food Name: ");
                        foodName = scanner.nextLine();

                        for (int i = 1; i <= orderList.getNumberOfEntries(); i++) {
                            if (foodName.equals(orderList.getEntry(i).getFoodName())) {
                                order = orderList.getEntry(new Order(orderList.getEntry(i).getOrderId()));

                                System.out.println("");
                                System.out.printf("\t\tOrder ID   : %d\n" + order.getOrderId());
                                System.out.println("\t\tOrder Date : " + order.getOrderDate());
                                System.out.println("\t\tFood       : " + order.getFoodName());
                                System.out.printf("\t\tUnit Price : %.2f\n", order.getUnitPrice());
                                System.out.println("\t\tQuantity   : " + order.getQuantity());
                                System.out.printf("\t\tTotal      : %.2f\n", order.getUnitPrice() * order.getQuantity());
                            }
                        }

                        //order = orderList.getEntry(new Order(orderList.getEntry(i).getOrderId()));
                        if (order == null) {
                            System.out.println(RED + "\t\t[No records found.]" + RESET);
                        }

                        break;
                }
        }
    }

    public void modifyOrder() {

        System.out.println("\n\n" + BLUE + "\t\tModification");
        System.out.println("\t\t=============" + RESET);
        System.out.println("\t\t[1] Customer");
        System.out.println("\t\t[2] Order");
        System.out.println("\t\t[3] Quit");
        while (true) {
            System.out.print("\n\t\tOption: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    if (customerList.isEmpty()) {
                        System.out.println(RED + "\t\tNo records found." + RESET);
                        return;     //back to menu if no records to be modified
                    }
                    System.out.println("\t\t|----+---------+----------------------+--------------|");
                    System.out.println("\t\t| No | Cust ID |    Customer Name     | Phone Number |");
                    System.out.println("\t\t|----+---------+----------------------+--------------|");

                    for (int i = 1; i <= customerList.getNumberOfEntries(); i++) {
                        System.out.printf("\t\t| %2s | %-7s | %-20s | %-12s |\n", i, customerList.getEntry(i).getCustId(), customerList.getEntry(i).getCustName(), customerList.getEntry(i).getCustPhoneNumber());
                    }
                    System.out.println("\t\t|----+---------+----------------------+--------------|");

                    int custRow;
                    while (true) {
                        System.out.print("\n\t\tSelect row number: ");
                        custRow = scanner.nextInt();

                        if (custRow > customerList.getNumberOfEntries()) {
                            System.out.println(RED + "\t\tInvalid input. Out of range." + RESET);
                        } else {
                            break;
                        }
                    }
                    customer = customerList.getEntry(custRow);

                    System.out.println("\n\n\t\tModify ...");
                    System.out.println("\t\t==========");
                    System.out.println("\t\t[1] Customer ID");
                    System.out.println("\t\t[2] Customer Name");
                    System.out.println("\t\t[3] Customer Phone Number");
                    System.out.print("\n\t\tOption: ");
                    int custOption = scanner.nextInt();

                    switch (custOption) {
                        case 1:
                            scanner.nextLine();
                            int newCustId;
                            while (true) {
                                System.out.print("\n\t\tEnter new Customer ID: ");
                                newCustId = scanner.nextInt();
                                boolean repeated = false;

                                //validation
                                for (int i = 1; i <= customerList.getNumberOfEntries(); i++) {
                                    if (newCustId == customerList.getEntry(i).getCustId()) {
                                        System.out.println(RED + "\t\t[Customer ID enter already exists.]" + RESET);
                                        repeated = true;
                                    }
                                }
                                if (!repeated) {
                                    break;
                                }
                            }

                            //confirmation
                            scanner.nextLine();
                            while (true) {
                                System.out.println("\n\t\tConfirm to modify? (Y/N)");
                                System.out.print("\t\t>");
                                char editCust = Character.toUpperCase(scanner.nextLine().charAt(0));
                                
                                
                                if (editCust == 'Y') {
                                    customer.setCustId(newCustId);
                                    customerList.replace(custRow, customer);

                                    //modify in textfile
                                    //if order list is empty, no records will be existed in textfile
                                    if (!orderList.isEmpty()) {
                                        writeCustOrderFile(customerList, orderList);
                                    }
                                    System.out.println(GREEN + "\t\t[Modified successfully.]" + RESET);
                                    break;
                                } else if (editCust == 'N') {
                                    System.out.println(RED + "\t\t[No modification is done.]" + RESET);
                                    break;
                                } else {
                                    System.out.println(RED + "\t\t[Invalid option. Please enter only Y or N.]" + RESET);
                                }
                            }
                            break;
                        case 2:
                            scanner.nextLine();
                            String newCustName;
                            while (true) {
                                System.out.print("\n\t\tEnter new Customer Name: ");
                                newCustName = scanner.nextLine();
                                boolean valid = true;
                                //validation
                                for (int i = 0; i < newCustName.length(); i++) {
                                    if (!Character.isAlphabetic(newCustName.charAt(i)) && newCustName.charAt(i) != ' ') {
                                        System.out.println(RED + "\t\t[Do not include any numbers or symbols]" + RESET);
                                        valid = false;
                                    }
                                }

                                if (valid) {
                                    break;
                                }
                            }

                            //confirmation
                            while (true) {
                                System.out.println("\n\t\tConfirm to modify? (Y/N)");
                                System.out.print("\t\t>");

                                char editCust = Character.toUpperCase(scanner.nextLine().charAt(0));
                                if (editCust == 'Y') {
                                    customer.setCustName(newCustName);
                                    customerList.replace(custRow, customer);

                                    if (!orderList.isEmpty()) {
                                        writeCustOrderFile(customerList, orderList);
                                    }
                                    System.out.println(GREEN + "\t\t[Modified successfully.]" + RESET);

                                    break;
                                } else if (editCust == 'N') {
                                    System.out.println(RED + "\t\t[No modification is done.]" + RESET);
                                    break;
                                } else {
                                    System.out.println(RED + "\t\t[Invalid option. Please enter only Y or N.]" + RESET);
                                }
                            }

                            break;
                        case 3:
                            scanner.nextLine();
                            String newCustPhoneNum;
                            while (true) {
                                System.out.print("\n\t\tEnter new Customer Phone Number: ");
                                newCustPhoneNum = scanner.nextLine();
                                boolean valid = true;
                                //validation
                                for (int i = 0; i < newCustPhoneNum.length(); i++) {
                                    if (!Character.isDigit(newCustPhoneNum.charAt(i)) && newCustPhoneNum.charAt(i) != '-') {
                                        System.out.println(RED + "\t\t[Do not include any alphabets or symbols except(-). ]" + RESET);
                                        valid = false;
                                    } else if (newCustPhoneNum.length() < 11 || newCustPhoneNum.length() > 12) {
                                        System.out.println(RED + "\t\t[Invalid phone number format. e.g:(012-3456789)]" + RESET);
                                        valid = false;
                                    }
                                }

                                if (valid) {
                                    break;
                                }
                            }

                            //confirmation
                            while (true) {
                                System.out.println("\n\t\tConfirm to modify? (Y/N)");
                                System.out.print("\t\t>");

                                char editCust = Character.toUpperCase(scanner.nextLine().charAt(0));
                                if (editCust == 'Y') {
                                    customer.setCustPhoneNumber(newCustPhoneNum);
                                    customerList.replace(custRow, customer);

                                    if (!orderList.isEmpty()) {
                                        writeCustOrderFile(customerList, orderList);
                                    }
                                    System.out.println(GREEN + "\t\t[Modified successfully.]" + RESET);
                                    break;
                                } else if (editCust == 'N') {
                                    System.out.println(RED + "\t\t[No modification is done.]" + RESET);
                                    break;
                                } else {
                                    System.out.println(RED + "\t\t[Invalid option. Please enter only Y or N.]" + RESET);
                                }
                            }
                            break;
                    }
                    return;
                    
                case 2:

                    System.out.println("\t\t|----+----------+------------+----------------------+------------+-----+------------|");
                    System.out.println("\t\t| No | Order ID | Order Date |       Food Name      | Food Price | Qty | Total Cost |");
                    System.out.println("\t\t|----+----------+------------+----------------------+------------+-----+------------|");

                    if (orderList.isEmpty()) {
                        System.out.println(RED + "\t\t[No Records found. No modifications can be carried out.]" + RESET);
                        return;
                    }

                    for (int i = 1; i <= orderList.getNumberOfEntries(); i++) {
                        System.out.printf("\t\t| %2s | %-8s | %-10s | %-20s | %-10.2f | %3s | %-10.2f |\n", i, orderList.getEntry(i).getOrderId(), orderList.getEntry(i).getOrderDate(), orderList.getEntry(i).getFoodName(), orderList.getEntry(i).getUnitPrice(), orderList.getEntry(i).getQuantity(), orderList.getEntry(i).getUnitPrice() * orderList.getEntry(i).getQuantity());
                    }
                    System.out.println("\t\t|----+----------+------------+----------------------+------------+-----+------------|");

                    //prompt user input and get orderID
                    int orderRow;
                    while (true) {
                        System.out.print("\n\t\tSelect row number: ");
                        orderRow = scanner.nextInt();

                        if (orderRow > orderList.getNumberOfEntries()) {
                            System.out.println(RED + "\t\tInvalid input. Out of range." + RESET);
                        } else {
                            break;
                        }
                    }
                    order = orderList.getEntry(orderRow);

                    //order from which customer
                    for (int i = 1; i <= customerList.getNumberOfEntries(); i++) {
                        customer = customerList.getEntry(i);
                        ListInterface<Order> tempList = customer.getAllOrder();

                        for (int j = 1; j <= tempList.getNumberOfEntries(); j++) {
                            if (tempList.getEntry(j).equals(order.getOrderId())) {
                                //if the customer's orderID is same as chosen orderID, break the loop and get the current customer
                                break;
                            }
                        }
                    }

                    System.out.println("\n\n\t\tModify ...");
                    System.out.println("\t\t==========");
                    System.out.println("\t\t[1] Order ID");
                    System.out.println("\t\t[2] Order Date");
                    System.out.println("\t\t[3] Quantity");
                    System.out.println("\t\t[4] Back to previous page");
                    System.out.print("\n\t\tOption: ");

                    int orderOption = scanner.nextInt();
                    switch (orderOption) {
                        case 1:
                            scanner.nextLine();
                            int newOrderId;

                            System.out.print("\n\t\tEnter new Order ID : ");
                            newOrderId = scanner.nextInt();

                            order.setOrderId(newOrderId);

                            //confirmation
                            scanner.nextLine();
                            while (true) {
                                System.out.println("\n\t\tConfirm to modify? (Y/N)");
                                System.out.print("\t\t>");

                                char editOrder = Character.toUpperCase(scanner.nextLine().charAt(0));
                                if (editOrder == 'Y') {
                                    orderList.replace(orderRow, order);
                                    customer.modifyOrderFromCust(orderRow, order);
                                    
                                    if (!orderList.isEmpty()) {
                                        //modify in textfile
                                        writeCustOrderFile(customerList, orderList);

                                    }
                                    System.out.println(GREEN + "\t\t[Modified successfully.]" + RESET);
                                    break;
                                } else if (editOrder == 'N') {
                                    System.out.println(RED + "\t\t[No modification is done.]" + RESET);
                                    break;
                                } else {
                                    System.out.println(RED + "\t\t[Invalid option. Please enter only Y or N.]" + RESET);
                                }
                            }
                            break;
                        case 2:
                            scanner.nextLine();
                            String newDateStr;

                            while (true) {
                                System.out.print("\n\t\tEnter new Order Date (YYYY-MM-DD): ");
                                newDateStr = scanner.nextLine();

                                if (newDateStr.length() != 10) {
                                    System.out.println(RED + "\t\t[The length of order date should be 10.]" + RESET);
                                } else if (!Character.isDigit(newDateStr.charAt(0)) || !Character.isDigit(newDateStr.charAt(1)) || !Character.isDigit(newDateStr.charAt(2)) || !Character.isDigit(newDateStr.charAt(3)) || newDateStr.charAt(4) != '-' || !Character.isDigit(newDateStr.charAt(5)) || !Character.isDigit(newDateStr.charAt(6)) || newDateStr.charAt(7) != '-' || !Character.isDigit(newDateStr.charAt(8)) || !Character.isDigit(newDateStr.charAt(9))) {
                                    System.out.println(RED + "\t\t[Invalid format. Try (2022-12-12)]" + RESET);
                                } else if (LocalDate.parse(newDateStr).compareTo((LocalDate.now())) > 0) {
                                    System.out.println(RED + "\t\t[No records available for future date]" + RESET);
                                } else {
                                    break;
                                }
                            }

                            //parsing to LocalDate after validation
                            LocalDate newDate = LocalDate.parse(newDateStr);
                            order.setOrderDate(newDate);

                            //confirmation
                            while (true) {
                                System.out.println("\n\t\tConfirm to modify? (Y/N)");
                                System.out.print("\t\t>");

                                char editOrder = Character.toUpperCase(scanner.nextLine().charAt(0));
                                if (editOrder == 'Y') {
                                    orderList.replace(orderRow, order);
                                    customer.modifyOrderFromCust(orderRow, order);

                                    if (!orderList.isEmpty()) {
                                        //modify in textfile
                                        writeCustOrderFile(customerList, orderList);

                                    }
                                    System.out.println(GREEN + "\t\t[Modified successfully.]" + RESET);
                                    break;
                                } else if (editOrder == 'N') {
                                    System.out.println(RED + "\t\t[No modification is done.]" + RESET);
                                    break;
                                } else {
                                    System.out.println(RED + "\t\t[Invalid option. Please enter only Y or N.]" + RESET);
                                }
                            }

                            break;
                        case 3:
                            scanner.nextLine();
                            int quantity;

                            System.out.print("\n\t\tEnter new quantity : ");
                            quantity = scanner.nextInt();

                            order.setQuantity(quantity);

                            scanner.nextLine();
                            //confirmation
                            while (true) {
                                System.out.println("\n\t\tConfirm to modify? (Y/N)");
                                System.out.print("\t\t>");

                                char editOrder = Character.toUpperCase(scanner.nextLine().charAt(0));
                                if (editOrder == 'Y') {
                                    orderList.replace(orderRow, order);
                                    customer.modifyOrderFromCust(orderRow, order);
                                    if (!orderList.isEmpty()) {
                                        //modify in textfile
                                        writeCustOrderFile(customerList, orderList);

                                    }
                                    System.out.println(GREEN + "\t\t[Modified successfully.]" + RESET);
                                    break;
                                } else if (editOrder == 'N') {
                                    System.out.println(RED + "\t\t[No modification is done.]" + RESET);
                                    break;
                                } else {
                                    System.out.println(RED + "\t\t[Invalid option. Please enter only Y or N.]" + RESET);
                                }
                            }
                            break;
                        case 4:
                            return;
                        default:
                            System.out.println(RED + "\t\t[Invalid option. Please enter only 1-3.]" + RESET);
                            break;
                    }
                    return;
                case 3:
                    return;
                default:
                    System.out.println(RED + "Invalid option. Please enter only 1-3." + RESET);
                    break;
            }
        }
    }

    public void removeOrder() {
        System.out.println("\n\n" + BLUE + "\t\tRemove Order");
        System.out.println("\t\t============" + RESET);

        if (orderList.isEmpty()) {
            System.out.println(RED + "\t\tNo orders found." + RESET);
            return;
        }

        System.out.println("\t\t|----+----------+------------+----------------------+------------+-----+------------|");
        System.out.println("\t\t| No | Order ID | Order Date |       Food Name      | Food Price | Qty | Total Cost |");
        System.out.println("\t\t|----+----------+------------+----------------------+------------+-----+------------|");

        for (int i = 1; i <= orderList.getNumberOfEntries(); i++) {
            System.out.printf("\t\t| %2s | %-8s | %-10s | %-20s | %-10.2f | %3s | %-10.2f |\n", i, orderList.getEntry(i).getOrderId(), orderList.getEntry(i).getOrderDate(), orderList.getEntry(i).getFoodName(), orderList.getEntry(i).getUnitPrice(), orderList.getEntry(i).getQuantity(), orderList.getEntry(i).getUnitPrice() * orderList.getEntry(i).getQuantity());
        }
        System.out.println("\t\t|----+----------+------------+----------------------+------------+-----+------------|");

        System.out.print("\n\t\tSelect row number: ");
        int row = scanner.nextInt();

        if (row > orderList.getNumberOfEntries()) {
            System.out.println(RED + "Invalid option. Out of range" + RESET);
        } else {
            //remove at orderList
            orderList.remove(row);
            order = orderList.getEntry(row);
            
            //order from which customer
            for (int i = 1; i <= customerList.getNumberOfEntries(); i++) {
                customer = customerList.getEntry(i);
                ListInterface<Order> tempList = customer.getAllOrder();

                for (int j = 1; j <= tempList.getNumberOfEntries(); j++) {
                    if (tempList.getEntry(j).equals(order.getOrderId())) {
                        //if the customer's orderID is same as chosen orderID, break the loop and get the current customer

                        customer.removeOrderOfCust(row);
                        break;
                    }
                }
            }

            
            System.out.println(GREEN + "\t\tRecord removed successfully." + RESET);
        }

    }

    public void clearOrder() {
        System.out.println("\n" + BLUE + "\t\tClear Order");
        System.out.println("\t\t===========" + RESET);

        if (orderList.isEmpty()) {
            System.out.println(RED + "\t\t[No records found.]" + RESET);
            return;
        }

        while (true) {
            System.out.println("\t\tConfirm to clear all the orders? (Y/N)");
            System.out.print("\t\t> ");
            char confirm = Character.toUpperCase(scanner.next().charAt(0));

            if (confirm == 'Y') {
                orderList.clear();
                customer.clearOrderOfCust();
                
                System.out.println(GREEN + "\t\tOrder has been cleared successfully." + RESET);
                break;
            } else if (confirm == 'N') {
                System.out.println("\t\tOrder are not cleared.");
                break;
            } else {
                System.out.println(RED + "\t\tInvalid option." + RESET);
            }
        }
    }

    public void appendCustOrderFile(Customer customer, Order order) {
        try {
            File file = new File("customerorder.txt");
            PrintWriter write = new PrintWriter(new FileWriter(file, true));

            write.print(customer.toString() + "|" + order.toString() + "\n");
            write.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    public void writeCustOrderFile(ListInterface<Customer> customer, ListInterface<Order> order) {
        try {
            FileWriter myWriter = new FileWriter("customerorder.txt");

            for (int i = 1; i <= customer.getNumberOfEntries(); i++) {

                ListInterface<Order> tempList = new ArrayList<>();
                tempList = customerList.getEntry(i).getAllOrder();

                for (int j = 1; j <= tempList.getNumberOfEntries(); j++) {
                    myWriter.write(customer.getEntry(i).toString() + "|" + order.getEntry(j).toString() + "\n");
                }
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
