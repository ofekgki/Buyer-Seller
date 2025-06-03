package Models;

import java.util.*;

public class Seller extends User {

    private List<Product> products = new ArrayList<>();
    private int numOfProducts;
    private int seller_id;

    public Seller(int id, String userName, String password) {
        super(userName, password);
        seller_id = id;
        numOfProducts = 0;
    }

    public int getNumOfProducts() {
        return numOfProducts;
    }

    public List<Product>  getProducts() {
        return products;
    }

    public void addProduct(Product p1) {
        products.add(p1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (products.isEmpty()) {
            sb.append("\nNo products yet.\n");
            return sb.toString();
        }

        sb.append("\nSeller products:\n");
        for (int i = 0; i < products.size(); i++) {
            sb.append("   ").append(i + 1).append(") ")
                    .append(products.get(i).toString()).append('\n');
        }

        return sb.toString();
    }

    public int getSeller_id() {
        return seller_id;
    }
}

