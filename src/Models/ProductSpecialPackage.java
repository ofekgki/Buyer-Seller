package Models;

import Enums.Category;

public class ProductSpecialPackage extends Product {
    private final double specialPackagePrice;

    public ProductSpecialPackage(String productName, double productPrice, Category category, double specialPackagePrice) {
        super(productName, productPrice, category);
        this.specialPackagePrice = specialPackagePrice;
    }

    public ProductSpecialPackage(Product other, double specialPackagePrice) {
        super(other);
        this.specialPackagePrice = specialPackagePrice;
    }

    public double getSpecialPackagePrice() {
        return specialPackagePrice;
    }

    @Override
    public String toString() {
        return super.toString() + ", Product have a special package, additional price is: " + specialPackagePrice;
    }
}
