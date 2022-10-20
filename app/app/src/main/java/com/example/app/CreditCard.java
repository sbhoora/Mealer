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

    public String[] paymentInfo() {
        String[] info = new int[2];
        info[0] = getCardNumber();
        info[1] = getCvv();
        info[2] = getExp();

        return info;
        
    }

}
