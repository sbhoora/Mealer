public class CreditCard {
    private int cardNumber;
    private int cvv;
    private int exp;
    private String cardName;

    public CreditCard (String cardName, int cardNumber, int cvv, int exp) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.exp = exp;
    }

    public String getCardName() {
        return cardName;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public int getExp() {
        return exp;
    }

    public int[] paymentInfo() {
        int[] info = new int[2];
        info[0] = getCardNumber();
        info[1] = getCvv();
        info[2] = getExp();
        
    }

}
