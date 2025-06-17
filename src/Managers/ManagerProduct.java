package Managers;

import java.util.*;

import Enums.Category;
import Enums.ExceptionsMessages;
import Models.Categories;
import Models.Product;
import Models.ProductSpecialPackage;

public class ManagerProduct implements ProductInterface {


    private final Categories categoriesArrays;

    ArrayList<Product> products_list = new ArrayList<>();

    private static ManagerProduct instance;

    public static ManagerProduct getInstance() {
        if (instance == null)
            instance = new ManagerProduct();
        return instance;
    }

    private ManagerProduct() {

        categoriesArrays = new Categories();

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

        // Create a Sorted Copy Of the array
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

    public void addtolist(Product product) {
        products_list.add(product);
    }



}
