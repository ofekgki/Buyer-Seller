package Models;

public class Address {
    private final String street;
    private final String houseNum;
    private final String city;
    private final String country;

    public Address(String street, String houseNum, String city, String country) {
        this.street = street;
        this.houseNum = houseNum;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString() {
        return street + " " + houseNum + " , " + city + " , " + country;
    }
}
