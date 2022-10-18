public class Client implements Account {
    String firstName;
    String lastName;
    String email;
    String password;
    String address;
    int creditCard;

    public Client(String f, String l, String e, String p, String address, int creditCard) {
        firstName = f;
        lastName = l;
        email = e;
        password = p;
        this.address = address;
        this.creditCard = creditCard;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType(){
        return "Client";
    }

    public String getAddress(){
        return address;
    }

    public int getCreditCard(){
        return creditCard;
    }

}
