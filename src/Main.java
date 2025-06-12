
import Managers.DBConnection;
import Managers.ManagerFacade;
import Managers.SQL_HELPER;

import java.sql.*;
import java.util.*;

public class Main {
    private static Scanner sc;
    private static ManagerFacade managerFacade;
    private static String input;
    private static SQL_HELPER sqlHelper;
    private static final Connection connection = DBConnection.getConnection();

    public static void main(String[] args) {

        sc = new Scanner(System.in);
        managerFacade = ManagerFacade.getInstance();
        sqlHelper = SQL_HELPER.getInstance();
        sqlHelper.loadDatabase();
        menu(managerFacade);
        sc.close();
    }

    private static void menu(ManagerFacade managerFacade) {
        int choice;
        System.out.println("\n------------------------------------------------------------------------");
        System.out.println(" ------------HELLO AND WELCOME TO OUR BUYER - SELLER PROGRAM-----------");
        System.out.println(" ------------(In anytime press -1 for return to main menu)-------------");
        System.out.println("------------------------------------------------------------------------");
        do {
            managerFacade.printMenu();
            try {
                input = sc.next();
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("\nInvalid input, please enter a digit!");
                choice = -1;
                continue;
            }
            sc.nextLine();
            switch (choice) {
                case -2:
                    break;
                case -1:
                    break;
                case 0:
                    System.out.println("\nThanks for using our system. GoodBye!");

                    break;
                case 1:
                    managerFacade.case1();

                    break;
                case 2:
                    managerFacade.case2();
                    break;
                case 3:
                    managerFacade.addProductSeller();
                    break;
                case 4:
                    managerFacade.addProductToBuyer();
                    break;
                case 5:
                    managerFacade.case5();
                    break;
                case 6:
                    System.out.println(managerFacade.case6());
                    break;
                case 7:
                    System.out.println(managerFacade.case7());
                    break;
                case 8:
                    System.out.println(managerFacade.case8());
                    break;
                case 9:
                    managerFacade.case9();
                    break;
                case 10:
                    managerFacade.case10();
                    break;
                default:
                    System.out.println("\nPlease enter a valid choice in range 0-9!");
                    break;
            }
        } while (choice != 0);

    }


}

