package Comparators;

import Models.Seller;

import java.util.Comparator;

public class CompareSellersByProductsNumber implements Comparator<Seller> {
    @Override
    public int compare(Seller s1, Seller s2) {
        return s2.getNumOfProducts() - s1.getNumOfProducts();
    }
}
