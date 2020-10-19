package com.example.myfirebaseapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

public class AddCourse extends AppCompatActivity implements TextWatcher {

    TextInputLayout course_subject;
    Spinner spinner_day, spinner_time, spinner_time_end, spinner_lecturer;
    String course, day, time, time_end, lecturer;
    Button submit_button;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        //findViewById
        spinner_day = findViewById(R.id.spinner_day);
        spinner_time = findViewById(R.id.spinner_time);
        spinner_time_end = findViewById(R.id.spinner_time_end);
        spinner_lecturer = findViewById(R.id.spinner_lecturer);
        course_subject = findViewById(R.id.course_subject);
        submit_button = findViewById(R.id.button_add_course);
        toolbar = findViewById(R.id.toolbar_AddCourse);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Adapter spinner day
        ArrayAdapter<CharSequence> adapter_day = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapter_day);

        //Adapter spinner time
        ArrayAdapter<CharSequence> adapter_time = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapter_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time.setAdapter(adapter_time);

        //Adapter spinner time end
        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapterend = null;
                if (position == 0) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0730, android.R.layout.simple_spinner_item);
                } else if (position == 1) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0800, android.R.layout.simple_spinner_item);
                } else if (position == 2) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0830, android.R.layout.simple_spinner_item);
                } else if (position == 3) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0900, android.R.layout.simple_spinner_item);
                } else if (position == 4) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0930, android.R.layout.simple_spinner_item);
                } else if (position == 5) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1000, android.R.layout.simple_spinner_item);
                } else if (position == 6) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1030, android.R.layout.simple_spinner_item);
                } else if (position == 7) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1100, android.R.layout.simple_spinner_item);
                } else if (position == 8) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1130, android.R.layout.simple_spinner_item);
                } else if (position == 9) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1200, android.R.layout.simple_spinner_item);
                } else if (position == 10) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1230, android.R.layout.simple_spinner_item);
                } else if (position == 11) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1300, android.R.layout.simple_spinner_item);
                } else if (position == 12) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1330, android.R.layout.simple_spinner_item);
                } else if (position == 13) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1400, android.R.layout.simple_spinner_item);
                } else if (position == 14) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1430, android.R.layout.simple_spinner_item);
                } else if (position == 15) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1500, android.R.layout.simple_spinner_item);
                } else if (position == 16) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1530, android.R.layout.simple_spinner_item);
                } else if (position == 17) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1600, android.R.layout.simple_spinner_item);
                } else if (position == 18) {
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1630, android.R.layout.simple_spinner_item);
                }

                adapterend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_time_end.setAdapter(adapterend);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Adapter spinner lecturer
        ArrayAdapter<CharSequence> adapter_lecturer = ArrayAdapter.createFromResource(this, R.array.lecturer, android.R.layout.simple_spinner_item);
        adapter_lecturer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lecturer.setAdapter(adapter_lecturer);

        submit_button.setEnabled(false);
        course_subject.getEditText().addTextChangedListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCourse.this, Starter.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        course = course_subject.getEditText().getText().toString().trim();
        day = spinner_day.getSelectedItem().toString();
        time = spinner_time.getSelectedItem().toString();
        time_end = spinner_time_end.getSelectedItem().toString();
        lecturer = spinner_lecturer.getSelectedItem().toString();

        if (!course.isEmpty() && !day.isEmpty() && !time.isEmpty() && !time_end.isEmpty() && !lecturer.isEmpty()) {
            Log.d("button condition", "button enabled");
            submit_button.setEnabled(true);
        } else {
            Log.d("button condition", "button disabled");
            submit_button.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}