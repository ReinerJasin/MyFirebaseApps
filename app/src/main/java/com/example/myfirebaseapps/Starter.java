package com.example.myfirebaseapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Starter extends AppCompatActivity {

    Button course, lecturer, login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        //findViewById
        course = findViewById(R.id.button_course);
        lecturer = findViewById(R.id.button_lecturer);
        register = findViewById(R.id.button_student);
        login = findViewById(R.id.button_login);

    }

    public void button_student_clicked(View view) {
        Intent intent = new Intent(Starter.this, StudentRegister.class);
        intent.putExtra("action", "add");
        startActivity(intent);
        finish();
    }

    public void button_lecturer_clicked(View view) {
        Intent intent = new Intent(Starter.this, AddLecturer.class);
        intent.putExtra("action", "add");
        startActivity(intent);
        finish();
    }

    public void button_course_clicked(View view) {
        Intent intent = new Intent(Starter.this, AddCourse.class);
        intent.putExtra("action", "add");
        startActivity(intent);
        finish();
    }

    public void button_login_clicked(View view) {
        Intent intent = new Intent(Starter.this, StudentLogin.class);
        startActivity(intent);
        finish();
    }
}