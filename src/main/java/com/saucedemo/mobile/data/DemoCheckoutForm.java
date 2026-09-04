package com.saucedemo.mobile.data;

public final class DemoCheckoutForm {

    private final String fullName;
    private final String addressLineOne;
    private final String city;
    private final String stateRegion;
    private final String zipCode;
    private final String country;
    private final String cardFullName;
    private final String cardNumber;
    private final String expirationDate;
    private final String securityCode;

    private DemoCheckoutForm(
            String fullName,
            String addressLineOne,
            String city,
            String stateRegion,
            String zipCode,
            String country,
            String cardFullName,
            String cardNumber,
            String expirationDate,
            String securityCode) {
        this.fullName = fullName;
        this.addressLineOne = addressLineOne;
        this.city = city;
        this.stateRegion = stateRegion;
        this.zipCode = zipCode;
        this.country = country;
        this.cardFullName = cardFullName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
    }

    public static DemoCheckoutForm rebeccaWinter() {
        return new DemoCheckoutForm(
                "Rebecca Winter",
                "Mandorley 112",
                "Truro",
                "Cornwall",
                "89750",
                "United Kingdom",
                "Rebecca Winter",
                "325812657568789",
                "03/30",
                "123");
    }

    public String fullName() {
        return fullName;
    }

    public String addressLineOne() {
        return addressLineOne;
    }

    public String city() {
        return city;
    }

    public String stateRegion() {
        return stateRegion;
    }

    public String zipCode() {
        return zipCode;
    }

    public String country() {
        return country;
    }

    public String cardFullName() {
        return cardFullName;
    }

    public String cardNumber() {
        return cardNumber;
    }

    public String expirationDate() {
        return expirationDate;
    }

    public String securityCode() {
        return securityCode;
    }
}
