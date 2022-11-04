package com.example.app;

public class CreditCard {
    private String cardNumber;
    private String cvv;
    private String exp;
    private String cardName;

    public CreditCard (String cardName, String cardNumber, String cvv, String exp) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.exp = exp;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExp() {
        return exp;
    }

    public static boolean isValidCVV(String cvv){
        return cvv.length() == 3;
    }
    public static boolean isValidExpiration(String exp){
        return exp.length() == 4;
    }

}
