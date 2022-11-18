package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;


public class CookHome extends AppCompatActivity {

    private ListView mealListView;
    private DatabaseReference database;
    private Cook cook;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_home);

        // Buttons
        MaterialButton signOutButton = (MaterialButton) findViewById(R.id.signOutButton);
        Button addMealButton = (Button) findViewById(R.id.addMealButton);

        // Value retrieved from SignIn for the cook that just signed in
        email = getIntent().getStringExtra("email");


        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CookHome.this, MealCreation.class));
                update();
            }
        });

        mealListView = (ListView) findViewById(R.id.mealListView);

        update();

        // Signs out user
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CookHome.this.finish();
            }
        });
    }
    private void update(){
        Context cntx = this;

        // Soooo, it seems a cook object will never be able to have a menu object that gets its
        // value from the database
        // The reason for that? //
            // The Firebase methods/listeners (etc.) , which are APIs, are asynchronous, as are
            // most web related functions. This means data is not returned synchronously,
            // at the time the method is called. What this means for us is the method call may be
            // over with the compiler going to the next thing before the value is returned.
            // While the value is eventually returned, it is only after the method call is over.
            // This means the menu field of the cook cannot be assigned a value inside that listener
            // since that value has not yet been returned. While it may exist inside the listener,
            // Once it leaves it, the value actually not yet been returned, and hence not actually
            // assigning that value to the instance variable.
        // The solution? //
            // In our case, the solution to still be able to use the menu value from the database
            // outside of the listener is what Russian man proposed, which allows us to pass the value
            // through an onCallback() and access it inside there from wherever, leaving only the
            // one firebase listener wherever it is placed (in this case, the Cook class).
            // Here is link https://www.youtube.com/watch?v=OvDZVV5CbQg Solution starts at [3:20]
            // However, this means the menu value can only ever be accessed inside the onCallBack() too.
            // This brings us back to the same issue of it only being accessible inside the listener,
            // except that it is now the onCallBack(). So, there is no simple solution.
        // Conclusion //
        // What we have below is the best we can have. It does not eliminate the restrictions
        // that the listener imposes on the code since the onCallBack() ends up doing the same.
        // However, this allows for the getMenu() to be associated with a cook object, providing
        // clearer separation of actions and what objects they are related to.
        // TLDR: //
            // Anytime you want to access the menu from the database, use the onCallBack() method.
            // Yeah it looks just like a firebase reference. c( 0_ 0) i have done bad.
        Cook cook = new Cook(email);
        cook.getMenu(new Cook.FirebaseMenuCallback() {
            @Override
            public void onCallBack(Menu menu) {

                Menu myMenu = menu;
                // ARRAY OF MEALS

                // TODO: Double check that menu has offeredMeals, else it returns null and crashes
                // Currently commented out because the current Menu object in the db for qw does not
                // have an offeredMeal, so it just returns null and crashes.
//                MenuItem offered[] = menu.getOfferedMeals().values().toArray(new MenuItem[0]);
                // TODO: Double check that menu has notOfferedMeals, else it returns null and crashes
                MenuItem notOffered[] = menu.getNotOfferedMeals().values().toArray(new MenuItem[0]);
                // ARRAY OF MEAL TITLESa
                // TODO: Double check that menu has offeredMeals, else it returns null and crashes
//                String offeredTitle[] = menu.getOfferedMeals().keySet().toArray(new String[0]);
                // TODO: Double check that menu has notOfferedMeals, else it returns null and crashes
                String notOfferedTitle[] = menu.getNotOfferedMeals().keySet().toArray(new String[0]);
                // (IF YOU WANT TO SHOW THEIR OFFERED STATUS SIMPLY APPEND (OFFERED) TO THE END OF THE TITLE STRING)

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(cntx, R.layout.complaint_list_item, R.id.textView, notOfferedTitle);
                mealListView.setAdapter(arrayAdapter);

                mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogTheme));
                        // USE THE ARRAY OF MEALS FROM ABOVE AT INDEX i TO GET THE DATA OF THIS MEAL
                        builder.setTitle("Manage Meal");

                        final CheckBox input = new CheckBox(view.getContext());
                        input.setText("Offered");
                        builder.setView(input);

                        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                boolean newOffered = input.isChecked();
                                // APPLY THE CHANGES TO THE DATABASE

                                dialog.cancel();
                                update();
                            }
                        });
                        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // DELETE MEAL FROM DATABASE

                                dialog.cancel();
                                update();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });


    }
}