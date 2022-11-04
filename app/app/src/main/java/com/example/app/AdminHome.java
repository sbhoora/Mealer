package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdminHome extends AppCompatActivity {
    ListView complaintListView;
    DatabaseReference database;
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
        complaintListView = (ListView) findViewById(R.id.complaintListView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Ended Activity: AdminHome");
    }

    private void update(){
        // The list of complaints, should be obtained from the database
        String testArray[] = {"Complaint 1", "Complaint 2", "Complaint 3"};
        Context cntx = this;

        database = FirebaseDatabase.getInstance().getReference("Accounts");
        database.child("admin").child("Complaints").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                Complaint compArray[] = new Complaint[Math.toIntExact(dataSnapshot.getChildrenCount())];
                String compTitleArray[] = new String[Math.toIntExact(dataSnapshot.getChildrenCount())];
                Log.i("SIZE",Integer.toString(compArray.length));

                Map<String,Object> compMap =  (Map<String,Object>) dataSnapshot.getValue();
                if(compMap!=null) {

                    int i = 0;

                    for (Map.Entry<String, Object> entry : compMap.entrySet()) {
                        Map comp = (Map) entry.getValue();
                        compArray[i] = new Complaint(comp.get("subject").toString(), comp.get("complaintAbout").toString(),
                                comp.get("complaint").toString());
                        compTitleArray[i] = comp.get("subject").toString();
                        i++;
                    }
                }
                //Log.i("ARRAY",compArray[0].subject);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(cntx, R.layout.complaint_list_item, R.id.textView, compTitleArray);
                complaintListView.setAdapter(arrayAdapter);

                // Opens info when complaint is pressed
                complaintListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Information about the complaint, change this to match database
                        String title, message, cookEmail;
                        title = compArray[i].getSubject();
                        cookEmail = compArray[i].getComplaintAbout().replace(".", "");
                        message = compArray[i].getComplaint();
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
                                database.child("Cooks").child(cookEmail).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        database.child("Cooks").child(cookEmail).child("suspended").setValue(true);
                                    }
                                });
                                dialog.cancel();
                                delete(cookEmail);
                                update();
                            }
                        });
                        // Add the buttons
                        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Dismiss button
                                dialog.cancel();
                                delete(cookEmail);
                                update();
                            }
                        });
                        builder.setNegativeButton("Ban", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                database.child("Cooks").child(cookEmail).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        database.child("Cooks").child(cookEmail).child("banned").setValue(true);
                                    }
                                });
                                dialog.cancel();
                                delete(cookEmail);
                                update();
                            }
                        });

                        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });

    }

    public void delete(String email) {
        database = FirebaseDatabase.getInstance().getReference("Accounts").child("admin").child("Complaints");
        database.child(email.replace(".", "")).removeValue();
        Log.i("DELETE","PASS");
    }

}