package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdminHome extends AppCompatActivity {
    ListView complaintListView;
    String testArray[] = {"Complaint 1", "Complaint 2", "Complaint 3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Buttons
        MaterialButton signOutButton = (MaterialButton) findViewById(R.id.signOutButton);

        // List view
        complaintListView = (ListView) findViewById(R.id.complaintListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.complaint_list_item, R.id.textView, testArray);
        complaintListView.setAdapter(arrayAdapter);

        // Opens info when complaint is pressed
        complaintListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle("Manage Complaint");

                // Add the buttons
                builder.setPositiveButton("Suspend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Suspend button
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


                // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();

                return false;
            }
        });

        // Signs out user
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminHome.this.finish();
            }
        });
    }
}