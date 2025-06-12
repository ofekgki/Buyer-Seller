package Factory;

import Enums.Category;
import Models.Product;
import Models.ProductSpecialPackage;

public class FactoryProduct {
    private String input;



    public Product createSpecialProduct(String name, double price, int categoryIndex, double specialPrice) {
        Category category = Category.values()[categoryIndex];
        return new ProductSpecialPackage(name, price, category, specialPrice);
    }

    public Product createRegularProduct(String name, double price, int categoryIndex) {
        Category category = Category.values()[categoryIndex];
        return new Product(name, price, category);
    }
}
