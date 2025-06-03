package Factory;

import Enums.Category;
import Models.Product;
import Models.ProductSpecialPackage;

public class FactoryProduct {
    private String input;


    public Product createProduct(int id,String productName, double productPrice, int categoryIndex, double specialPackagePrice) {

         
         if(specialPackagePrice != 0  )

         return new ProductSpecialPackage(id,productName, productPrice, Category.values()[categoryIndex], specialPackagePrice);

         else {
       	  return new Product(id,productName, productPrice, Category.values()[categoryIndex]);
         }

         
     }
 }
