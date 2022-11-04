package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdminHome extends AppCompatActivity {
    ListView complaintListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Console Message
        System.out.println("New Activity: AdminHome");

        // Buttons
        MaterialButton signOutButton = (MaterialButton) findViewById(R.id.signOutButton);

        update();

        // Signs out user
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminHome.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Ended Activity: AdminHome");
    }
    private void update(){
        // The list of complaints, should be obtained from the database
        String testArray[] = {"Complaint 1", "Complaint 2", "Complaint 3"};
        // List view
        complaintListView = (ListView) findViewById(R.id.complaintListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.complaint_list_item, R.id.textView, testArray);
        complaintListView.setAdapter(arrayAdapter);

        // Opens info when complaint is pressed
        complaintListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Information about the complaint, change this to match database
                String title, message, cookEmail;
                title = "No Title";
                cookEmail = "cook@email.com";
                message = "No Message";
                // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogTheme));

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle("Manage Complaint");
                builder.setMessage(String.format("Title: %s\nCook Email: %s\nMessage: %s\n\nSuspend Until (Blank for permanent)", title, cookEmail, message ));

                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                input.setHint("DD/MM/YYYY");
                builder.setView(input);


                // Add the buttons
                builder.setPositiveButton("Suspend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Suspend button
                        dialog.cancel();
                        update();
                    }
                });
                // Add the buttons
                builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Dismiss button
                        dialog.cancel();
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });

                // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}