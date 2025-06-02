package Factory;

import Enums.Category;
import Models.Product;
import Models.ProductSpecialPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class FactoryProduct {
    private String input;


    public Product createProduct(int id,String productName, double productPrice, int categoryIndex, double specialPackagePrice) {

         
//         if(specialPackagePrice != 0  )
//
//         return new ProductSpecialPackage(id,productName, productPrice, Category.values()[categoryIndex], specialPackagePrice);
//
//         else {
//       	  return new Product(id,productName, productPrice, Category.values()[categoryIndex - 1]);
//         }
		 return null;
         
     }


	 public Map<String, Integer> toLinkedHashMap( Product[] allProducts) {
	        Map<String, Integer> map = new HashMap<>();
	        for (Product p : allProducts) {
	            if (p != null) {
	                if (map.containsKey(p.getProductName().toLowerCase()))
	                    map.put(p.getProductName().toLowerCase(), map.get(p.getProductName().toLowerCase()) + 1);
	                else
	                    map.put(p.getProductName().toLowerCase(), 1);
	            }
	        }
	        return map;
	    }
	 
	 public SortedSet<String> ToTreeSet(int logicProductsSize, Product[] allProducts) {
	        SortedSet<String> sortedSet = new TreeSet<>((o1, o2) -> {
	            String lowerO1 = o1.toLowerCase();
	            String lowerO2 = o2.toLowerCase();

	            if (lowerO1.equals(lowerO2)) {
	                return 0;
	            }
	            int lengthDiff = lowerO1.length() - lowerO2.length();
	            if (lengthDiff != 0) {
	                return lengthDiff;
	            }
	            return lowerO1.compareTo(lowerO2);
	        });
	        for (int i = 0; i < logicProductsSize; i++) {
	            sortedSet.add(allProducts[i].getProductName().toUpperCase());
	        }
	        return sortedSet;
	    }





	

}
