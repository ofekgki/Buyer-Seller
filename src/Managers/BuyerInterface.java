package Managers;

import Models.Buyer;

public interface BuyerInterface {

	String isExistBuyer (String name);
	
	String chooseValidBuyer(String indexInput);
	
	 void addBuyer(Buyer buyer );

	    
	  String buyersInfo();
	    
	  String buyersNames();
	  
	  
	  
	  String pay(int buyerIndex, int lastID);
	  
	  int replaceCarts(int historyCartIndex, int buyerIndex);
	  
	    String isValidHistoryCartIndex(String indexCartInput, int buyerIndex);

	
}
