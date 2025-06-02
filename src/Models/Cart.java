package Models;

import java.util.Arrays;
import java.util.Date;

public class Cart {
    private static final int SIZE_INCREASE = 2;
    private Product[] products;
    private int numOfProducts;
    private final Date date;
    private Double totalPrice;

    public Date getDate() {
        return date;
    }

    public int getNumOfProducts() {
        return numOfProducts;
    }

    public Cart(Cart other) {    // copy constructor cart
        products = other.products;
        date = new Date();
        totalPrice = other.totalPrice;
        numOfProducts = other.numOfProducts;
    }

    public Cart() {
        this.products = new Product[0];
        this.date = new Date();
        this.totalPrice = 0.0;
        this.numOfProducts = 0;
    }

    public void addProductToCart (Product p1) {
        if (numOfProducts == products.length) {
            if (numOfProducts == 0) {
                products = Arrays.copyOf(products, 1);
            } else {
                products = Arrays.copyOf(products, products.length * SIZE_INCREASE);
            }
        }
        products[numOfProducts++] = p1;
        totalPrice += p1.getProductPrice();
        if (p1 instanceof ProductSpecialPackage) {
            totalPrice += ((ProductSpecialPackage) p1).getSpecialPackagePrice();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cart details: \n");
        for (int i = 0; i < numOfProducts; i++) {
            sb.append("   ").append(i + 1).append(") ").append(products[i].toString()).append("\n");
        }
        sb.append("------------------\n").append("total : ").append(totalPrice)
                .append("\n------------------\n");
        return sb.toString();
    }
}
