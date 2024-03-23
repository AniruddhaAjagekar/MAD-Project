package com.example.typingtestproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.lang.reflect.Executable;

public class FlashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(intent);

                finish();
            }
        }, 2000);
    }
}