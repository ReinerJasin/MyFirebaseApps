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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myfirebaseapps.Fragment.AccountFragment;
import com.example.myfirebaseapps.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StudentRegister extends AppCompatActivity implements TextWatcher {

    Toolbar toolbar;
    Dialog dialog;
    TextInputLayout input_email, input_password, input_name, input_nim, input_age, input_address;
    RadioGroup rg_gender;
    RadioButton radioButton;
    Button btn_register;
    String uid = "", email = "", pass = "", name = "", nim = "", age = "", gender = "male", address = "", action = "";
    int position = 0;
    Student student;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        //findViewById
        toolbar = findViewById(R.id.toolbar_StudentRegister);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dialog = Glovar.loadingDialog(StudentRegister.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_name = findViewById(R.id.input_name);
        input_nim = findViewById(R.id.input_nim);
        input_age = findViewById(R.id.input_age);
        input_address = findViewById(R.id.input_address);
        btn_register = findViewById(R.id.button_register);
        rg_gender = findViewById(R.id.radioGender);

        btn_register.setEnabled(false);

        //implement text watcher
        input_email.getEditText().addTextChangedListener(this);
        input_password.getEditText().addTextChangedListener(this);
        input_name.getEditText().addTextChangedListener(this);
        input_nim.getEditText().addTextChangedListener(this);
        input_age.getEditText().addTextChangedListener(this);
        input_address.getEditText().addTextChangedListener(this);

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                gender = radioButton.getText().toString();
            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        position = intent.getIntExtra("position", 0);
        student = intent.getParcelableExtra("data_student");

        if (action.equalsIgnoreCase("add")) {
            toolbar.setTitle("Register Student");
            btn_register.setText("Register Student");
        } else if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("login")) {
            toolbar.setTitle("Edit Student");
            btn_register.setText("Edit Student");

            input_email.getEditText().setText(student.getEmail());
            input_password.getEditText().setText(student.getPassword());
            input_name.getEditText().setText(student.getName());
            input_nim.getEditText().setText(student.getNim());
            input_age.getEditText().setText(student.getAge());
            input_address.getEditText().setText(student.getAddress());
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equalsIgnoreCase("add")) {
                    addStudent();
                } else if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("login")) {
                    editStudent();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRegister.this, Starter.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void editStudent() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        email = input_email.getEditText().getText().toString().trim();
        pass = input_password.getEditText().getText().toString().trim();
        name = input_name.getEditText().getText().toString().trim();
        nim = input_nim.getEditText().getText().toString().trim();
        age = input_age.getEditText().getText().toString().trim();
        address = input_address.getEditText().getText().toString().trim();

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("pass", pass);
        params.put("name", name);
        params.put("nim", nim);
        params.put("age", age);
        params.put("address", address);

        mDatabase.child("student").child(student.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.cancel();
                Intent intent;
                if(action.equalsIgnoreCase("login")){
                    intent = new Intent(StudentRegister.this, StudentMainActivity.class);
                    intent.putExtra("action", "login");
                }else {
                    intent = new Intent(StudentRegister.this, StudentData.class);
                    intent.putExtra("action", "edit");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentRegister.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
    }

    public void addStudent() {
        getFormValue();
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(StudentRegister.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.cancel();
                    uid = mAuth.getCurrentUser().getUid();
                    Student student = new Student(uid, email, pass, name, nim, gender, age, address);
//                    Log.d("isi form", uid);
//                    Log.d("isi form", email);
//                    Log.d("isi form", pass);
//                    Log.d("isi form", name);
//                    Log.d("isi form", nim);
//                    Log.d("isi form", gender);
//                    Log.d("isi form", age);
//                    Log.d("isi form", address);
                    mDatabase.child("student").child(uid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(StudentRegister.this, "Student register Successful", Toast.LENGTH_SHORT).show();

                            Intent intent1 = new Intent(StudentRegister.this, Starter.class);
                            startActivity(intent1);
                            finish();
                        }
                    });
                    mAuth.signOut();
                } else {
                    Log.d("testing", "nd masuk");
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException malFormed) {
                        Toast.makeText(StudentRegister.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthUserCollisionException existEmail) {
                        Toast.makeText(StudentRegister.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(StudentRegister.this, "Register failed!", Toast.LENGTH_SHORT).show();
                    }
                    dialog.cancel();
                }
            }
        });
    }

    public void getFormValue() {
        email = input_email.getEditText().getText().toString().trim();
        pass = input_password.getEditText().getText().toString().trim();
        name = input_name.getEditText().getText().toString().trim();
        nim = input_nim.getEditText().getText().toString().trim();
        age = input_age.getEditText().getText().toString().trim();
        address = input_address.getEditText().getText().toString().trim();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getFormValue();
        if (!email.isEmpty() && !pass.isEmpty() && !name.isEmpty() && !nim.isEmpty() && !age.isEmpty() && !address.isEmpty()) {
            btn_register.setEnabled(true);
        } else {
            btn_register.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(StudentRegister.this, Starter.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentRegister.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.student_list) {
            Intent intent;
            intent = new Intent(StudentRegister.this, StudentData.class);
            intent.putExtra("action", "not_delete");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentRegister.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }
}