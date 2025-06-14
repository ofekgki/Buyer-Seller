package Models;

import Managers.SQL_HELPER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Cart {

    private ArrayList<Product> products = new ArrayList<>();
    private final Date date;
    private Double totalPrice;
    private int id;

    public Date getDate() {
        return date;
    }

    public int getId(){
        return id;
    }

    public Cart(Cart other, int FromHistory) {    // copy constructor cart
        products = other.products;
        date = new Date();
        totalPrice = other.totalPrice;
        if (FromHistory == 1) {
            id = SQL_HELPER.getInstance().getLastCartID();

        }
        else {
            id = other.getId();
        }
    }


    public Cart(int id) {
        this.date = new Date();
        this.totalPrice = 0.0;
        this.id = id;
    }

    public void addProductToCart (Product p1) {

        products.add(p1);
        totalPrice += p1.getProductPrice();
        if (p1 instanceof ProductSpecialPackage) {
            totalPrice += ((ProductSpecialPackage) p1).getSpecialPackagePrice();
        }
    }

    public int getSize(){
        return products.size();
    }

    public double getTotalPrice(){
        return totalPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cart details: \n");
        sb.append("Cart ID :")
                .append(getId())
                .append("\n");
        for (int i = 0; i < products.size(); i++) {
            sb.append("   ").append(i + 1).append(") ")
                    .append(products.get(i).toString()).append("\n");
        }
        sb.append("total : ").append(String.format("%.2f", totalPrice))
                .append("\n------------------\n");
        return sb.toString();
    }
}
