package com.example.myfirebaseapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.myfirebaseapps.Fragment.AccountFragment;
import com.example.myfirebaseapps.Fragment.CourseFragment;
import com.example.myfirebaseapps.Fragment.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    String action;
    Toolbar toolbar;
    Button button_logout;
    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        toolbar = findViewById(R.id.toolbar_StudentMain);
        button_logout = findViewById(R.id.button_logout);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if(action.equalsIgnoreCase("login")){

            bottomNav.setSelectedItemId(R.id.nav_account);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();

        }else{

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFragment()).commit();

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_schedule:
                    selectedFragment = new ScheduleFragment();
                    toolbar.setTitle("Schedule");
                    break;
                case R.id.nav_course:
                    selectedFragment = new CourseFragment();
                    toolbar.setTitle("Course");
                    break;
                case R.id.nav_account:
                    selectedFragment = new AccountFragment();
                    toolbar.setTitle("Account");
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

}