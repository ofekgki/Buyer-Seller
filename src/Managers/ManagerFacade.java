package Managers;

import Enums.Category;
import Enums.ExceptionsMessages;
import Factory.FactoryProduct;
import Factory.FactoryUser;
import Models.*;


import java.sql.SQLException;
import java.util.*;

public class ManagerFacade implements Manageable {
    private final SQL_HELPER sqlHelper;
    private final ManagerBuyer managerBuyer;
    private final ManagerSeller managerSeller;
    private final ManagerProduct managerProduct;
    private String input;
    private String message;

    private final FactoryProduct fPro;
    private final FactoryUser fUser;

    private static ManagerFacade instance;


    private ManagerFacade() {
        sqlHelper = SQL_HELPER.getInstance();
        managerBuyer = ManagerBuyer.getInstance();
        managerSeller = ManagerSeller.getInstance();
        managerProduct = ManagerProduct.getInstance();
        fPro = new FactoryProduct();
        fUser = new FactoryUser();

    }

    public static ManagerFacade getInstance() {
        if (instance == null)
            instance = new ManagerFacade();
        return instance;
    }

    public void printMenu() {
        System.out.println("\nMenu : ");
        System.out.println("0) Exit");
        System.out.println("1) Add seller");
        System.out.println("2) Add buyer");
        System.out.println("3) Add item for seller");
        System.out.println("4) Add item for buyer");
        System.out.println("5) Payment for buyer");
        System.out.println("6) Buyer's details");
        System.out.println("7) Seller's details");
        System.out.println("8) Product's by category");
        System.out.println("9) Replace current cart with cart from history");
        System.out.println("Please enter your choice: ");
    }

    public String validProductIndex(int sellerIndex, String productIndexInput) {
        try {
            int productIndex = Integer.parseInt(productIndexInput);
            int numProducts = managerSeller.getSellers().get(sellerIndex).getNumOfProducts();

            if (productIndex <= 0 || productIndex > numProducts) {
                throw new IndexOutOfBoundsException(
                        ExceptionsMessages.INVALID_PRODUCT_INDEX.getExceptionMessage()
                );
            }
        } catch (NumberFormatException e) {
            return ExceptionsMessages.INVALID_NUMBER_CHOICE.getExceptionMessage();
        } catch (IndexOutOfBoundsException e) {
            return e.getMessage();
        }

        return null;
    }

    public void addProductBuyer(int buyerIndex, int sellerIndex, int productIndex) {
        Product p1;

        Product selectedProduct = managerSeller.getSellers().get(sellerIndex).getProducts().get(productIndex);

        if (selectedProduct instanceof ProductSpecialPackage) {
            p1 = fPro.createSpecialProduct(selectedProduct.getProductName(),
                    selectedProduct.getProductPrice(),
                    selectedProduct.getCategory().ordinal(),
                    ((ProductSpecialPackage) selectedProduct).getSpecialPackagePrice());
        } else {
            p1 = fPro.createRegularProduct(
                    selectedProduct.getProductName(),
                    selectedProduct.getProductPrice(),
                    selectedProduct.getCategory().ordinal()
            );
        }

        managerBuyer.getBuyers()
                .get(buyerIndex)
                .getCurrentCart()
                .addProductToCart(p1);
    }


    public void addProductSeller() {
        if (managerSeller.getNumberOfSellers() == 0) {
            System.out.println("Haven't sellers yet, cannot be proceed. return to Menu.");
            return;
        }
        int sellerIndex = managerSeller.chooseSeller();
        if (sellerIndex == -1)
            return;

        String input;
        do input = UserInput.stringInput("Enter product name to add:");
        while (input.isEmpty());
        String productName = input;

        String msg;
        do {
            input = UserInput.stringInput("Enter product price:");
            msg = ManagerProduct.getInstance().validPrice(input);
            if (msg != null) System.out.println(msg);
        } while (msg != null);
        double productPrice = Double.parseDouble(input);

        System.out.println(Categories.categoriesByNames());
        do {
            input = UserInput.stringInput("Choose category: ");
            msg = ManagerProduct.getInstance().validCategoryIndex(input);
            if (msg != null) System.out.println(msg);
        } while (msg != null);
        int categoryIndex = Integer.parseInt(input);

        double specialPackagePrice = 0;
        boolean isSpecial = false;
        do {
            input = UserInput.stringInput("This product have special package? YES / NO");
            if (input.equalsIgnoreCase("yes")) {
                isSpecial = true;
                do {
                    input = UserInput.stringInput("Enter price for special package:");
                    msg = ManagerProduct.getInstance().validPrice(input);
                    if (msg != null) System.out.println(msg);
                } while (msg != null);
                specialPackagePrice = Double.parseDouble(input);
                break;
            }
            if (!input.equalsIgnoreCase("no")) {
                System.out.println("Please enter YES / NO only !");
            }
        } while (!input.equalsIgnoreCase("no"));


        Product p1;

        if (isSpecial) {
            p1 = fPro.createSpecialProduct(productName, productPrice, categoryIndex, specialPackagePrice);
        } else {
            p1 = fPro.createRegularProduct(productName, productPrice, categoryIndex);
        }

        addProductToSeller(p1, sellerIndex);

        int sellerId = managerSeller.getSellers().get(sellerIndex).getSeller_id();
        sqlHelper.insertProduct(p1, sellerId);
        p1.setID(sqlHelper.getProductIdByName(p1.getProductName()));
        System.out.println("Product added successfully.");
    }


