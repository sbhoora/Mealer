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

import java.io.Serializable;
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

    public Cook(String email) {
        super(email);
    }

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
        // for some reason, Android requires to create 1-element arrays
        // for this to use a variable that's defined outside the listener inside the listener.
        // If not, it shows red underlined.
        final Menu[] menu = new Menu[1];
        /*
        cookReference.child(getEmail()).child("Menu").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // Too simple to work but pray it will
                // The other implementation looked a little funky with the MenuItem array?
                // Didn't quite understand that. So I wanted to try this.
                // Obviously it looks deceptively simple, but who knows? might work.
                // Here's where I'm getting the idea from:
                // https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot#getValue(java.lang.Class%3CT%3E)
                // However the listener is skipped over for some reason, so wasn't able to test.
                if (task.isSuccessful()) {
                    Log.i("COOK", "PASS 1");
                    DataSnapshot snapshot = task.getResult();
                    //menu[0] = snapshot.getValue(Menu.class);
                    Log.i("COOK", String.valueOf(snapshot.child("notOfferedMeals").child("gross0").child("name").getValue()));
                }
            }
        });
        Log.i("COOK", "PASS 2");
        return menu[0];
         */

        cookReference.child(getEmail()).child("Menu").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.i("onComplete", "PASS");
                DataSnapshot snapshot = task.getResult();
                MenuItem menuItems[] = new MenuItem[Math.toIntExact(snapshot.getChildrenCount())];
                HashMap<String, MenuItem> m = new HashMap<String, MenuItem>();
                Map<String,Object> menuMap =  (Map<String,Object>) snapshot.getValue();

                //Log.i("SIZE", Integer.toString(menuItems.length));

                if (menuMap != null) {
                    int i = 0;

                    for (Map.Entry<String, Object> entry : menuMap.entrySet()) {
                        Map item = (Map) entry.getValue();
                        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(item.get("ingredients").toString().split(",")));
                        ArrayList<String> allergens = new ArrayList<>(Arrays.asList(item.get("allergens").toString().split(",")));

                        menuItems[i] = new MenuItem(item.get("name").toString(), Types.valueOf(item.get("type").toString()),
                                CuisineTypes.valueOf(item.get("cuisineType").toString()),ingredients,allergens,
                                Double.parseDouble(item.get("price").toString()),item.get("description").toString());

                        m.put(item.get("name").toString(),menuItems[i]);
                        Log.i("MENU",String.valueOf(snapshot.child("notOfferedMeals").child("gross0").child("name").getValue()));
                        i++;
                    }
                    menu[0] = new Menu(m);
                } else {
                    menu[0] = new Menu();
                }
            }
        });

        return menu[0];
    }

    public void addMeal(MenuItem item) {
        cookReference.child(getEmail()).child("Menu").child("notOfferedMeals").child(item.getName()).setValue(item);
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
