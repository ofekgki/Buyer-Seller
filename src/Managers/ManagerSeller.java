package Managers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import Enums.ExceptionsMessages;
import Factory.FactoryUser;
import Models.Seller;


public class ManagerSeller implements SellerInterface {

    private String input;
    private String msg;

    private int last_id;

    private final ArrayList<Seller> list_seller = new ArrayList<>();

    private static ManagerSeller instance;

    public static ManagerSeller getInstance() {
        if (instance == null)
            instance = new ManagerSeller();
        return instance;
    }

    private ManagerSeller() {
        FactoryUser fUser = new FactoryUser();
    }

    public ArrayList<Seller> getSellers() {
        return list_seller;
    }

    public int getNumberOfSellers() {
        return list_seller.size();
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
            if (index > list_seller.size() || index <= 0)
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
        last_id++;
    }

    public String sellersInfo() {
        if (list_seller.isEmpty()) {
            return "\nHaven't sellers yet, cannot be proceed. return to Menu.";
        }

        StringBuilder sb = new StringBuilder("\nSellers info:\n--------------\n");

        for (int i = 0; i < list_seller.size(); i++) {
            sb.append(i + 1)
                    .append(") ")
                    .append("seller ID : ")
                    .append(getSellers().get(i).getSeller_id())
                    .append("\n	")
                    .append("Seller Name :  ")
                    .append(list_seller.get(i).getUserName())
                    .append(": ")
                    .append(list_seller.get(i).toString())
                    .append("\n")
                    .append("________________________________________________________________________________\n");

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

    public Seller getSellerById(int id) {
        for (Seller s : list_seller) {
            if (s.getSeller_id() == id) {
                return s;
            }
        }
        return null;
    }

    public int getLastId() {
        return last_id;
    }
}


