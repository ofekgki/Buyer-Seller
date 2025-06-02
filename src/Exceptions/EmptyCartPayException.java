package Exceptions;

public class EmptyCartPayException extends Exception{
    public EmptyCartPayException(String name) {
        super(name + "'s cart is empty, cannot proceed to checkout!");
    }
}
