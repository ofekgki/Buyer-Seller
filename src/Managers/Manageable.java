package Managers;

import Enums.Category;
import Models.Address;
import Models.Product;


public interface Manageable {

    String validProductIndex(int sellerIndex, String productIndexInput);

    public void addProductSeller();

    public void addProductToBuyer();

    public void addProductToSeller(Product p1, int sellerIndex);


    public  void printMenu();

    public void addProductBuyer(int buyerIndex, int sellerIndex, int productIndex);
    

   

 

  
    }
