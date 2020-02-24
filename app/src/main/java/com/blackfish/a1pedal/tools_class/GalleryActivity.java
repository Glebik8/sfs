package com.blackfish.a1pedal.tools_class;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blackfish.a1pedal.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);
        ImageView image = findViewById(R.id.ImageView);
       TextView BackText = findViewById(R.id.BackText);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        String urlcont = intent.getStringExtra("url");
        File path1 = new File(path);
        if (!path1.exists()){
            try {
                Picasso.get().load(urlcont).into(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Glide.with(GalleryActivity.this).load(path1).into(image);
        }
        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
finish();
            }
        });


    }}
