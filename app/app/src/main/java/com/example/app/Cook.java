package com.example.app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cook extends Account {

    // attributes
    private Address address;
    private String description;
    private Boolean suspended = false;
    private Boolean banned = false;
    private Date suspendedUntil;
    private Menu menu;

    // Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference cookReference = database.getReference("Accounts").child("Cooks");

    public Cook(String first, String last, String mail, String pass, Address address, String description) 
    {
        super(first, last, mail, pass);
        this.address = address;
        this.description = description;
    }

    public Cook(String email, String password, Boolean suspended, Boolean banned) {
        super(email, password);
        this.suspended = suspended;
        this.banned = banned;
    }

    /**
     * Saves a {@link Menu} to the Cook using {@link DatabaseReference}.
     * Any changes to a menu are only saved to the database once this method has been called.
     * Therefore, to update a cook's menu, it is necessary to 1) get the menu from the database, 2)
     * create a {@link Menu} object, 3) add changes to that Menu object and finally
     * 4) call the save(Menu) method on the Cook object.
     * @param menu
     */
    public void save(Menu menu) {
        cookReference.child(getEmail()).child("Menu").setValue(menu)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("Firebase", "Menu saved to cook with email: " + getEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Failed to save Menu to cook " +
                                "with email: " + getEmail());
                    }
                });
    }

    public void removeSuspension() {suspended = false;};

    public void ban() {banned = true;};

    public Menu getMenu() {
        cookReference.child("Menu").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                MenuItem menuItems[] = new MenuItem[Math.toIntExact(dataSnapshot.getChildrenCount())];
                HashMap<String, MenuItem> m = new HashMap<String, MenuItem>();

                //Log.i("SIZE", Integer.toString(menuItems.length));

                Map<String, Object> menuMap = (Map<String, Object>) dataSnapshot.getValue();
                if (menuMap != null) {
                    int i = 0;
                    //creating a list of complaints that exist on the DB
                    for (Map.Entry<String, Object> entry : menuMap.entrySet()) {
                        Map item = (Map) entry.getValue();
                        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(item.get("ingredients").toString().split(",")));
                        ArrayList<String> allergens = new ArrayList<>(Arrays.asList(item.get("allergens").toString().split(",")));

                        menuItems[i] = new MenuItem(item.get("name").toString(), Types.valueOf(item.get("type").toString()),
                                CuisineTypes.valueOf(item.get("cuisineType").toString()),ingredients,allergens,
                                Double.parseDouble(item.get("price").toString()),item.get("description").toString());

                        m.put(item.get("name").toString(),menuItems[i]);
                        i++;
                        //String name, Types type, CuisineTypes cuisineType, ArrayList<String> ingredients,
                        //    ArrayList<String> allergens, Double price, String description
                    }
                    menu = new Menu(m);
                } else {
                    menu = new Menu();
                }
            }
        });
        return menu;
    }

    private void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getAccountType(){
        return "Cook";
    }

    public Address getAddress(){
        return address;
    }

    public String getDescription() {
        return description;
    }

    public Date getSuspendedUntil() {return suspendedUntil;};

    // Setters
    public void suspend(Date suspendedUntil) {
        this.suspendedUntil = suspendedUntil;
        suspended = true;
    }

    public Boolean isSuspended() { return suspended;};

    public Boolean isBanned() { return banned; };
}
