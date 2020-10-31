package com.example.myfirebaseapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myfirebaseapps.Model.Course;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCourse extends AppCompatActivity implements TextWatcher {

    TextInputLayout course_subject;
    Spinner spinner_day, spinner_time, spinner_time_end, spinner_lecturer;
    String course, day, time, time_end, lecturer, action = "";
    Button submit_button;
    Toolbar toolbar;

    Course course_object;

    private DatabaseReference mDatabase;
    List<String> lecturer_list;
    ArrayAdapter<CharSequence> adapterend;

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

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                adapterend = null;
                set_spinner_time_end(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Adapter spinner lecturer
        lecturer_list = new ArrayList<>();
        showSpinnerLecturer();

        action = getIntent().getStringExtra("action");
        if(action.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle("Add Course");

            submit_button.setEnabled(false);
            course_subject.getEditText().addTextChangedListener(this);

            submit_button.setText("Add Course");
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    course = course_subject.getEditText().getText().toString().trim();
                    lecturer = spinner_lecturer.getSelectedItem().toString();
                    day = spinner_day.getSelectedItem().toString();
                    time = spinner_time.getSelectedItem().toString();
                    time_end = spinner_time_end.getSelectedItem().toString();
                    addCourse(course, lecturer, day, time, time_end);
                }
            });
        }else if(action.equalsIgnoreCase("edit")){
            getSupportActionBar().setTitle("Edit Course");
            submit_button.setText("Edit Course");
            course_object = getIntent().getParcelableExtra("edit_data_course");

            course_subject.getEditText().setText(course_object.getSubject());

            int dayIndex = adapter_day.getPosition(course_object.getDay());
            spinner_day.setSelection(dayIndex);

            int startIndex = adapter_time.getPosition(course_object.getStart());
            spinner_time.setSelection(startIndex);

            set_spinner_time_end(startIndex);
            final int endIndex = adapterend.getPosition(course_object.getEnd());
            spinner_time_end.setSelection(endIndex);

            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    course = course_subject.getEditText().getText().toString().trim();
                    lecturer = spinner_lecturer.getSelectedItem().toString();
                    day = spinner_day.getSelectedItem().toString();
                    time = spinner_time.getSelectedItem().toString();
                    time_end = spinner_time_end.getSelectedItem().toString();
                    Map<String, Object> params = new HashMap<>();
                    params.put("subject", course);
                    params.put("lecturer", lecturer);
                    params.put("day", day);
                    params.put("start", time);
                    params.put("end", time_end);
                    mDatabase.child("course").child(course_object.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent;
                            intent = new Intent(AddCourse.this, CourseData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
        ArrayAdapter<CharSequence> adapter_lecturer = ArrayAdapter.createFromResource(this, R.array.lecturer, android.R.layout.simple_spinner_item);
        adapter_lecturer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lecturer.setAdapter(adapter_lecturer);

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

    public void addCourse(String course, String lecturer, String day, String time, String time_end) {//add course
        String mid = mDatabase.child("course").push().getKey();
        Course course_temp = new Course(mid,course, day, time, time_end, lecturer);
        mDatabase.child("course").child(mid).setValue(course_temp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddCourse.this, "Add Course Successfully", Toast.LENGTH_SHORT).show();
                course_subject.getEditText().setText("");
                spinner_lecturer.setSelection(0);
                spinner_time.setSelection(0);
                spinner_time_end.setSelection(0);
                spinner_day.setSelection(0);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                dialog.cancel();
                Toast.makeText(AddCourse.this, "Add Course Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showSpinnerLecturer(){
        mDatabase.child("lecturer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    String spinner_lecturer = childSnapshot.child("name").getValue(String.class);
                    lecturer_list.add(spinner_lecturer);
                }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(AddCourse.this, android.R.layout.simple_spinner_item,lecturer_list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_lecturer.setAdapter(arrayAdapter);
                if (action.equalsIgnoreCase("edit")){
                    int index = arrayAdapter.getPosition(course_object.getLecturer());
                    spinner_lecturer.setSelection(index);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(AddCourse.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }else if(id == R.id.course_list){
            Intent intent;
            intent = new Intent(AddCourse.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AddCourse.this, Starter.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    public void set_spinner_time_end(int position){
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
}