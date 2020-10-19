package com.example.myfirebaseapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class StudentLogin extends AppCompatActivity implements TextWatcher {

    Toolbar toolbar;
    Button btn_login;
    TextInputLayout login_email, login_pass;
    String email = "", pass = "";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        //findViewById
        toolbar = findViewById(R.id.toolbar_StudentLogin);
        btn_login = findViewById(R.id.button_student_login);
        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        login_email.getEditText().addTextChangedListener(this);
        login_pass.getEditText().addTextChangedListener(this);

        btn_login.setEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLogin.this, Starter.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(StudentLogin.this, "All field are required!", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(StudentLogin.this, StudentMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(StudentLogin.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        email = login_email.getEditText().getText().toString().trim();
        pass = login_pass.getEditText().getText().toString().trim();

        if(email.isEmpty() || pass.isEmpty()){
            btn_login.setEnabled(false);
        }else{
            btn_login.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}