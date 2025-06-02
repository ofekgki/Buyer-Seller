package Managers;

import Models.Seller;

public interface SellerInterface {

	
	
	
	 String isExistSeller (String name);
	 
	  String chooseValidSeller(String indexInput);
	  
	  void addSeller(Seller seller);
	  
	  String sellersInfo();
	  
	  String sellersNames();
	 	
	
}