    public void addProductToSeller(Product p1, int sellerIndex) {
        // הוספה למוכר
        managerSeller.getSellers().get(sellerIndex).addProduct(p1);

        // הוספה למאגר כללי של מוצרים
        managerProduct.getAllProducts().add(p1);

        // הוספה לרשימת קטגוריות (אם יש כזה טיפול)
        managerProduct.addToCategoryArray(p1);
    }

    public void addProductToBuyer() {
        if (managerBuyer.getBuyers().isEmpty()) {
            System.out.println("Haven't buyers yet, cannot be proceed. return to Menu.");
            return;
        }
        if (managerSeller.getSellers().isEmpty()) {
            System.out.println("Haven't sellers yet, cannot be proceed. return to Menu.");
            return;
        }

        int buyerIndex = managerBuyer.chooseBuyer();
        if (buyerIndex == -1)
            return;

        if (managerBuyer.getBuyerById(buyerIndex).getCurrentCart() == null) {
            managerBuyer.getBuyerById(buyerIndex).createNewCart(sqlHelper.getLastID());
        }

        int sellerIndex = managerSeller.chooseSeller();
        if (sellerIndex == -1)
            return;

        Seller selectedSeller = managerSeller.getSellers().get(sellerIndex);
        if (selectedSeller.getNumOfProducts() == 0) {
            System.out.println("This seller hasn't added any products yet, cannot proceed. Returning to Menu.");
            return;
        }

        System.out.println(selectedSeller.toString());

        do {
            input = UserInput.stringInput("Enter product's number to add to your cart: ");
            if (input.equals("-1"))
                return;

            message = validProductIndex(sellerIndex, input);
            if (message != null) {
                System.out.println(message);
            }
        } while (message != null);

        int productIndex = Integer.parseInt(input);
        productIndex -= 1;
        addProductBuyer(buyerIndex, sellerIndex, productIndex);

        Buyer buyer = managerBuyer.getBuyers().get(buyerIndex);
        Product product = managerSeller.getSellers().get(sellerIndex).getProducts().get(productIndex);
        Seller seller = managerSeller.getSellers().get(sellerIndex);
        sqlHelper.addProductToCartInDatabase(buyer, product, seller);

        System.out.println("Product added successfully to cart.");
    }

    public void case1() {
        input = UserInput.stringInput("Please enter seller name: ");
        message = ManagerSeller.getInstance().isExistSeller(input);
        if (message != null) {
            System.out.println(message);
        }

        String username = input;
        String password = null;
        try {
            // קודם נבדוק אם המשתמש קיים בטבלת users
            if (!sqlHelper.isUserExists(input)) {
                password = UserInput.stringInput("Please enter password:");
                sqlHelper.addNewUser(input, password); // מוסיף ל־users
            }

            sqlHelper.addNewSeller(input, password); // מוסיף ל־seller
            System.out.println("Seller '" + input + "' added successfully.");

        } catch (SQLException e) {
            System.out.println("Failed to add seller: " + e.getMessage());
        }
    }

    public void case2() {
        String username = UserInput.stringInput("Please enter buyer name: ");

        try {
            if (sqlHelper.isUserExists(username)) {
                System.out.println("This username already exists in the system.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Failed to check user existence: " + e.getMessage());
            return;
        }

        String password = UserInput.stringInput("Please enter password: ");

        System.out.println("Enter your full address: ");
        String street = UserInput.stringInput("Street: ");
        String houseNumber = UserInput.stringInput("House number: ");
        String city = UserInput.stringInput("City: ");
        String country = UserInput.stringInput("Country: ");

        Address address = new Address(street, houseNumber, city, country);

        try {
            sqlHelper.addNewUser(username, password);
            sqlHelper.addNewBuyer(username, password, address);
            System.out.println("Buyer added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add buyer: " + e.getMessage());
        }
    }


    public void case5() {
        int buyerIndex = managerBuyer.paymentForBuyer(sqlHelper.getLastID());
        sqlHelper.UpdateCart(buyerIndex);


    }

    public String case6() {
        return managerBuyer.buyersInfo().toString();
    }

    public String case7() {
        return managerSeller.sellersInfo().toString();

    }

    public String case8() {

        return managerProduct.productsByCategory().toString();

    }

    public void case9() {
        HashMap<Integer, Integer> mapTemp = managerBuyer.updateCartHistory();
        if (mapTemp != null) {
            Map.Entry<Integer, Integer> entry = mapTemp.entrySet().iterator().next();
            int buyerId = entry.getKey().intValue() + 1;
            int cartId = entry.getValue().intValue();
            sqlHelper.UpdateHistory(buyerId, cartId);

        } else {
            System.out.println("Error in Replace Cart");
        }
    }


    public void case_2() {
        System.out.println("Seller");
        managerSeller.showlist();
        System.out.println("Buyer");
        managerBuyer.showlist();
        System.out.println("product");
        managerProduct.show_list();
    }


}
