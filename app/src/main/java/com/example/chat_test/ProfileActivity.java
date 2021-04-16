package com.example.chat_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ProfileActivity extends AppCompatActivity {

    ImageView image;
    TextView text1, text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        image = findViewById(R.id.image);
        text1 = findViewById(R.id.u_name);
        text2 = findViewById(R.id.u_email);

        SharedPreferences sp = getSharedPreferences("Chat_Test", Context.MODE_PRIVATE);
        text1.setText(sp.getString("Name", ""));
        text2.setText(sp.getString("Email", ""));

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("Profile", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, "user.jpg");
        Picasso.get().load(myImageFile).into(image);
    }
}