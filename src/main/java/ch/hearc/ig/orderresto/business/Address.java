package ch.hearc.ig.orderresto.business;

public class Address extends AbstractRestoObject {

    private String countryCode;
    private String postalCode;
    private String locality;
    private String street;
    private String streetNumber;

    public Address(String countryCode, String postalCode, String locality, String street, String streetNumber) {
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.locality = locality;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLocality() {
        return locality;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    @Override
    public String toString() {
        return street + " " + streetNumber + ", " + postalCode + " " + locality + " " + countryCode;
    }
}