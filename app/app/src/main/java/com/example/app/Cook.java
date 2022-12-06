package com.example.app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

    public Cook() {
        super();
        // Default Constructor must be defined to retrieve as object from database
    }

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
                        if (isSuspended() || isBanned()) {
                            // Create a menu with an empty offeredMealList
                            Menu emptyOfferedMenu = new Menu();
                            emptyOfferedMenu.addAsOfferedMeal(new ArrayList<MenuItem>());
                            // When the meals try to be updated on the main Meals branch, an empty
                            // offeredList is sent. This way, per the updateOfferedMealsOnDatabase()
                            // definition, all meals with the cook's email are removed and no new ones
                            // are added.
                            MenuItem.updateOfferedMealsOnDatabase(Cook.this, emptyOfferedMenu);
                        }
                        MenuItem.updateOfferedMealsOnDatabase(Cook.this,menu);
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
        // Created a "custom callback" to deal with
        // asynchronous nature of firebase retrieve methods
        getMenu(new FirebaseMenuCallback() {
            @Override
            public void onCallBack(Menu firebaseMenu) {
                setMenu(firebaseMenu);
            }
        });
        return menu;
    }

    // What follows is Russian man solution to asynchronous callback //
    // Allah bless this man //

    public void getMenu(FirebaseMenuCallback callback) {
        cookReference.child(getEmail()).child("Menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Firebase", "Menu Retrieve Successful for cook: " + getEmail());
                Menu menuFromFirebase = snapshot.getValue(Menu.class);
                callback.onCallBack(menuFromFirebase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Menu Retrieve Failed for cook:" + getEmail());
            }
        });
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

    public Boolean getBanned() {
        return banned;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    // Setters
    public void suspend(Date suspendedUntil) {
        this.suspendedUntil = suspendedUntil;
        suspended = true;
    }

    public Boolean isSuspended() { return suspended;};

    public Boolean isBanned() { return banned; };

    private void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public void setSuspendedUntil(Date suspendedUntil) {
        this.suspendedUntil = suspendedUntil;
    }

    public interface FirebaseMenuCallback {
        void onCallBack(Menu menu);
    }
}
