public class Cook implements Account {
    String firstName;
    String lastName;
    String email;
    String password;
    String description;

    public Cook(String f, String l, String e, String p, String d) 
    {
        firstName = f;
        lastName = l;
        email = e;
        password = p;
        description = d;
        
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
        return "Cook";
    }

    public String getDescription() {
        return description;
    }

}
