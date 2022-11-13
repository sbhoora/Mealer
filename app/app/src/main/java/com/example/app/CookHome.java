package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CookHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_home);

        MaterialButton addMenuButton = (MaterialButton) findViewById(R.id.addMenu);

        addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MenuItem> items = new ArrayList<>();

                for (int i = 0; i < 5; i++) {
                    MenuItem item = new MenuItem("n" + i,"", "a" + i );
                    items.add(item);
                }

                Menu menu = new Menu("My Menu", items);

                Cook alfredo = new Cook("alfredolinguini@pastacom", "pasta", false, false);

                alfredo.save(menu);

                Menu newMenu = alfredo.getMenu();
            }
        });

    }
}