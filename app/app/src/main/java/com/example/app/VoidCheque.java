package com.example.app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class VoidCheque extends AppCompatActivity {

    ImageView cheque;
    Button upload, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void_cheque);

        System.out.println("New Activity: VoidCheque");

        cheque = (ImageView) findViewById(R.id.voidCheque);
        upload = (Button) findViewById(R.id.uploadVoidChequeButton);
        done = (Button) findViewById(R.id.doneButton);

        // Opens camera or gallery to upload void cheque picture
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(VoidCheque.this)
                        .crop(16f, 9f)                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        // Ends activity
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoidCheque.this.finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Ended Activity: VoidCheque");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        cheque.setImageURI(uri);
    }
}