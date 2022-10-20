public class Address {
    private String street;
    private int number;
    private String postal;
    private String country;
    private String province;
    private String city;

    public Address(String street, int number, String postal, String country, String province, String city){
        this.street = street;
        this.number = number;
        this.postal = postal;
        this.country = country;
        this.province = province;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    public String getPostal() {
        return postal;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String toString(){
        String a = (getNumber() + " " + getStreet() + " St., "  + getCity() + " " + getProvince() + " " + getPostal(), + " " + getCountry());
        return a;
    }
}
