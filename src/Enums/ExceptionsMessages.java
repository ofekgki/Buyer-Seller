package Enums;

public enum ExceptionsMessages {
    INVALID_NUMBER_CHOICE("\nChoice must to be digit, please try again!\n"),
    INVALID_BUYER_INDEX("\nBuyer number is NOT exist, PLEASE choose from the RANGE!\n"),
    INVALID_SELLER_INDEX("\nSeller number is NOT exist, PLEASE choose from the RANGE!\n"),
    INVALID_HISTORY_CART_INDEX("\nHistory cart number is NOT exist, PLEASE choose from the RANGE!\n"),
    INVALID_CATEGORY_INDEX("\nCategory number is NOT exist, PLEASE choose from the RANGE!\n"),
    INVALID_PRODUCT_INDEX("\n Product number is NOT exist, PLEASE choose from the RANGE!\n"),
    INVALID_PRICE_VALUE("\nPrice cannot be zero or negative, please try again!\n"),
    PRICE_EMPTY("\nPrice cannot be empty, please try again!\n"),
    INVALID_PRICE_INPUT("\nPrice must enter as number, please try again!\n");

    private final String exceptionMessage;


    ExceptionsMessages(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
