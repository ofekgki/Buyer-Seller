package Managers;

import Models.Product;

public interface ProductInterface {
	
	
	String validPrice(String priceInput);
	
	String productsByCategory();
	
	 void addToCategoryArray(Product p);
	 
	  String validCategoryIndex(String categoryInput);
	  
	  

}
