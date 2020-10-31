package com.example.myfirebaseapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myfirebaseapps.Model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {

    private int loading_time = 1500;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mUser != null) {

                    intent = new Intent(SplashScreen.this, StudentMainActivity.class);
                    intent.putExtra("action", "reicakep");
                    startActivity(intent);

                } else {

                    intent = new Intent(SplashScreen.this, Starter.class);
                    startActivity(intent);

                }
                finish();
            }
        }, loading_time);
    }
}