package com.example.myfirebaseapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.example.myfirebaseapps.Model.Lecturer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class AddLecturer extends AppCompatActivity implements TextWatcher {

    TextInputLayout lecturer_input_name, lecturer_input_expertise;
    String lecturer_name, lecturer_gender, lecturer_expertise;
    RadioGroup rg;
    RadioButton rb;
    Button submit_button;
    Toolbar toolbar;
    Dialog dialog;
    String action = "";
    Lecturer lecturer;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        dialog = Glovar.loadingDialog(AddLecturer.this);

        //findViewById
        rg = findViewById(R.id.radioGroup);
        rb = findViewById(R.id.radioButton);
        lecturer_input_name = findViewById(R.id.lecturer_input_name);
        lecturer_input_expertise = findViewById(R.id.lecturer_input_expertise);
        submit_button = findViewById(R.id.button_add_lecturer);
        toolbar = findViewById(R.id.toolbar_AddLecturer);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        submit_button.setEnabled(false);
        lecturer_input_name.getEditText().addTextChangedListener(this);
        lecturer_input_expertise.getEditText().addTextChangedListener(this);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = findViewById(checkedId);
                lecturer_gender = rb.getText().toString();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddLecturer.this, Starter.class);
                startActivity(intent);
                finish();
            }
        });


        lecturer_name = lecturer_input_name.getEditText().getText().toString();
        lecturer_expertise = lecturer_input_expertise.getEditText().getText().toString();

//        if (lecturer_gender.equalsIgnoreCase("Male")){
//            lecturer_gender = "m";
//        }else if(lecturer_gender.equalsIgnoreCase("Female")){
//            lecturer_gender = "f";
//        }

        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if(action.equalsIgnoreCase("edit")){

            toolbar.setTitle("Edit Lecturer");
            submit_button.setText("Save Changes");

            lecturer = intent.getParcelableExtra("edit_data_lect");

            lecturer_input_name.getEditText().setText(lecturer.getName());
            lecturer_input_expertise.getEditText().setText(lecturer.getExpertise());

            if(lecturer.getGender().equalsIgnoreCase("male")){
                rg.check(R.id.radioButton);
            }else{
                rg.check(R.id.radioButton2);
            }

            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    lecturer_name = lecturer_input_name.getEditText().getText().toString().trim();
                    lecturer_expertise = lecturer_input_expertise.getEditText().getText().toString().trim();
                    Map<String,Object> params = new HashMap<>();
                    params.put("name", lecturer_name);
                    params.put("expertise", lecturer_expertise);
                    params.put("gender", lecturer_gender);
                    mDatabase.child("lecturer").child(lecturer.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent;
                            intent = new Intent(AddLecturer.this, LecturerData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturer.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });

        }else{
            toolbar.setTitle("Add Lecturer");
            submit_button.setText("Add Lecturer");

            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addLecturer(lecturer_name, lecturer_gender, lecturer_expertise);
                }
            });

        }




    }

    public void addLecturer(String mnama, String mgender, String mexpertise) {
        dialog.show();
        String mid = mDatabase.child("lecturer").push().getKey();
        Lecturer lecturer = new Lecturer(mid, mnama, mgender, mexpertise);

        //kalau hanya seperti ini, kita tidak akan tau apabila terjadi error kecuali kita mengecek di logcat
        //mDatabase.child("lecturer").child(mid).setValue(lecturer);

        //tambahan pengecek success dan failure untuk melakukan sesuatu apabila error atau berhasil
        mDatabase.child("lecturer").child(mid).setValue(lecturer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.cancel();
                Intent intent = new Intent(AddLecturer.this, Starter.class);
                startActivity(intent);
                Toast.makeText(AddLecturer.this, "Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        lecturer_name = lecturer_input_name.getEditText().getText().toString();
        lecturer_expertise = lecturer_input_expertise.getEditText().getText().toString();

        lecturer_gender = rb.getText().toString();


        if (!lecturer_name.isEmpty() && !lecturer_gender.isEmpty() && !lecturer_expertise.isEmpty()) {
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

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AddLecturer.this, Starter.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturer.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lecturer_list) {
            Intent intent;
            intent = new Intent(AddLecturer.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturer.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }
}