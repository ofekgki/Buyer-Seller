package Managers;

import Enums.Category;
import Enums.ExceptionsMessages;
import Factory.FactoryProduct;
import Factory.FactoryUser;
import Models.*;


import java.util.*;

public class ManagerFacade implements Manageable {

    private final ManagerBuyer managerBuyer;
    private final ManagerSeller managerSeller;
    private final ManagerProduct managerProduct;
    private String input;
    private String message;

    private final FactoryProduct fPro;
    private final FactoryUser fUser;

    private static ManagerFacade instance;


    private ManagerFacade() {
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

        Product selectedProduct = managerSeller.getSellers()
                .get(sellerIndex)
                .getProducts()
                .get(productIndex);

        if (selectedProduct instanceof ProductSpecialPackage) {
            p1 = fPro.createProduct(
                    selectedProduct.getProductId(),
                    selectedProduct.getProductName(),
                    selectedProduct.getProductPrice(),
                    selectedProduct.getCategory().ordinal(),
                    ((ProductSpecialPackage) selectedProduct).getSpecialPackagePrice()
            );
        } else {
            p1 = fPro.createProduct(
                    selectedProduct.getProductId(),
                    selectedProduct.getProductName(),
                    selectedProduct.getProductPrice(),
                    selectedProduct.getCategory().ordinal(),
                    0
            );
        }

        managerBuyer.getBuyers()
                .get(buyerIndex)
                .getCurrentCart()
                .addProductToCart(p1);
    }



    public void addProductSeller() {
        int id =0; ///
        if (managerSeller.getNumberOfSellers() == 0) {
            System.out.println("Haven't sellers yet, cannot be proceed. return to Menu.");
            return;
        }
        int sellerIndex = managerSeller.chooseSeller();
        if (sellerIndex == -1)
            return;

        do input = UserInput.stringInput("Enter product name to add:");
        while (input.isEmpty());

        String productName = input;

        String msg;
        do {
            input = UserInput.stringInput("Enter product price:");

            msg = ManagerProduct.getInstance().validPrice(input);
            if (msg != null) {
                System.out.println(msg);
            }
        } while (msg != null);
        double productPrice = Double.parseDouble(input);
        System.out.println(Categories.categoriesByNames());

        do {
            input = UserInput.stringInput("Choose category: ");

            msg = ManagerProduct.getInstance().validCategoryIndex(input);
            if (msg != null) {
                System.out.println(msg);
            }
        } while (msg != null);
        int categoryIndex = Integer.parseInt(input);
        double specialPackagePrice = 0;
        do {
            input = UserInput.stringInput("This product have special package? YES / NO");

            if (input.equalsIgnoreCase("yes")) {

                do {
                    input = UserInput.stringInput("Enter price for special package:");

                    msg = ManagerProduct.getInstance().validPrice(input);
                    if (msg != null) {
                        System.out.println(msg);
                    }
                } while (msg != null);
                specialPackagePrice = Double.parseDouble(input);
                break;
            }
            if (!input.equalsIgnoreCase("no")) {
                System.out.println("Please enter YES / NO only !");
            }
        } while (!input.equalsIgnoreCase("no"));


        Product p1 = fPro.createProduct(id,productName, productPrice, categoryIndex, specialPackagePrice);

        addProductToSeller(p1, sellerIndex);
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

            message = validProductIndex(sellerIndex, input); // כבר הותאמה ל-ArrayList
            if (message != null) {
                System.out.println(message);
            }
        } while (message != null);

        int productIndex = Integer.parseInt(input);
        addProductBuyer(buyerIndex, sellerIndex, productIndex - 1); // הנחה: הפונקציה כבר תומכת ב-ArrayList
        System.out.println("Product added successfully to cart.");
    }


    public void addProductSeller(int id, int sellerIndex, String productName, int productPrice, Category category,
                                 int specialPackagePrice) {

        Product product;

        if (specialPackagePrice != 0) {
            product = new ProductSpecialPackage(id, productName, productPrice, category, specialPackagePrice);
        } else {
            product = new Product(id, productName, productPrice, category);
        }

        // הוספה למוכר
        managerSeller.getSellers().get(sellerIndex).addProduct(product);

        // הוספה לרשימת מוצרים כללית
        managerProduct.getAllProducts().add(product);

        // הוספה לרשימת קטגוריה
        managerProduct.addToCategoryArray(product);
    }


    public void case1() {

        input = UserInput.stringInput("Please enter seller name: ");
        message = ManagerSeller.getInstance().isExistSeller(input);
        if (message != null) {
            System.out.println(message);
        }

        String username = input;
        String password = UserInput.stringInput("Please enter password:");
        int last_id = managerSeller.getLastId();
        //sql_helper.addSeller(new Seller(last_id,username,password));
        //managerSeller.addSeller(fUser.addSellerFromUser(username, password));

        System.out.println("Seller added successfully.");

    }

    public void case2() {

        String street;
        String houseNumber;
        String city;
        String country;

        input = UserInput.stringInput("Please enter buyer name: ");

        message = ManagerBuyer.getInstance().isExistBuyer(input);
        ;
        if (message != null) {
            System.out.println(message);
        }

        String username = input;

        String password = UserInput.stringInput("Please enter password: ");


        System.out.println("Enter your full address: ");
        do
            street = UserInput.stringInput("Street: ");
        while (input.isEmpty());

        do
            houseNumber = UserInput.stringInput("House number: ");
        while (input.isEmpty());

        do
            city = UserInput.stringInput("City:  ");
        while (input.isEmpty());

        do
            country = UserInput.stringInput("State: ");
        while (input.isEmpty());


        fUser.createAddress(street, houseNumber, city, country);

        managerBuyer.addBuyer(fUser.addBuyerFromUser(username, password, fUser.createAddress(street, houseNumber, city, country)));

        System.out.println("Buyer added successfully.");

    }

    public void case5() {
        managerBuyer.paymentForBuyer();

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

        managerBuyer.updateCartHistory();

    }

    public void case_2(){
        System.out.println("SEller");
        managerSeller.showlist();
        System.out.println("BUyer");
        managerBuyer.showlist();
        System.out.println("product");
        managerProduct.show_list();
    }



}
