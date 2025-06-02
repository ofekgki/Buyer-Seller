package Models;

import Enums.Category;

import java.util.Arrays;

public class Categories {
    private Product[] electronics;
    private Product[] children;
    private Product[] office;
    private Product[] clothes;
    private int numElectronics;
    private int numChildren;
    private int numOffice;
    private int numClothes;
    private final int SIZE_INCREASE = 2;

    public Categories() {
        electronics = new Product[0];
        children = new Product[0];
        office = new Product[0];
        clothes = new Product[0];
    }


    public int getNumElectronics() {
        return numElectronics;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public int getNumOffice() {
        return numOffice;
    }

    public int getNumClothes() {
        return numClothes;
    }

    public void addElectronic(Product product) {
        electronics = extendCategoryArray(electronics, getNumElectronics(), product);
        numElectronics++;
    }

    public void addChild(Product product) {
        children = extendCategoryArray(children, getNumChildren(), product);
        numChildren++;
    }

    public void addOffice(Product product) {
        office = extendCategoryArray(office, getNumOffice(), product);
        numOffice++;
    }

    public void addClothes(Product product) {
        clothes = extendCategoryArray(clothes, getNumClothes(), product);
        numClothes++;
    }

    private Product[] extendCategoryArray(Product[] category, int numOfProductsInCategory, Product product) {
        if (category.length == numOfProductsInCategory) {
            if (category.length == 0) {
                category = Arrays.copyOf(category, 1);
            } else {
                category = Arrays.copyOf(category, category.length * SIZE_INCREASE);
            }
        }
        category[numOfProductsInCategory] = product;
        return category;
    }

    public static String categoriesByNames() {
        Category[] allCategories = Category.values();
        StringBuilder sb = new StringBuilder("\nCategory list:\n--------------\n");
        for (Category category : allCategories) {
            sb.append(category.ordinal() + 1).append(") ").append(category.name()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Electronics: \n");
        if (numElectronics > 0) {
            for (int i = 0; i < numElectronics; i++) {
                sb.append(i + 1).append(") ").append(electronics[i].toString()).append("\n");
            }
        } else {
            sb.append("No electronics products yet.\n");
        }
        sb.append("\nChildren: \n");
        if (numChildren > 0) {
            for (int i = 0; i < numChildren; i++) {
                sb.append(i + 1).append(") ").append(children[i].toString()).append("\n");
            }
        } else {
            sb.append("No children products yet.\n");
        }
        sb.append("\nOffice: \n");
        if (numOffice > 0) {
            for (int i = 0; i < numOffice; i++) {
                sb.append(i + 1).append(") ").append(office[i].toString()).append("\n");
            }
        } else {
            sb.append("No office products yet.\n");
        }
        sb.append("\nClothes: \n");
        if (numClothes > 0) {
            for (int i = 0; i < clothes.length; i++) {
                sb.append(i + 1).append(") ").append(clothes[i].toString()).append("\n");
            }
        } else {
            sb.append("No clothes products yet.\n");
        }
        return sb.toString();
    }
}
