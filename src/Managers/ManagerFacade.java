package Managers;

import Enums.Category;
import Enums.ExceptionsMessages;
import Factory.FactoryCommands;
import Factory.FactoryProduct;
import Factory.FactoryUser;
import Models.*;

import java.util.*;

public class ManagerFacade implements Manageable {

    private final ManagerBuyer managerBuyer;
    private final ManagerSeller managerSeller;
    private final ManagerProduct managerProduct;
    private static Memento memento;
    ArrayList<?> array_q18;
    private String input;
    private String message;

    private final FactoryProduct fPro;
    private final FactoryUser fUser;
    private final FactoryCommands fCommands;

    private static ManagerFacade instance;


    private ManagerFacade() {

        array_q18 = new ArrayList<>();
        managerBuyer = ManagerBuyer.getInstance();
        managerSeller = ManagerSeller.getInstance();
        managerProduct = ManagerProduct.getInstance();
        managerProduct.attach(new Action1());
        managerProduct.attach(new Action2());
        fPro = new FactoryProduct();
        fUser = new FactoryUser();
        fCommands = new FactoryCommands();

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
        System.out.println("10) Run Tests");
        System.out.println("99)  Q15");
        System.out.println("100) Q16");
        System.out.println("101) Q17:");
        System.out.println("102) Q18:");
        System.out.println("103) Q19:");
        System.out.println("104) Create Memento:");
        System.out.println("105) Set Memento:");
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
//        Product p1;
//        if (managerSeller.getSellers()[sellerIndex].getProducts()[productIndex] instanceof ProductSpecialPackage) {
//            p1 = fPro.createProduct(managerSeller.getSellers()[sellerIndex].getProducts()[productIndex].getProductName(),
//                    managerSeller.getSellers()[sellerIndex].getProducts()[productIndex].getProductPrice(),
//                    managerSeller.getSellers()[sellerIndex].getProducts()[productIndex].getCategory().ordinal(),
//                    ((ProductSpecialPackage) managerSeller.getSellers()[sellerIndex].getProducts()[productIndex]).getSpecialPackagePrice());
//
//        } else {
//
//            p1 = fPro.createProduct(managerSeller.getSellers()[sellerIndex].getProducts()[productIndex].getProductName(),
//                    managerSeller.getSellers()[sellerIndex].getProducts()[productIndex].getProductPrice(),
//                    managerSeller.getSellers()[sellerIndex].getProducts()[productIndex].getCategory().ordinal(),
//                    0);
//
//        }
//        managerBuyer.getBuyers()[buyerIndex].getCurrentCart().addProductToCart(p1);
    }

    public ArrayList<String> toArrayList(Map<String, Integer> map) {
        ArrayList<String> arr = new ArrayList<>();
        Set<String> set = map.keySet();
        Iterator<String> it2 = set.iterator();
        while (it2.hasNext()) {
            arr.add(it2.next());
        }
        return arr;
    }

    public void printByListIterator(List<?> lst) {

        //ListIterator<?> it = lst.listIterator(lst.size());
        IteratorTarget lit = new ListIteratorAdapter(lst.listIterator(lst.size()));
        while (lit.myHasPrevious()) {
            System.out.println(lit.myPrevious());
            lit.myNext();
            System.out.println(lit.myPrevious());
        }
    }

//    public SortedSet<String> ToTreeSet() {
//        SortedSet<String> sortedSet = new TreeSet<>((o1, o2) -> {
//            String lowerO1 = o1.toLowerCase();
//            String lowerO2 = o2.toLowerCase();
//
//            if (lowerO1.equals(lowerO2)) {
//                return 0;
//            }
//            int lengthDiff = lowerO1.length() - lowerO2.length();
//            if (lengthDiff != 0) {
//                return lengthDiff;
//            }
//            return lowerO1.compareTo(lowerO2);
//        });
//        for (int i = 0; i < managerProduct.getLogicProductsSize(); i++) {
//            sortedSet.add(managerProduct.getAllProducts()[i].getProductName().toUpperCase());
//        }
//        return sortedSet;
//    }

