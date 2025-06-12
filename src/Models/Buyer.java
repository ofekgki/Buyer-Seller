package Models;

import java.util.ArrayList;

public class Buyer extends User {

    private final Address address;

    public int getBuyer_id() {
        return buyer_id;
    }

    private Cart currentCart;
    private ArrayList<Cart> historyCart = new ArrayList<>();
    private int buyer_id;

    public Buyer(int id, String userName, String password, Address address) {
        super(userName, password);
        this.buyer_id = id;
        this.address = address;
    }

    public int getHistoryCartsNum() {
        return historyCart.size();
    }

    public ArrayList<Cart> getHistoryCart() {
        return historyCart;
    }

    public void setCurrentCart(Cart currentCart) {
        this.currentCart = new Cart(currentCart, 0);
    }

    public Cart getCurrentCart() {
        return currentCart;
    }

    public void payAndMakeHistoryCart(int lastID) {
        Cart hCart = new Cart(currentCart, 0);
        historyCart.add(hCart);
        currentCart = new Cart(lastID);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Buyer details:\n")
                .append("   Name: ").append(userName).append("\n")
                .append("   Address: ").append(address.toString()).append("\n\n");


        if (currentCart == null || currentCart.getSize() == 0) {
            sb.append("Cart is empty\n");
        } else {
            sb.append(currentCart.toString());
        }


        if (historyCart == null || historyCart.isEmpty()) {
            sb.append("No history carts.\n\n");
        } else {
            sb.append("\nHistory carts details:\n");
            for (int i = 0; i < historyCart.size(); i++) {
                sb.append(i + 1)
                        .append(") ")
                        .append(historyCart.get(i).toString())
                        .append("Date: ")
                        .append(historyCart.get(i).getDate())
                        .append("\n\n");
            }
        }

        return sb.toString();
    }


    public void createNewCart(int lastID) {
        this.currentCart = new Cart(lastID);
    }

    public void deleteCart() {
        this.currentCart = null;
    }


    public void setCurrentCartFromHistory(Cart cart) {
        this.currentCart = new Cart(cart, 1);
    }
}


