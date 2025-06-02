package Models;

import Enums.Category;

import java.util.Objects;

public class Product {
    private final String productName;
    private final double productPrice;
    protected int id;
    private final Category category;
    
    
    public Product(int id,String productName, double productPrice, Category category) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.id = id;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public Product(Product other) {
        this.productName = other.productName;
        this.productPrice = other.productPrice;
        this.id = other.id;
        this.category = other.category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productName.toLowerCase(), product.productName.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, productPrice);
    }

    public double getProductPrice() {
        return productPrice;
    }

    @Override
    public String toString() {
        return "Product name: " + productName +
                ", Product price: " + productPrice +
                ", Product id: " + id +
                ", Product category: " + category;
    }


}