    public void addProductSeller() {
        int id =0; /// Kמחוק!
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


//    public void printArrayByOrder() { //q15
//        if (managerProduct.getAllProducts().length != 0)
//            managerProduct.printArray();
//        else {
//            System.out.println("Nothing to use please enter products");
//        }
//    }

//    public void printArrayByMap() { //q16
//        if (managerProduct.getAllProducts().length != 0) {
//            Map<String, Integer> map = fPro.toLinkedHashMap(managerProduct.getAllProducts());
//            map.forEach((String, Integer) -> {
//                System.out.println(String.format("%-4s........%4d", String, map.get(String)));
//            });
//        } else {
//            System.out.println("Nothing to use please enter products");
//        }
//
//    }

//    public void printCountOfItemInMap() { //q17
//        if (managerProduct.getAllProducts().length != 0) {
//
//            String name = UserInput.stringInput("Please enter the string:").toLowerCase();
//            Map<String, Integer> map = fPro.toLinkedHashMap(managerProduct.getAllProducts());
//            if (map.containsKey(name)) {
//                System.out.println(
//                        "The number of times '" + name + "' appears in the original array is " + map.get(name));
//            } else
//                System.out.println("the '" + name + "' is not exist");
//        } else {
//            System.out.println("Nothing to use please enter products");
//        }
//    }
//
//    public void printItemTwice() { //q18
//        if (managerProduct.getAllProducts().length != 0) {
//            array_q18 = createArray_q18();
//            printByListIterator(array_q18);
//        } else {
//            System.out.println("Nothing to use please enter products");
//            return;
//        }
//        String input = UserInput.stringInput("Do you want to see the output of my self-implemented iterators (Y/y or any other key to skip)");
//        if (input.equalsIgnoreCase("y")) {
//
//            System.out.println("print array by order with my Iterator: ");
//            printByConcerteIterator();
//
//            System.out.println("print array by order with my ListIterator: ");
//
//            printWithMyListIterator();
//
//
//        }
//    }
//
//    public void printWithMyListIterator() {
//
//        ListIterator<?> it = managerProduct.listIterator();
//        while (it.hasNext())
//            System.out.println(it.next());
//        managerProduct.myNotify("My ListIterator Ended!");
//        System.out.println("-----------------------------------");
//        System.out.println("print array from opposite order:  ");
//
//        while (it.hasPrevious())
//            System.out.println(it.previous());
//        managerProduct.myNotify("My ListIterator Ended!");
//
//    }
//
//    public void printByConcerteIterator() {
//
//        Iterator<?> it = managerProduct.iterator();
//        while (it.hasNext())
//            System.out.println(it.next());
//        managerProduct.myNotify("My Iterator Ended!");
//
//
//    }
//
//    public void printSortedWithLambda() { //q19
//        if (managerProduct.getAllProducts().length != 0) {
//            SortedSet<? extends String> sortSet = fPro.ToTreeSet(managerProduct.getLogicProductsSize(),
//                    managerProduct.getAllProducts());
//            for (String s : sortSet)
//                System.out.println(s);
//
//        } else {
//            System.out.println("Nothing to use please enter products");
//        }
//    }

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

        //sql_helper.addSeller(username,password);
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
//
//    public ArrayList<?> createArray_q18() {
//        Map<String, Integer> map = fPro.toLinkedHashMap(managerProduct.getAllProducts());
//        ArrayList<?> arr = toArrayList(map);
//        return arr;
//
//    }

//    public void case104() {
//
//        if (managerProduct.getAllProducts().length != 0) {
//            array_q18 = createArray_q18();
//            memento = createMemento();
//            System.out.println(memento);
//        } else {
//            System.out.println("Nothing to save ,please enter products");
//        }
//    }

    public void case105() {

        if (memento != null) {
            setMemento(memento);
            System.out.println(memento);
        } else {

            System.out.println("Nothing to undo");
        }
    }


    public static class Memento {
        private final ArrayList<?> array_q18;

        @Override
        public String toString() {
            return "Memento of ArrayList: = " + array_q18 + " ";
        }

        private Memento(ArrayList<?> array_q18) {
            this.array_q18 = new ArrayList<>(array_q18);
        }

    }

    public Memento createMemento() {
        return new Memento(array_q18);
    }

    public void setMemento(Memento m) {
        this.array_q18 = new ArrayList<>(array_q18);
    }

    public void actionByCommand(int action){
        fCommands.createCommand(action).execute();
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
