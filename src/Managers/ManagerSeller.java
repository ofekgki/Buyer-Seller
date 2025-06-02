package Managers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import Comparators.CompareSellersByProductsNumber;
import Enums.ExceptionsMessages;
import Factory.FactoryUser;
import Models.Seller;


public class ManagerSeller implements SellerInterface {

    private String input;
    private String msg;
    private final int SIZE_INCREASE = 2;

    private int numberOfSellers;

    private final Comparator<Seller> comparatorSeller;

    private final ArrayList<Seller> list_seller = new ArrayList<>();

    private static ManagerSeller instance;

    public static ManagerSeller getInstance() {
        if (instance == null)
            instance = new ManagerSeller();
        return instance;
    }

    private ManagerSeller() {
        comparatorSeller = new CompareSellersByProductsNumber();
        FactoryUser fUser = new FactoryUser();
    }

    public ArrayList<Seller> getSellers() {
        return list_seller;
    }

    public int getNumberOfSellers() {
        return numberOfSellers;
    }

    public String isExistSeller(String name) {
        for (Seller seller : list_seller) {
            if (seller.getUserName().equalsIgnoreCase(name)) {
                return "Seller name already EXIST, please try again!";
            }
        }
        return null;
    }

    public String chooseValidSeller(String indexInput) {
        try {
            int index = Integer.parseInt(indexInput);
            if (index > numberOfSellers || index <= 0)
                throw new IndexOutOfBoundsException(ExceptionsMessages.INVALID_SELLER_INDEX.getExceptionMessage());
        } catch (IndexOutOfBoundsException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return ExceptionsMessages.INVALID_NUMBER_CHOICE.getExceptionMessage();
        }
        return null;
    }

    public void addSeller(Seller seller) {
        list_seller.add(seller);
    }
    public void showlist(){
        for (Seller s : list_seller) {
            System.out.println(s);
        }

    }


    public String sellersInfo() {
        if (list_seller.isEmpty()) {
            return "\nHaven't sellers yet, cannot be proceed. return to Menu.";
        }

        StringBuilder sb = new StringBuilder("\nSellers info:\n--------------\n");

        // יוצרים עותק ממוין של הרשימה
        ArrayList<Seller> sortedSellers = new ArrayList<>(list_seller);
        sortedSellers.sort(comparatorSeller);

        for (int i = 0; i < sortedSellers.size(); i++) {
            sb.append(i + 1)
                    .append(") ")
                    .append(sortedSellers.get(i).getUserName())
                    .append(": ")
                    .append(sortedSellers.get(i).toString())
                    .append("\n");
        }

        return sb.toString();
    }

    public String sellersNames() {
        StringBuilder sb = new StringBuilder("Sellers:\n--------------\n");
        for (int i = 0; i < list_seller.size(); i++) {
            sb.append(i + 1)
                    .append(") ")
                    .append(list_seller.get(i).getUserName())
                    .append("\n");
        }
        return sb.toString();
    }

    public int chooseSeller() {
        System.out.println(sellersNames());

        while (true) {
            input = UserInput.stringInput("Please choose seller from the list above: ");
            if (input.equals("-1"))
                return -1;
            msg = chooseValidSeller(input);
            if (msg != null) {
                System.out.println(msg);
            } else {
                break;
            }
        }
        return Integer.parseInt(input) - 1;
    }

}


