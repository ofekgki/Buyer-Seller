package Models;

import java.util.Arrays;

public class Buyer extends User {

    private static final int SIZE_INCREASE = 2;
    private final Address address;

    public int getBuyer_id() {
        return buyer_id;
    }

    private Cart currentCart;
    private Cart[] historyCart;
    private int historyCartsNum;
    private int buyer_id;

    public Buyer(int id,String userName, String password, Address address) {
        super(userName, password);
        this.buyer_id = id;
        this.address = address;
        currentCart = new Cart();
        historyCart = new Cart[0];
    }

    public int getHistoryCartsNum() {
        return historyCartsNum;
    }

    public Cart[] getHistoryCart() {
        return historyCart;
    }

    public void setCurrentCart(Cart currentCart) {
        this.currentCart = currentCart;
    }

    public Cart getCurrentCart() {
        return currentCart;
    }

    public void payAndMakeHistoryCart(){
        Cart hCart = new Cart(currentCart);
        if (historyCart.length == historyCartsNum) {
            if (historyCart.length == 0) {
                historyCart = Arrays.copyOf(historyCart, 1);
            }
            else {
                historyCart = Arrays.copyOf(historyCart, historyCart.length * SIZE_INCREASE);
            }
        }
        historyCart[historyCartsNum++] = hCart;
        currentCart = new Cart();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Buyer details:\n").append("   Name: ").append(userName).append("\n")
                .append("   Address: ").append(address.toString()).append("\n\n");
        if (currentCart.getNumOfProducts() == 0) {
            sb.append("Cart is empty\n");
        } else {
            sb.append(currentCart.toString());
        }
        if (historyCartsNum == 0) {
            return sb.append("No history carts.\n\n").toString();
        }
        sb.append("\nHistory carts details : \n");
        for (int i = 0; i < historyCartsNum; i++) {
            sb.append(i+1).append(") ").append(historyCart[i].toString())
                    .append(historyCart[i].getDate()).append("\n\n");
        }
        return sb.toString();
    }
}


