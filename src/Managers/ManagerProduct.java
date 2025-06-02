package Managers;

import java.util.*;

import Enums.Category;
import Enums.ExceptionsMessages;
import Models.Categories;
import Models.Product;
import Models.ProductSpecialPackage;

public class ManagerProduct implements ProductInterface {


    private Set<Observer> set = new HashSet<>();

    private final Categories categoriesArrays;

    ArrayList<Product> products_list = new ArrayList<>();

    private int logicProductsSize = 0;

    private final int SIZE_INCREASE = 2;

    private static ManagerProduct instance;

    public static ManagerProduct getInstance() {
        if (instance == null)
            instance = new ManagerProduct();
        return instance;
    }

    private ManagerProduct() {

        categoriesArrays = new Categories();

    }

    public void attach(Observer o) {
        set.add(o);
    }

    public void myNotify(String msg) {
        for (Observer o : set)
            o.update(msg);
    }

    public int getLogicProductsSize() {
        return logicProductsSize;
    }

    public ArrayList<Product>  getAllProducts() {
        return products_list;
    }

    public String validPrice(String priceInput) {
        try {
            double price = Double.parseDouble(priceInput);
            if (price <= 0)
                throw new InputMismatchException(ExceptionsMessages.INVALID_PRICE_VALUE.getExceptionMessage());
        } catch (NullPointerException e) {
            return ExceptionsMessages.PRICE_EMPTY.getExceptionMessage();
        } catch (NumberFormatException e) {
            return ExceptionsMessages.INVALID_PRICE_INPUT.getExceptionMessage();
        } catch (InputMismatchException e) {
            return e.getMessage();
        }
        return null;
    }

    public double getTotalPrice(Product p){
        double totalPrice = 0;
            if(p instanceof ProductSpecialPackage)
                totalPrice = p.getProductPrice() +((ProductSpecialPackage) p).getSpecialPackagePrice();
            else
                totalPrice = p.getProductPrice();
        return totalPrice;
    }


    public String productsByCategory() {
        if (products_list.isEmpty()) {
            return "No products available.";
        }

        // יוצרים עותק ממוין של הרשימה לפי קטגוריה
        ArrayList<Product> sorted = new ArrayList<>(products_list);
        sorted.sort(Comparator.comparing(Product::getCategory));

        StringBuilder sb = new StringBuilder("\nProducts sorted by category:\n----------------------------\n");

        Category currentCategory = null;

        for (Product p : sorted) {
            if (currentCategory != p.getCategory()) {
                currentCategory = p.getCategory();
                sb.append("\nCategory: ").append(currentCategory).append("\n");
            }
            sb.append("- ").append(p.getProductName())
                    .append(String.format(" | Price: %.2f\n", getTotalPrice(p)))
                    .append("\n");
        }
        return sb.toString();
    }


    public String validCategoryIndex(String categoryInput) {
        try {
            int categoryChoice = Integer.parseInt(categoryInput);
            if (categoryChoice <= 0 || categoryChoice > Category.values().length)
                throw new IndexOutOfBoundsException(ExceptionsMessages.INVALID_CATEGORY_INDEX.getExceptionMessage());
        } catch (NumberFormatException e) {
            return ExceptionsMessages.INVALID_NUMBER_CHOICE.getExceptionMessage();
        } catch (IndexOutOfBoundsException e) {
            return e.getMessage();
        }
        return null;
    }


    public void addToCategoryArray(Product p) {
        switch (p.getCategory()) {
            case ELECTRONICS:
                categoriesArrays.addElectronic(p);
                break;
            case CHILDREN:
                categoriesArrays.addChild(p);
                break;
            case CLOTHES:
                categoriesArrays.addClothes(p);
                break;
            case OFFICE:
                categoriesArrays.addOffice(p);
                break;
            default:
                break;
        }
    }



//    public ListIterator<String> listIterator() {
//        return new MyListIterator(0);
//    }
//
//    public Iterator<String> iterator() {
//        return new ConcreteIterator();
//    }

    public void addtolist(Product product) {
        products_list.add(product);
    }

    public void show_list(){
        for (Product s : products_list) {
            System.out.println(s);
        }
    }

//    private class ConcreteIterator implements Iterator<String> {
//        int cur = 0;
//
//        @Override
//        public boolean hasNext() {
//            return cur < logicProductsSize;
//        }
//
//        @Override
//        public String next() {
//            if (!hasNext())
//                throw new NoSuchElementException();
//            String tmp = allProducts[cur].getProductName().toLowerCase();
//
//            cur++;
//            return tmp;
//        }
//
//        @Override
//        public void remove() {
//            throw new UnsupportedOperationException();
//
//        }
//
//    }
//
//
//    private class MyListIterator extends ConcreteIterator implements ListIterator<String> {
//
//        MyListIterator(int index) {
//            cur = index;
//        }
//
//        @Override
//        public boolean hasNext() {
//            return cur < logicProductsSize;
//        }
//
//
//        @Override
//        public boolean hasPrevious() {
//            return cur > 0;
//
//        }
//
//
//        @Override
//        public String previous() {
//            if (!hasPrevious())
//                throw new NoSuchElementException();
//            cur--;
//
//            String tmp = allProducts[cur].getProductName().toLowerCase();
//            return tmp;
//        }
//
//        @Override
//        public int nextIndex() {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        public int previousIndex() {
//            throw new UnsupportedOperationException();
//
//        }
//
//        @Override
//        public void set(String e) {
//            throw new UnsupportedOperationException();
//
//        }
//
//        @Override
//        public void add(String e) {
//            throw new UnsupportedOperationException();
//
//        }
//
//    }
//

}
