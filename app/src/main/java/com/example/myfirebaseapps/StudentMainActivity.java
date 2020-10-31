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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentMainActivity extends AppCompatActivity {

    String action;
    Toolbar toolbar;
    Button button_logout;
    FirebaseUser mUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        toolbar = findViewById(R.id.toolbar_StudentMain);
        button_logout = findViewById(R.id.button_logout);
//
//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if(action.equalsIgnoreCase("login")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
            toolbar.setTitle("Account");
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

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
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case button:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                return true;
//        }
//        return false;
//    }

}