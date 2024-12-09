/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Win Yan
 */
import Entity.Customer;
import java.util.Scanner;
import java.io.*;

public class OrderClient {
        
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[0;34m";
    public static final String RESET = "\u001B[0m";
    public static final String PURPLE = "\u001B[35m";

    private OrderManager orderM = new OrderManager();
    private Scanner input = new Scanner(System.in);
    
    

    public void runSystem() throws IOException {
       int addCount = 0, displayCount = 0, SearchCount = 0, ModifyCount = 0, RemoveCount = 0, ClearCount = 0;
       boolean menu = true;
       
        while(menu) {
            System.out.println("\n\n");
            System.out.println(BLUE + "\t\t ========== FOOD ORDERING SYSTEM ==========\n");
            System.out.println(PURPLE + "\t\t             -- PIZZA HUT --             \n" + RESET);
            System.out.println("\t\t  [1] Add Order");          
            System.out.println("\t\t  [2] Display Order");
            System.out.println("\t\t  [3] Search Customer's Order");         
            System.out.println("\t\t  [4] Modify Order & Customer");
            System.out.println("\t\t  [5] Remove Order");
            System.out.println("\t\t  [6] Clear Order");
            System.out.println("\t\t  [7] Exit");
            int option = 0;
            while (option<1 || option > 7) {
                System.out.print("\n\t\t  Option: ");
                option = input.nextInt();

                switch (option) {
                    case 1:
                        orderM.displayMenu();
                        orderM.getOrder();
                        addCount += 1;
                        break;
                    case 2:
                        orderM.displayCustomerOrder();
                        displayCount += 1;
                        break;
                    case 3:
                        orderM.searchCustomer();
                        SearchCount += 1;
                        break;
                    case 4:
                        orderM.modifyOrder();
                        ModifyCount += 1;
                        break;
                    case 5:
                        orderM.removeOrder();
                        RemoveCount += 1;
                        break;
                    case 6:
                        orderM.clearOrder();
                        ClearCount += 1;
                        break;
                    case 7:
                        System.out.println("\n\n\n\n\t\t\tSummary of System Usage");
                        System.out.println("\t\t\t=======================");
                        System.out.println("\t\t\tYou have used the following functions .. times\n");
                        System.out.println("\t\t\t > Add Order     : " + addCount);
                        System.out.println("\t\t\t > Display Order : " + displayCount);
                        System.out.println("\t\t\t > Search Order  : " + SearchCount);
                        System.out.println("\t\t\t > Modify Order  : " + ModifyCount);
                        System.out.println("\t\t\t > Remove Order  : " + RemoveCount);
                        System.out.println("\t\t\t > Clear Order   : " + ClearCount);

                        System.out.println("\n\n" + PURPLE + "\t\t        Thanks for using the system.");
                        System.out.println("\t\t             Have a great day!" + RESET);
                        menu = false;
                        break;
                    default:
                        System.out.println(RED + "\t\t  Invalid option. Please select between 1-7" + RESET);
                        break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new OrderClient().runSystem();
    }
}
